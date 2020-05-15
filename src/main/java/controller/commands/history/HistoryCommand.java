package controller.commands.history;

import controller.commands.Command;
import response.Response;
import domain.commandsRepository.ICommandsRepository;
import domain.commandsRepository.Record;
import manager.LogManager;

import java.util.List;
import java.util.Map;

public class HistoryCommand extends Command {
    private ICommandsRepository commandsRepository;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(HistoryCommand.class);
    public HistoryCommand(String name,
                          Map<String, String> args,
                          ICommandsRepository commandsRepository) {
        super(name, args);
        this.commandsRepository = commandsRepository;
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды history...");
        List<Record> historyList = commandsRepository.getRecords(15);
        String answer = getAnswer(historyList);
        return getSuccessfullyResponseDTO(answer);
    }

    private String getAnswer(List<Record> historyList) {
        StringBuilder answer = new StringBuilder();
        for (Record record : historyList){
            answer.append(record.toString());
        }

        return answer.toString();
    }

}
