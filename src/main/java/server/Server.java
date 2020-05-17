package server;

import connection.exception.ConnectionException;
import connectionWorker.ConnectionWorker;
import controller.Controller;
import controller.Interpretator;
import domain.commandsRepository.HistoryRepository;
import domain.commandsRepository.ICommandsRepository;
import domain.exception.CreationException;
import domain.exception.VerifyException;
import domain.studyGroupFactory.StudyGroupFactory;
import domain.studyGroupFactory.idProducer.IdProducer;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.TreeSetStudyGroupRepository;
import manager.LogManager;
import message.EntityType;
import message.Message;
import message.exception.WrongTypeException;
import query.Query;
import response.Response;
import response.ResponseDTO;
import response.Status;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;
import storage.exception.DAOException;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private static final String ARGUMENTS_ERROR = "Введено слишком много аргументов, повторите ввод директории," +
            " куда будет сохраняться коллекция и сопутсвующие файлы";
    private final int connectionBufferSize;
    private final int port;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(Server.class);

    public Server(int connectionBufferSize, int port) {
        this.connectionBufferSize = connectionBufferSize;
        this.port = port;
    }

    public void start(String[] args) {
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
            System.exit(1);;
        }

        ICommandsRepository commandsRepository = new HistoryRepository();
        LOG_MANAGER.debug("HstoryRepository was created SUCCESSFUL.");
        Interpretator interpretator = new Interpretator(studyGroupRepository, commandsRepository);
        LOG_MANAGER.debug("Interpretator was created SUCCESFULL.");
        Controller controller = new Controller(interpretator, commandsRepository);
        LOG_MANAGER.debug("Controller was created SUCCESSFUL.");

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

    private static void checkInputPath(String[] args) {
        if (args.length > 1) {
            System.err.println(ARGUMENTS_ERROR);
            System.exit(1);
        }
    }
}
