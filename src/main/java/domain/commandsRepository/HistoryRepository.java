package domain.commandsRepository;

import java.util.ArrayList;
import java.util.List;

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
        for (Record record : historyList) {
            if(historyList.size() <= quantity){
                return historyList;
            }

            historyList.remove(record);
        }

        return null;
    }
}
