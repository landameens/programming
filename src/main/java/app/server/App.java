package app.server;

import controller.Controller;
import controller.Interpretator;
import domain.commandsRepository.HistoryRepository;
import domain.commandsRepository.ICommandsRepository;
import domain.exception.VerifyException;
import domain.studyGroupFactory.StudyGroupFactory;
import domain.studyGroupFactory.idProducer.IdProducer;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.TreeSetStudyGroupRepository;
import manager.LogManager;
import server.Server;
import storage.exception.DAOException;

import java.io.File;


public class App {
    private static final String ARGUMENTS_ERROR = "Введено слишком много аргументов, повторите ввод директории," +
            " куда будет сохраняться коллекция и сопутсвующие файлы";
    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);

    public static void main(String[] args) {
        String pathForAppFiles = null;
        if (args.length > 0) {
            checkInputPath(args);

            pathForAppFiles = args[0];

            File file = new File(pathForAppFiles);
            if (!file.exists()) {
                System.err.println("Такого файла не существует. Проверьте наличие такого файла и повторите попытку.");
                System.exit(1);
            }

            if (!file.canExecute()) {
                System.err.println("Недостаточно прав. Пожалуйста, предоставьте права доступа и повторите попытку.");
                System.exit(1);
            }
        }

        IdProducer idProducer = new IdProducer(pathForAppFiles);
        LOG_MANAGER.debug("IdProducer was created SUCCESSFUL.");
        StudyGroupFactory studyGroupFactory = new StudyGroupFactory(idProducer);
        LOG_MANAGER.debug("StudyGroupFactory was created SUCCESFULL.");
        IStudyGroupRepository studyGroupRepository = null;
        try {
            studyGroupRepository = new TreeSetStudyGroupRepository(studyGroupFactory, pathForAppFiles);
            LOG_MANAGER.debug("IStudyGroupRepository was created SUCCESFULL.");
        } catch (DAOException | VerifyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        File directoryForStoringScripts = new File(pathForAppFiles + "/script");
        if (!directoryForStoringScripts.exists()){
            directoryForStoringScripts.mkdir();
        }

        ICommandsRepository commandsRepository = new HistoryRepository();
        LOG_MANAGER.debug("HstoryRepository was created SUCCESSFUL.");
        Interpretator interpretator = new Interpretator(studyGroupRepository, commandsRepository);
        LOG_MANAGER.debug("Interpretator was created SUCCESFULL.");
        Controller controller = new Controller(interpre tator, commandsRepository);
        LOG_MANAGER.debug("Controller was created SUCCESSFUL.");

        Server server = new Server(128, 45789, controller);
        LOG_MANAGER.debug("Server was created SUCCESSFUL.");

        LOG_MANAGER.info("Server is starting...");
        server.start();
    }

    private static void checkInputPath(String[] args) {
        if (args.length > 1) {
            System.err.println(ARGUMENTS_ERROR);
            System.exit(1);
        }
    }
}
