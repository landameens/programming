package controller.commands.factory;

import controller.commands.Command;
import domain.exception.CreationException;

import java.util.Map;

/**
 * Interface for all factories
 */
public interface ICommandFactory {
    Command createCommand(String commandName,
                          Map<String, String> arguments) throws CreationException;
}
