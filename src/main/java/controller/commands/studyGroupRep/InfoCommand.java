package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.studyGroupRepository.IStudyGroupRepository;
import manager.LogManager;

import java.util.Map;

public class InfoCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(InfoCommand.class);
    public InfoCommand(String type,
                       Map<String, String> args,
                       IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды info...");
        return getSuccessfullyResponseDTO(studyGroupRepository.getInfo().toString());
    }
}
