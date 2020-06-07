package app;

import app.controller.commands.ExitCommand;
import app.controller.commands.enterScreen.LoginCommand;
import app.controller.commands.enterScreen.SignUpCommand;
import app.controller.commands.mainScreen.BuildQueryToServerCommand;
import app.controller.commands.mainScreen.LogoutCommand;
import app.controller.services.connectionService.ConnectionService;
import app.controller.services.exitingDirector.ExitingDirector;
import app.controller.services.exitingDirector.INeedExiting;
import app.controller.commands.mainScreen.CommandName;
import app.screens.ConsoleScreen;
import app.screens.EnterScreen;
import app.screens.MainScreen;
import connection.Connection;
import connection.SocketConnection;
import connection.exception.ConnectionException;
import connectionService.ConnectionWorker;
import controller.AbstractController;
import controller.ControllerBuilder;
import controller.command.Command;
import controller.components.serviceMediator.Service;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import router.Router;
import router.RouterBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public final class App {
    public static void main(String[] args) throws Throwable {
        Configuration configuration = createConfiguration();

        Console console = createConsole();
        Interpretator interpretator = createInterpretator();
        Validator validator = createValidator();
        Viewer viewer = creaeteViewer();

        ConnectionService connectionService = createConnectionService(configuration);
        ExitingDirector exitingDirector = createExitingDirector();

        AbstractController enterController = createEnterScreenController(configuration, connectionService, exitingDirector);
        AbstractController mainController = creaateMainController(configuration, connectionService, exitingDirector, console, interpretator, validator, viewer);

        ConsoleScreen enterScreen = createEnterScreen(console, viewer, enterController);
        exitingDirector.addINeedExiting(enterScreen);
        ConsoleScreen mainScreen = createMainScreen(console, viewer, mainController);
        exitingDirector.addINeedExiting(mainScreen);

        createRouter(enterScreen, mainScreen).start();
    }

    private static Configuration createConfiguration() throws ConfigurationException, IOException {
        String fileName = Paths.get("./config_client.properties").toRealPath().toString();

        Parameters parameters = new Parameters();

        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties().setFileName(fileName));

        return builder.getConfiguration();
    }

    private static Console createConsole() {
        return new Console(System.in, System.out);
    }

    private static Viewer creaeteViewer() {
        return new Viewer();
    }

    private static Interpretator createInterpretator() {
        return new Interpretator();
    }

    private static Validator createValidator() {
        return new Validator(false);
    }

    private static ConnectionService createConnectionService(Configuration configuration) throws ConnectionException {
        String address = configuration.getString("server.address");
        int port = configuration.getInt("server.port");
        int bufferSize = configuration.getInt("connection.bufferSize");

        Connection connection = new SocketConnection(address, port, bufferSize);
        ConnectionWorker connectionWorker = ConnectionWorker.createDefault(connection);

        return new ConnectionService(connectionWorker);
    }

    private static ExitingDirector createExitingDirector() {
        List<INeedExiting> iNeedExitings = new ArrayList<>();
        return new ExitingDirector(iNeedExitings);
    }

    private static AbstractController createEnterScreenController(Configuration configuration,
                                                                  ConnectionService connectionService,
                                                                  ExitingDirector exitingDirector) {
        Map<String, Class<? extends Command>> commandMap = new HashMap<>();
        commandMap.put("exit", ExitCommand.class);
        commandMap.put("login", LoginCommand.class);
        commandMap.put("signUp", SignUpCommand.class);

        Set<Service> services = new HashSet<>();
        services.add(connectionService);
        services.add(exitingDirector);

        return new ControllerBuilder(configuration, commandMap)
                        .buildServiceMediator(services)
                        .build();
    }

    private static ConsoleScreen createEnterScreen(Console console,
                                                   Viewer viewer,
                                                   AbstractController enterController) {
        return new EnterScreen(console, viewer, enterController);
    }

    private static AbstractController creaateMainController(Configuration configuration,
                                                            ConnectionService connectionService,
                                                            ExitingDirector exitingDirector,
                                                            Console console,
                                                            Interpretator interpretator,
                                                            Validator validator,
                                                            Viewer viewer) {
        Map<String, Class<? extends Command>> commandMap = new HashMap<>();
        commandMap.put("exit", ExitCommand.class);
        commandMap.put("logout", LogoutCommand.class);

        Arrays.asList(CommandName.values())
              .forEach(commandName -> commandMap.put(commandName.getName(), BuildQueryToServerCommand.class));


        Set<Service> services = new HashSet<>();
        services.add(connectionService);
        services.add(exitingDirector);
        services.add(console);
        services.add(interpretator);
        services.add(validator);
        services.add(viewer);

        return new ControllerBuilder(configuration, commandMap)
                    .buildServiceMediator(services)
                    .build();
    }

    private static ConsoleScreen createMainScreen(Console console,
                                                  Viewer viewer,
                                                  AbstractController mainController) {
        return new MainScreen(console, viewer, mainController);
    }

    private static Router createRouter(ConsoleScreen enterScreen,
                                       ConsoleScreen mainScreen) {
        return new RouterBuilder("enter")
                    .registerScreen("enter", enterScreen)
                    .registerScreen("main", mainScreen)
                    .build();
    }
}
