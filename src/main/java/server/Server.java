package server;

import connection.exception.ConnectionException;
import connectionWorker.ConnectionWorker;
import controller.Controller;
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

                    Response response = controller.handleQuery(query);

                    Message messageToSend = new Message(EntityType.RESPONSE, Response.getResponseDTO(response));
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
            //todo !?
            e.printStackTrace();
        }

    }
}
