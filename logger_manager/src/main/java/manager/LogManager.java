package manager;

import manager.loggerStrategy.ILoggerStrategy;
import manager.loggerStrategy.Log4jStrategy;
import manager.visualizer.IVisualizer;
import manager.visualizer.Log4jVisualizer;

/**
 * This class is used for all logging needs
 */
public class LogManager {
    private final ILoggerStrategy loggerStrategy;
    private final IVisualizer visualizer;


    private LogManager(ILoggerStrategy loggerStrategy,
                       IVisualizer visualizer) {
        this.loggerStrategy = loggerStrategy;
        this.visualizer = visualizer;
    }

    /**
     * Create a loggerAdapter with default Log4j strategy
     */
    public static LogManager createDefault(Class<?> clazz) {
        IVisualizer visualizer = new Log4jVisualizer();
        return new LogManager(new Log4jStrategy(clazz.getSimpleName(), visualizer),
                visualizer);
    }


    public void errorThrowable(String simpleMessage, Throwable throwable) {
        loggerStrategy.logError(visualizer.getExceptionMessage(simpleMessage, throwable));
    }

    public void errorThrowable(Throwable throwable) {
        loggerStrategy.logError(visualizer.getExceptionMessage(throwable));
    }

    public void errorThrowable(String simpleMessage) {
        loggerStrategy.logError(visualizer.getExceptionMessage(simpleMessage));
    }

    public void fatalThrowable(String simpleMessage, Throwable throwable) {
        loggerStrategy.logFatal(visualizer.getExceptionMessage(simpleMessage, throwable));
    }

    public void fatalThrowable(Throwable throwable) {
        loggerStrategy.logFatal(visualizer.getExceptionMessage(throwable));
    }

    public void fatalThrowable(String simpleMessage) {
        loggerStrategy.logFatal(visualizer.getExceptionMessage(simpleMessage));
    }

    public void debug(String message) {
        loggerStrategy.logDebug(message);
    }

    public void info(String message) {
        loggerStrategy.logInfo(message);
    }

    public void warn(String message) {
        loggerStrategy.logWarn(message);
    }

    public void error(String message) {
        loggerStrategy.logError(message);
    }

    public void fatal(String message) {
        loggerStrategy.logFatal(message);
    }
}
