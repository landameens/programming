package controller.commands.scripts;

import app.query.CommandName;
import app.query.CommandType;
import app.Viewer;
import controller.commands.Command;
import controller.commands.Interpretator;
import controller.commands.factory.ICommandFactory;
import controller.response.Response;
import domain.commandsRepository.ICommandsRepository;
import domain.commandsRepository.Record;
import domain.exception.CreationException;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.TreeSetStudyGroupRepository;
import storage.exception.RecursionExeption;
import storage.scriptDAO.IScriptDAO;
import storage.scriptDAO.ScriptDAO;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ExecuteScriptCommand extends Command {
    private final Interpretator interpretator;
    private final Viewer viewer;
    private final ICommandsRepository history;
    private final RecursionChecker recursionChecker;

    //todo костыль
    private String directoryForStoringFiles;


    public ExecuteScriptCommand(String type,
                                Map<String, String> args,
                                IStudyGroupRepository studyGroupRepository,
                                ICommandsRepository commandsRepository,
                                RecursionChecker recursionChecker){
        super(type, args);
        this.history = commandsRepository;
        this.recursionChecker = recursionChecker;
        //todo перенести интерпретатор на сервер
        interpretator = new Interpretator(studyGroupRepository, commandsRepository, recursionChecker);
        viewer = new Viewer();

        initPathToScripts(studyGroupRepository);
    }

    /**
     * Method for executing command.
     * If it has the recursion, it throws Recursion exception.
     * @return response
     */
    @Override
    public Response execute() {
        IScriptDAO scriptDAO = new ScriptDAO(directoryForStoringFiles + "/" + args.get("file_name"));
        try {
            Script script = new Script();
            script.setTextScript(scriptDAO.getScript());
            if (recursionChecker.check(script.hashCode())){
                return executeScript(script);
            } else throw new RecursionExeption("ERROR: Обнаружена рекурсия при исполнении скриптов!");
        } catch (IOException | RecursionExeption e) {
            return getBadRequestResponseDTO(e.getMessage());
        }

    }

    private void initPathToScripts(IStudyGroupRepository studyGroupRepository) {
        String pathToAppFiles = ((TreeSetStudyGroupRepository) studyGroupRepository).getDirectoryForAppFiles();

        if (pathToAppFiles == null) {
            ClassLoader classLoader = TreeSetStudyGroupRepository.class.getClassLoader();
            URL url = classLoader.getResource("script");
            directoryForStoringFiles = url.getFile();
        } else {
            File directory = new File(pathToAppFiles);

            if (!directory.exists()) {
                directory.mkdir();
            }

            directoryForStoringFiles = pathToAppFiles;
        }
    }

    /**
     * Method for executng received script.
     * @param script
     * @return response
     */
    private Response executeScript(Script script){

        Iterator<String> iterator = script.getTextScript().iterator();

        StringBuilder answer = new StringBuilder();

        while (iterator.hasNext()){
            String line = iterator.next();

            if (line.isEmpty()){
                continue;
            }

            try {
                String[] commandArray = getCommandArray(line);
                String commandName = commandArray[0];

                Command command = createCommand(commandArray, iterator);

                addToHistory(commandName);

                String thisCommandAnswer = command.execute().getAnswer();
                answer = answer.append(thisCommandAnswer);

            } catch (CreationException e) {
                return getBadRequestResponseDTO(e.getMessage());
            }
        }
        return getSuccessfullyResponseDTO(answer.toString());
    }

    /**
     * This method create command and return them.
     * @param commandArray
     * @param iterator
     * @return command
     * @throws CreationException
     */
    private Command createCommand(String[] commandArray, Iterator<String> iterator) throws CreationException {
        String commandName = commandArray[0];
        ICommandFactory commandFactory = interpretator.getFactoryInstance(commandName);
        Map<String, String> args = getArguments(commandArray, iterator);

        return commandFactory.createCommand(commandName, args);
    }

    /**
     * This method adds the name of the executable command to the history.
     * @param commandName
     */
    private void addToHistory(String commandName) {
        Record commandDTO = new Record();
        commandDTO.name = commandName;
        history.add(commandDTO);
    }

    /**
     * This method gets command arguments depending on the type of command.
     * @param commandArray
     * @param iterator
     * @return  map of arguments
     */
    private Map<String, String> getArguments(String[] commandArray, Iterator<String> iterator) {
        Map<String, String> args = new HashMap<>();

        CommandType commandType = interpretator.interpretateCommandType(CommandName.getCommandNameEnum(commandArray[0]));

        if (commandType.equals(CommandType.COMPOUND_COMMAND)){
            args = getArgumentsForCompoundCommand(commandArray[0], iterator);
        }

        if (commandType.equals(CommandType.SIMPLE_COMMAND)){
            args = getArgumentsForSimpleCommand(commandArray);
        }

        return args;
    }

    private Map<String, String> getArgumentsForSimpleCommand(String[] commandArray) {
        CommandName commandName = CommandName.getCommandNameEnum(commandArray[0]);
        List<String> commandList = new ArrayList<>();
        Collections.addAll(commandList, commandArray);
        return interpretator.interpretateSimpleCommandArguments(commandName, commandList);
    }

    private String[] getCommandArray(String line) {
        line = line.trim();
        return line.split("[\\s]+");
    }

    private Map<String, String> getArgumentsForCompoundCommand(String commandName, Iterator<String> iterator) {
        Map<String,String> returnableArgs = new HashMap<>();
        Map<String, String> mapForInputArguments = interpretator.getMapForInputArguments(CommandName.getCommandNameEnum(commandName), viewer);

        for (Map.Entry<String,String> entry : mapForInputArguments.entrySet()) {
            String field = entry.getKey();

            String lineOfArgument = iterator.next();
            lineOfArgument = lineOfArgument.trim();

            returnableArgs.put(field, lineOfArgument);
        }

        return returnableArgs;
    }

}
