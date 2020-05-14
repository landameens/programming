package domain.commandsRepository;

import java.util.List;

/**
 * Interface for working with the repository where record stored.
 */
public interface ICommandsRepository {
    void add(Record commandDTO);

    List<Record> getRecords(int quantity);
}
