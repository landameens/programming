package guiApp;

import controller.AbstractController;
import controller.ControllerBuilder;
import controller.command.Command;
import controller.components.serviceMediator.Service;
import controller.loginController.LoginCommand;
import controller.loginController.SignUpCommand;
import controller.serverAdapter.ServerAdapter;
import controller.validators.LoginValidator;
import controller.validators.PasswordEqualityValidator;
import controller.validators.PasswordValidator;
import controller.validators.Validator;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import domain.studyGroupRepository.StudyGroupCollectionUpdater;
import domain.studyGroupRepository.StudyGroupRepositorySubscriber;
import domain.user.ServerUserDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import manager.LogManager;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import router.Router;
import router.RouterBuilder;
import view.enter.SignInController;
import view.enter.SignUpController;
import view.main.MainController;
import view.sceneAdapter.SceneAdapter;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class App extends Application {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Yofik Storage");

        Configuration configuration = provideConfiguration();
        ServerAdapter serverAdapter = provideServerAdapter(configuration);
        Validator validator = provideValidator(configuration);
        AbstractController loginBusinessController = createLoginBusinessController(configuration, serverAdapter);
        AbstractController mainBusinessController = createMainBusinessController(configuration);

        ServerStudyGroupDAO serverStudyGroupDAO = provideServerStudyGroupDAO(serverAdapter);
        ServerUserDAO serverUserDAO = provideUserDAO(serverAdapter);

        SignInController signInController = new SignInController(loginBusinessController, serverAdapter, serverUserDAO);
        SignUpController signUpController = new SignUpController(loginBusinessController,
                validator,
                serverUserDAO,
                serverAdapter);

        MainController mainController = new MainController(mainBusinessController, serverStudyGroupDAO, serverUserDAO);

        List<StudyGroupRepositorySubscriber> subscribers = new ArrayList<>();
        subscribers.add(mainController);

        StudyGroupCollectionUpdater studyGroupCollectionUpdater = new StudyGroupCollectionUpdater(serverStudyGroupDAO, subscribers);
        mainController.setStudyGroupCollectionUpdater(studyGroupCollectionUpdater);

        SceneAdapter signInAdapter = createSignInAdapter(primaryStage, signInController);
        SceneAdapter signUpAdapter = createSignUpAdapter(primaryStage, signUpController);
        SceneAdapter mainAdapter = createMainAdapter(primaryStage, mainController);

        RouterBuilder routerBuilder = new RouterBuilder("signIn")
                .registerScreen("signIn", signInAdapter)
                .registerScreen("signUp", signUpAdapter)
                .registerScreen("main", mainAdapter);

        Router router = routerBuilder.build();

        router.start();
        primaryStage.show();
    }

    private SceneAdapter createSignInAdapter(Stage primaryStage,
                                             SignInController signInController) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("localizedStrings.SignInScreen", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader();

        loader.setController(signInController);

        URL url = getClass().getClassLoader().getResource("markup/signIn.fxml");
        loader.setLocation(url);
        loader.setResources(resourceBundle);

        return new SceneAdapter(primaryStage, new Scene(loader.load()), signInController);
    }

    private SceneAdapter createSignUpAdapter(Stage primaryStage,
                                             SignUpController signUpController) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setController(signUpController);

        URL url = getClass().getClassLoader().getResource("markup/signUp.fxml");
        loader.setLocation(url);

        return new SceneAdapter(primaryStage, new Scene(loader.load()), signUpController);
    }

    private SceneAdapter createMainAdapter(Stage primaryStage,
                                           MainController mainController) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setController(mainController);

        URL url = getClass().getClassLoader().getResource("markup/main.fxml");
        loader.setLocation(url);

        AnchorPane parent = loader.load();
        return new SceneAdapter(primaryStage, new Scene(parent), mainController);
    }

    private AbstractController createLoginBusinessController(Configuration configuration,
                                                             ServerAdapter serverAdapter) {
        Map<String, Class<? extends Command>> commandMap = new HashMap<>();
        commandMap.put("login", LoginCommand.class);
        commandMap.put("signUp", SignUpCommand.class);

        Set<Service> services = new HashSet<>();
        services.add(serverAdapter);

        return new ControllerBuilder(configuration, commandMap)
                .buildServiceMediator(services)
                .build();
    }

    private AbstractController createMainBusinessController(Configuration configuration) {
        return new ControllerBuilder(configuration, new HashMap<>())
                .build();
    }

    private static final String CONFIG_PATH = "./config_client.properties";

    private static final String SERVER_ADDRESS_KEY         = "server.address";
    private static final String SERVER_PORT_KEY            = "server.port";
    private static final String CONNECTION_BUFFER_SIZE_KEY = "connection.bufferSize";

    private ServerStudyGroupDAO provideServerStudyGroupDAO(ServerAdapter serverAdapter) {
        return new ServerStudyGroupDAO(serverAdapter);
    }

    private ServerUserDAO provideUserDAO(ServerAdapter serverAdapter) {
        return new ServerUserDAO(serverAdapter);
    }

    private ServerAdapter provideServerAdapter(Configuration configuration) {
        return new ServerAdapter();
    }

    private Configuration provideConfiguration() throws org.apache.commons.configuration2.ex.ConfigurationException {
        LOG_MANAGER.debug("For Apache Configuration pathToFile: " + CONFIG_PATH);

        String fileName;
        try {
            fileName = Paths.get(CONFIG_PATH).toRealPath().toString();
        } catch (IOException e) {
            LOG_MANAGER.fatalThrowable("Cannot resolve given path for configuration", e);
            throw new ConfigurationException(e);
        }

        org.apache.commons.configuration2.builder.fluent.Parameters parameters = new org.apache.commons.configuration2.builder.fluent.Parameters();

        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties().setFileName(fileName));

        Configuration configuration = builder.getConfiguration();
        LOG_MANAGER.debug("Provided Apache Configuration: FileBasedConfiguration.");
        return configuration;
    }

    private Validator provideValidator(Configuration configuration) {
        Validator validator = new LoginValidator(configuration);
        validator.linkWith(new PasswordValidator(configuration))
                .linkWith(new PasswordEqualityValidator());

        LOG_MANAGER.debug("Provided Validator: Validator");
        return validator;
    }
}
