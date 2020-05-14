package manager.loggerStrategy;

import manager.visualizer.IVisualizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jStrategy implements ILoggerStrategy {
    private final Logger logger;
    private final IVisualizer visualizer;


    public Log4jStrategy(String loggerName,
                          IVisualizer visualizer) {
        this.logger = LogManager.getLogger(loggerName);
        this.visualizer = visualizer;
    }


    @Override
    public void logError(String message) {
        logger.error("{}", ()-> visualizer.makeErrorColor(message));
    }

    @Override
    public void logFatal(String message) {
        logger.fatal("{}", ()-> visualizer.makeFatalColor(message));
    }

    @Override
    public void logInfo(String message) {
        logger.info("{}", ()-> visualizer.makeInfoColor(message));
    }

    @Override
    public void logDebug(String message) {
        logger.debug("{}", ()-> visualizer.makeDebugColor(message));
    }

    @Override
    public void logWarn(String message) {
        logger.warn("{}", ()-> visualizer.makeWarnColor(message));
    }
}
