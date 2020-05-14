package app;

import app.Exceptions.InputException;
import app.Exceptions.InternalException;
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
import storage.exception.DAOException;

import java.io.File;

public final class App {
    private static final String ARGUMENTS_ERROR = "Введено слишком много аргументов, повторите ввод директории," +
            " куда будет сохраняться коллекция и сопутсвующие файлы";

    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);

    public static void main(String[] args) {
        String pathForAppFiles = null;
        if (args.length > 0) {
            checkInputPath(args);

            pathForAppFiles = args[0];

            File file = new File(pathForAppFiles);
            if (!file.exists()){
                System.err.println("Такого файла не существует. Проверьте наличие такого файла и повторите попытку.");
                System.exit(1);
            }

            if(!file.canExecute()){
                System.err.println("Недостаточно прав. Пожалуйста, предоставьте права доступа и повторите попытку.");
                System.exit(1);
            }
        }

        Console console = null;
        try {
            IdProducer idProducer = new IdProducer(pathForAppFiles);
            LOG_MANAGER.debug("IdProducer был проинициирован УСПЕШНО.");
            StudyGroupFactory studyGroupFactory = new StudyGroupFactory(idProducer);
            LOG_MANAGER.debug("StudyGroupFactory был проинициирован УСПЕШНО.");
            IStudyGroupRepository studyGroupRepository = new TreeSetStudyGroupRepository(studyGroupFactory, pathForAppFiles);
            LOG_MANAGER.debug("IStudyGroupRepository был проинициирован УСПЕШНО.");

            ICommandsRepository commandsRepository = new HistoryRepository();
            LOG_MANAGER.debug("HstoryRepository был создан УСПЕШНО.");
            Interpretator interpretator = new Interpretator(studyGroupRepository, commandsRepository);
            LOG_MANAGER.debug("Interpretator был создан УСПЕШНО.");
            Controller controller = new Controller(interpretator, commandsRepository);
            LOG_MANAGER.debug("Controller был создан УСПЕШНО.");
            console = new Console(System.in, System.out, controller);
            LOG_MANAGER.debug("Console был создан УСПЕШНО.");
        } catch (DAOException | VerifyException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try {
            LOG_MANAGER.info("App is starting...");
            console.start();
        } catch (InputException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void checkInputPath(String[] args) {
        if (args.length > 1) {
            System.err.println(ARGUMENTS_ERROR);
            System.exit(1);
        }
    }
}
