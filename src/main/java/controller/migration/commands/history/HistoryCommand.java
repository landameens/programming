package controller.migration.commands.history;

import controller.migration.commands.Command;
import response.Response;
import domain.commandsRepository.ICommandsRepository;
import domain.commandsRepository.Record;
import manager.LogManager;

import java.util.List;
import java.util.Map;

public class HistoryCommand extends Command {
    private ICommandsRepository commandsRepository;

    private static final String BEGINNING = "===============> HISTORY <===============";
    private static final String SEPARATOR = "=========================================";

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
        answer.append(BEGINNING).append(System.lineSeparator());
        historyList.stream()
                .forEachOrdered(record -> answer.append(record).append(System.lineSeparator())
                        .append(SEPARATOR).append(System.lineSeparator()));

        return answer.toString();
    }

}
