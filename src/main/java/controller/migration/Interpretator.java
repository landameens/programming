package controller.migration;

import controller.migration.commands.factory.*;
import controller.migration.commands.scripts.RecursionChecker;
import domain.commandsRepository.ICommandsRepository;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Interpretator {
    public final String ADD = "add";
    public final String HELP = "help";
    public final String SHOW = "show";
    public final String UPDATE = "update";
    public final String REMOVE_BY_ID = "remove_by_id";
    public final String CLEAR = "clear";
    public final String SAVE = "save";
    public final String EXIT = "exit";
    public final String ADD_IF_MIN = "add_if_min";
    public final String REMOVE_LOWER = "remove_lower";
    public final String COUNT_BY_GROUP_ADMIN = "count_by_group_admin";
    public final String FILTER_BY_SHOULD_BE_EXPELLED = "filter_by_should_be_expelled";
    public final String FILTER_LESS_THAN_SHOULD_BE_EXPELLED = "filter_less_than_should_be_expelled";
    public final String HISTORY = "history";
    public final String EXECUTE_SCRIPT = "execute_script";
    public final String INFO = "info";

    public final String LOGIN = "login";
    public final String SIGN_UP = "signUp";
    private final Set<String> LOGIN_CONTROLLER_COMMAND;

    private final Set<String> ALL_COMMANDS;

    private final ICommandFactory simpleCommandsFactory;
    private final ICommandFactory studyGroupRepositoryCommandFactory;
    private final ICommandFactory commandRepositoryFactory;
    private final ICommandFactory scriptCommandFactory;

    private final Map<String, Class<? extends ICommandFactory>> commandFactoryMap = new HashMap<String, Class<? extends ICommandFactory>>() {
        {
            put(HELP, SimpleCommandsFactory.class);
            put(SHOW, StudyGroupRepositoryCommandFactory.class);
            put(ADD, StudyGroupRepositoryCommandFactory.class);
            put(REMOVE_BY_ID, StudyGroupRepositoryCommandFactory.class);
            put(CLEAR, StudyGroupRepositoryCommandFactory.class);
            put(UPDATE, StudyGroupRepositoryCommandFactory.class);
            put(ADD_IF_MIN, StudyGroupRepositoryCommandFactory.class);
            //put(SAVE, StudyGroupRepositoryCommandFactory.class);
            put(REMOVE_LOWER, StudyGroupRepositoryCommandFactory.class);
            put(EXIT, StudyGroupRepositoryCommandFactory.class);
            put(FILTER_BY_SHOULD_BE_EXPELLED, StudyGroupRepositoryCommandFactory.class);
            put(FILTER_LESS_THAN_SHOULD_BE_EXPELLED, StudyGroupRepositoryCommandFactory.class);
            put(COUNT_BY_GROUP_ADMIN, StudyGroupRepositoryCommandFactory.class);
            //put(INFO, StudyGroupRepositoryCommandFactory.class);
            //put(EXECUTE_SCRIPT, ScriptCommandFactory.class);
            //put(HISTORY, HistoryRepositoryCommandFactory.class);
        }
    };

    public Interpretator(IStudyGroupRepository studyGroupRepository, ICommandsRepository commandsRepository) {
        simpleCommandsFactory = new SimpleCommandsFactory();
        studyGroupRepositoryCommandFactory = new StudyGroupRepositoryCommandFactory(studyGroupRepository);
        commandRepositoryFactory = new HistoryRepositoryCommandFactory(commandsRepository);
        scriptCommandFactory = new ScriptCommandFactory(studyGroupRepository, commandsRepository, new RecursionChecker());

        LOGIN_CONTROLLER_COMMAND = new HashSet<>();
        LOGIN_CONTROLLER_COMMAND.add(LOGIN);
        LOGIN_CONTROLLER_COMMAND.add(SIGN_UP);

        ALL_COMMANDS = initAllCommands();
    }

    private Set<String> initAllCommands() {
        Set<String> allCommands = new HashSet<>();
        allCommands.add(ADD);
        allCommands.add(HELP);
        allCommands.add(SHOW);
        allCommands.add(UPDATE);
        allCommands.add(REMOVE_BY_ID);
        allCommands.add(CLEAR);
        allCommands.add(SAVE);
        allCommands.add(EXIT);
        allCommands.add(ADD_IF_MIN);
        allCommands.add(REMOVE_LOWER);
        allCommands.add(COUNT_BY_GROUP_ADMIN);
        allCommands.add(FILTER_BY_SHOULD_BE_EXPELLED);
        allCommands.add(FILTER_LESS_THAN_SHOULD_BE_EXPELLED);
        allCommands.add(HISTORY);
        allCommands.add(EXECUTE_SCRIPT);
        allCommands.add(INFO);
        allCommands.add(LOGIN);
        allCommands.add(SIGN_UP);

        return allCommands;
    }

    public boolean isLoginControllerCommand(String commandName) {
        return LOGIN_CONTROLLER_COMMAND.contains(commandName);
    }

    public ICommandFactory getFactoryInstance(String name) {
        Class<? extends ICommandFactory> clazz = commandFactoryMap.get(name);

        if (clazz.equals(simpleCommandsFactory.getClass())) {
            return simpleCommandsFactory;
        }

        if (clazz.equals(studyGroupRepositoryCommandFactory.getClass())) {
            return studyGroupRepositoryCommandFactory;
        }

        if (clazz.equals(commandRepositoryFactory.getClass())) {
            return commandRepositoryFactory;
        }

        if (clazz.equals(scriptCommandFactory.getClass())) {
            return scriptCommandFactory;
        }

        return null;
    }

    public boolean isSuchCommandExists(String commandName) {
        return ALL_COMMANDS.contains(commandName) || LOGIN_CONTROLLER_COMMAND.contains(commandName);
    }
}
