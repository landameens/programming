package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;

import java.util.Map;
import java.util.Set;

public class RemoveByIdCommand extends StudyGroupRepositoryCommand {

    public RemoveByIdCommand(String type,
                             Map<String, String> args,
                             IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute(){
        Long id = Long.parseLong(args.get("id"));
        ConcreteSet removableStudyGroupSet = new ConcreteSetWithSpecialField(StudyGroup.class, "id", id);

        try {
            Set<StudyGroup> groupSet = studyGroupRepository.getConcreteSetOfStudyGroups(removableStudyGroupSet);

            for (StudyGroup studyGroup : groupSet) {
                studyGroupRepository.remove(studyGroup);
            }

            if (groupSet.isEmpty()) {
                return getPreconditionFailedResponseDTO("Группы с таким id не существует." + System.lineSeparator());
            }

            return getSuccessfullyResponseDTO("Группа удалена." + System.lineSeparator());

        } catch (StudyGroupRepositoryException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
