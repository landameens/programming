package controller.migration.commands.factory;

import controller.migration.commands.Command;
import domain.exception.CreationException;

import java.util.Map;

/**
 * Interface for all factories
 */
public interface ICommandFactory {
    Command createCommand(String commandName,
                          Map<String, String> arguments) throws CreationException;
}
