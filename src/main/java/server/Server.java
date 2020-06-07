package server;

import app.Exceptions.InputException;
import connection.exception.ConnectionException;
import connectionWorker.ConnectionWorker;
import controller.migration.Controller;
import domain.exception.CreationException;
import manager.LogManager;
import message.EntityType;
import message.Message;
import message.exception.WrongTypeException;
import query.Query;
import response.Response;
import response.Status;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private final int connectionBufferSize;
    private final int port;
    private final Controller controller;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(Server.class);

    public Server(int connectionBufferSize, int port, Controller controller) {
        this.connectionBufferSize = connectionBufferSize;
        this.port = port;
        this.controller = controller;
    }

    public void start() {
        new Thread(() -> {
            Console console = new Console(System.in, System.out, true);
            LOG_MANAGER.debug("Console was created SUCCESSFUL.");
            try {
                LOG_MANAGER.info("Console is starting...");
                console.start();
            } catch (InputException e) {
                LOG_MANAGER.fatalThrowable("Internal error", e);
                System.exit(1);
            }
        }).start();
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            
            while (true){
                SocketChannel socketChannel = serverSocketChannel.accept();
                LOG_MANAGER.info("Connection to the server was SUCCESSFUL");
                connection.SocketChannelConnection socketChannelConnection = new connection.SocketChannelConnection(socketChannel, connectionBufferSize);
                connectionWorker.ConnectionWorker connectionWorker = ConnectionWorker.createDefault(socketChannelConnection);

                try {
                    Message receivedMessage = connectionWorker.read();
                    Query query = receivedMessage.getCommandQuery();
                    LOG_MANAGER.info("The request is received " + query.toString());

                    LOG_MANAGER.info("Query execution...");
                    Response response = controller.handleQuery(query);

                    Message messageToSend = new Message(EntityType.RESPONSE, Response.getResponseDTO(response));
                    LOG_MANAGER.info("Response sent " + messageToSend.getResponse().toString());
                    try {
                        connectionWorker.send(messageToSend);
                    } catch (SerializationException e) {
                        LOG_MANAGER.errorThrowable("Can't send a responce ", e);
                    }
                } catch (ConnectionException | DeserializationException | WrongTypeException | CreationException e) {
                    Response internalError = new Response(Status.INTERNAL_ERROR, "Error ...");
                    try {
                        connectionWorker.send(new Message(EntityType.RESPONSE, Response.getResponseDTO(internalError)));
                    } catch (ConnectionException | SerializationException connectionException) {
                        LOG_MANAGER.fatalThrowable("Good bye!", connectionException);
                        System.exit(1);
                    }
                }

            }
        } catch (IOException e) {
            System.exit(1);
            LOG_MANAGER.fatalThrowable("What the happend?", e);
        }

    }
}
