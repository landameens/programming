package controller.serverAdapter;

import connection.SocketConnection;
import connection.exception.ConnectionException;
import connectionService.ConnectionService;
import connectionService.ConnectionWorker;
import controller.components.serviceMediator.Service;
import controller.serverAdapter.exception.*;
import manager.LogManager;
import query.Query;
import response.Response;
import response.Status;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ServerAdapter encapsulates common logic of sending queries to the server.
 * <br></br>
 * Provide commandName and arguments and ServerAdapter will return a response. Also
 * it will analyzes the status of a response and throw one of the following exceptions in bad cases:
 * <br></br>
 * {@link AccessTokenExpiredException},
 * <br></br>
 * {@link WrongSignatureOfAccessTokenException},
 * <br></br>
 * {@link ServerInternalErrorException},
 * <br></br>
 * {@link ServerUnavailableException}
 *
 * <br></br>
 * <br></br>
 */
public final class ServerAdapter implements Service {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ServerAdapter.class);

    private ReentrantLock reentrantLock;

    private String login, password;


    public ServerAdapter() {
        reentrantLock = new ReentrantLock();
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public synchronized Response send(Query query) throws ServerAdapterException {
        LOG_MANAGER.debug(String.format("Query %1$s %2$s", query.getCommandName(), query.getArguments()));

        ConnectionService connectionService;
        try {
            SocketConnection socketConnection = new SocketConnection("localhost", 8080, 128);
            ConnectionWorker connectionWorker = ConnectionWorker.createDefault(socketConnection);
            connectionService = new ConnectionService(connectionWorker);
        } catch (ConnectionException e) {
            throw new ServerUnavailableException();
        }

        Response response;
        try {
            response = connectionService.sendAndGetResponse(query);
            connectionService.close();
        } catch (ConnectionException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new ServerUnavailableException();
        }

        if (response.getStatus().equals(Status.BAD_REQUEST)) {
            LOG_MANAGER.errorThrowable("Server answered: bag request! " + response.getAnswer());
            throw new WrongQueryException();
        }

        if (response.getStatus().equals(Status.INTERNAL_ERROR)) {
            LOG_MANAGER.errorThrowable("Server answered: internal error! " + response.getAnswer());
            throw new ServerInternalErrorException();
        }

        if (response.getStatus().equals(Status.FORBIDDEN)) {
            LOG_MANAGER.errorThrowable("Server answered: FORBIDDEN. " + response.getAnswer());

            if (response.getAnswer().equals("JWT signature is wrong.")) {
                throw new WrongSignatureOfAccessTokenException();
            }

            if (response.getAnswer().equals("JWT has already expired.")) {
                throw new AccessTokenExpiredException();
            }
        }

        LOG_MANAGER.debug(response.toString());
        return response;
    }


    public Response send(String commandName,
                         Map<String, String> arguments) throws ServerAdapterException {
        Query query;
        try {
            reentrantLock.lock();
            arguments.put("login", login);
            arguments.put("password", password);
            query = new Query(commandName, arguments);
        } finally {
            reentrantLock.unlock();
        }

        return send(query);
    }
}
