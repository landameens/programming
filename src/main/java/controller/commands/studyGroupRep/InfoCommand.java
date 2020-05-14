package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.util.Map;

public class InfoCommand extends StudyGroupRepositoryCommand {

    public InfoCommand(String type,
                       Map<String, String> args,
                       IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        return getSuccessfullyResponseDTO(studyGroupRepository.getInfo().toString());
    }
}
