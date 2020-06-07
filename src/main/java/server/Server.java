package server;

import connection.SocketChannelConnection;
import connectionService.ConnectionService;
import connectionService.ConnectionWorker;
import manager.LogManager;
import middleware.Middleware;
import router.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class Server {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(Server.class);

    private final int connectionBufferSize;
    private final int port;
    private final Middleware rootMiddleware;
    private final ExecutorService executorService;
    private final Router router;

    public Server(int connectionBufferSize,
                  int port,
                  Middleware rootMiddleware,
                  ExecutorService executorService,
                  Router router) {
        this.connectionBufferSize = connectionBufferSize;
        this.port = port;
        this.rootMiddleware = rootMiddleware;
        this.executorService = executorService;
        this.router = router;
    }

    public void start() {
        new Thread(router::start).start();

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            
            while (true){
                SocketChannel socketChannel = serverSocketChannel.accept();
                LOG_MANAGER.info("Connection to the server was SUCCESSFUL");
                SocketChannelConnection socketChannelConnection = new SocketChannelConnection(socketChannel, connectionBufferSize);
                ConnectionWorker connectionWorker = ConnectionWorker.createDefault(socketChannelConnection);
                ConnectionService connectionService = new ConnectionService(connectionWorker);

                ForkJoinPool.commonPool().execute(new ReadAction(connectionService,
                                                                rootMiddleware,
                                                                executorService));
            }
        } catch (IOException e) {
            LOG_MANAGER.fatalThrowable("What the happend?", e);
            System.exit(1);
        }
    }
}
