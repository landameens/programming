package server;

import connection.exception.ConnectionException;
import connectionService.ConnectionService;
import manager.LogManager;
import middleware.Middleware;
import middleware.MiddlewareException;
import query.Query;
import response.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveAction;

public final class ReadAction extends RecursiveAction {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ReadAction.class);


    private ConnectionService connectionService;
    private Middleware rootMiddleware;
    private ExecutorService executorService;


    public ReadAction(ConnectionService connectionService,
                      Middleware rootMiddleware,
                      ExecutorService executorService) {
        this.connectionService = connectionService;
        this.rootMiddleware = rootMiddleware;
        this.executorService = executorService;
    }


    @Override
    protected void compute() {
        Query query;
        try {
            query = connectionService.readQuery();
        } catch (ConnectionException e) {
            LOG_MANAGER.errorThrowable(e);
            return;
        }

        new Thread(() -> {
            try {
                Response response = rootMiddleware.handle(query);
                executorService.execute(() -> {
                    try {
                        connectionService.send(response);
                    } catch (ConnectionException e) {
                        LOG_MANAGER.errorThrowable(e);
                    }
                });
            } catch (MiddlewareException e) {
                LOG_MANAGER.errorThrowable(e);
                executorService.execute(() -> {
                    try {
                        connectionService.send(Response.createInternalError());
                    } catch (ConnectionException ex) {
                        LOG_MANAGER.errorThrowable(ex);
                    }
                });
            }
        }).start();
    }
}
