package app.server;

import app.Console;
import app.controller.commands.ExitCommand;
import app.controller.services.exitingDirector.ExitingDirector;
import app.controller.services.exitingDirector.INeedExiting;
import controller.ControllerBuilder;
import controller.command.Command;
import controller.components.serviceMediator.Service;
import controller.loginController.LoginCommand;
import controller.loginController.SignUpCommand;
import controller.migration.Controller;
import controller.migration.Interpretator;
import controller.services.MD2PasswordHashService;
import domain.commandsRepository.HistoryRepository;
import domain.commandsRepository.ICommandsRepository;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.DBStudyGroupRepository;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.user.User;
import domain.userRepository.DBUserRepository;
import domain.userRepository.UserRepository;
import manager.LogManager;
import middleware.Middleware;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import router.Router;
import router.RouterBuilder;
import router.screen.Screen;
import server.Server;
import server.middleware.CommandNameValidator;
import server.middleware.GeneralAccessValidator;
import server.view.Viewer;
import server.view.screen.MainScreen;
import storage.dao.DAO;
import storage.postgresDAO.PostgresDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App {
    private static final String ARGUMENTS_ERROR = "Введено слишком много аргументов, повторите ввод директории," +
            " куда будет сохраняться коллекция и сопутсвующие файлы";
    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);

    public static void main(String[] args) throws Throwable {
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

        File directoryForStoringScripts = new File(pathForAppFiles + "/script");
        if (!directoryForStoringScripts.exists()){
            directoryForStoringScripts.mkdir();
        }

        Configuration configuration = createConfiguration();

        IStudyGroupRepository studyGroupRepository = createStudyGroupRepository(createStudyGroupDAO());
        UserRepository userRepository = creaeteUserRepository(creaateUserDAO());

        Interpretator interpretator = new Interpretator(new DBStudyGroupRepository(createStudyGroupDAO()),
                new HistoryRepository());

        Middleware migrationController = createMigrationController(studyGroupRepository);
        Middleware loginController = createLoginController(configuration, userRepository);
        Middleware rootMiddleware = createRootMiddleware(migrationController,
                loginController,
                userRepository,
                interpretator);
        
        ExitingDirector exitingDirector = createExitingDirector();
        Console console = createConsole();
        Viewer viewer = createViewer();
        Screen mainScreen = creaeteMainScreen(configuration, exitingDirector, console, viewer);
        Router router = createRouter(mainScreen);
        ExecutorService executorService = Executors.newCachedThreadPool();

        new Server(configuration.getInt("bufferSize"),
                configuration.getInt("port"),
                rootMiddleware,
                executorService,
                router).start();
    }

    private static void checkInputPath(String[] args) {
        if (args.length > 1) {
            System.err.println(ARGUMENTS_ERROR);
            System.exit(1);
        }
    }

    private static Configuration createConfiguration() throws ConfigurationException, IOException {
        String fileName = Paths.get("./config_server.properties").toRealPath().toString();

        Parameters parameters = new Parameters();

        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties().setFileName(fileName));

        return builder.getConfiguration();
    }

    private static DAO<User> creaateUserDAO() {
        return new PostgresDAO<>("From User");
    }

    private static UserRepository creaeteUserRepository(DAO<User> userDAO) {
        return new DBUserRepository(userDAO);
    }

    private static DAO<StudyGroup> createStudyGroupDAO() {
        return new PostgresDAO<>("From StudyGroup");
    }

    private static IStudyGroupRepository createStudyGroupRepository(DAO<StudyGroup> studyGroupDAO) {
        return new DBStudyGroupRepository(studyGroupDAO);
    }

    private static Middleware createRootMiddleware(Middleware migrationController,
                                                   Middleware loginController,
                                                   UserRepository userRepository,
                                                   Interpretator interpretator) {
        Middleware generalAccessValidator = new GeneralAccessValidator(userRepository);
        generalAccessValidator.addLeave("old", migrationController);

        Middleware commandNameValidator = new CommandNameValidator(interpretator);
        commandNameValidator.addLeave("old", generalAccessValidator);
        commandNameValidator.addLeave("login", loginController);

        return commandNameValidator;
    }

    private static Middleware createMigrationController(IStudyGroupRepository studyGroupRepository) {
        ICommandsRepository commandsRepository = new HistoryRepository();
        Interpretator interpretator = new Interpretator(studyGroupRepository, commandsRepository);
        return new Controller(interpretator, commandsRepository);
    }

    private static Middleware createLoginController(Configuration configuration,
                                                    UserRepository userRepository) {
        Map<String, Class<? extends Command>> commandMap = new HashMap<>();
        commandMap.put("login", LoginCommand.class);
        commandMap.put("signUp", SignUpCommand.class);

        Set<Service> services = new HashSet<>();
        services.add(userRepository);
        services.add(new MD2PasswordHashService());

        return new ControllerBuilder(configuration, commandMap)
                        .buildServiceMediator(services)
                        .build();
    }

    private static Console createConsole() {
        return new Console(System.in, System.out);
    }

    private static Viewer createViewer() {
        return new Viewer();
    }

    private static ExitingDirector createExitingDirector() {
        List<INeedExiting> needExitings = new ArrayList<>();
        return new ExitingDirector(needExitings);
    }

    private static Screen creaeteMainScreen(Configuration configuration,
                                            ExitingDirector exitingDirector,
                                            Console console,
                                            Viewer viewer) {
        Map<String, Class<? extends Command>> commands = new HashMap<>();
        commands.put("exit", ExitCommand.class);

        Set<Service> services = new HashSet<>();
        services.add(exitingDirector);
        services.add(console);

        controller.Controller controller = new ControllerBuilder(configuration, commands)
                .buildServiceMediator(services)
                .build();

        MainScreen mainScreen = new MainScreen(console, viewer, controller);
        exitingDirector.addINeedExiting(mainScreen);

        return mainScreen;
    }

    private static Router createRouter(Screen mainScreen) {
        return new RouterBuilder("main")
                        .registerScreen("main", mainScreen)
                        .build();
    }
}
