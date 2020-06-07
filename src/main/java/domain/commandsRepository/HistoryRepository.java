package domain.commandsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * These class are realize ICommandRepository interface for working with ArraList.
 */
public class HistoryRepository implements ICommandsRepository {
    private List<Record> historyList;

    public HistoryRepository() {
        this.historyList = new ArrayList<>();
    }

    @Override
    public void add(Record commandDTO) {
        historyList.add(commandDTO);
    }

    @Override
    public List<Record> getRecords(int quantity) {
        if (historyList.size() <= 15){
            return historyList;
        } else {
            return historyList.stream().skip((long) historyList.size() - 15L).collect(Collectors.toList());
        }
    }
}
