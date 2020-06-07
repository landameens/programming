package app.controller.services.connectionService;

import connection.exception.ConnectionException;
import connection.exception.NotYetConnectedException;
import connectionService.ConnectionWorker;
import controller.command.exception.CommandExecutionException;
import controller.components.serviceMediator.Service;
import manager.LogManager;
import message.EntityType;
import message.Message;
import query.Query;
import query.QueryDTO;
import response.Response;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;

public final class ConnectionService implements Service {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ConnectionService.class);

    private final ConnectionWorker connectionWorker;


    public ConnectionService(ConnectionWorker connectionWorker) {
        this.connectionWorker = connectionWorker;
    }


    /**
     * Prefer to use {@link ConnectionService#send(Query) send(query)}.
     */
    public void send(Message message) throws ConnectionException, SerializationException {
        connectionWorker.send(message);
    }

    /**
     * Prefer to use this method then you need to send a query and after that wait for response.
     */
    public Response send(Query query) throws CommandExecutionException {
        QueryDTO queryDTO = Query.dtoOf(query);
        try {
            if (!isConnected()) {
                connect();
            }

            send(new Message(EntityType.QUERY, queryDTO));
        } catch (NotYetConnectedException e) {
            LOG_MANAGER.errorThrowable("Not yet connected.", e);
            throw new CommandExecutionException(e);
        } catch (ConnectionException e) {
            LOG_MANAGER.errorThrowable("Cannot send queryDTO. THe problem in connection with server", e);
            throw new CommandExecutionException(e);
        }

        try {
            Message message = read();
            return message.getResponse();
        } catch (NotYetConnectedException e) {
            LOG_MANAGER.errorThrowable("Not yet connected.", e);
            throw new CommandExecutionException(e);
        } catch (ConnectionException e) {
            LOG_MANAGER.errorThrowable("Problem with connection to the server. Probably lost.", e);
            throw new CommandExecutionException(e);
        }
    }

    /**
     * Prefer to use {@link ConnectionService#send(Query) send(query)}.
     */
    public Message read() throws ConnectionException, DeserializationException {
        return connectionWorker.read();
    }

    public void connect() throws ConnectionException {
        connectionWorker.connect();
    }

    public boolean isConnected() {
        return connectionWorker.isConnected();
    }
}
