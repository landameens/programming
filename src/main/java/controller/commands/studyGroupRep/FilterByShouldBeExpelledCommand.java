package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;

import java.util.Map;
import java.util.Set;

public class FilterByShouldBeExpelledCommand extends StudyGroupRepositoryCommand {
    public FilterByShouldBeExpelledCommand(String type,
                                           Map<String, String> args,
                                           IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        Long shouldBeExpelled = Long.parseLong(args.get("should_be_expelled"));

        try {
            ConcreteSet concreteSet = new ConcreteSetWithSpecialField(StudyGroup.class, "shouldBeExpelled", shouldBeExpelled);
            Set<StudyGroup> studyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);

            if (studyGroupSet.isEmpty()){
                return getPreconditionFailedResponseDTO("Группы с таким значением поля should_be_expelled нет в коллекции." + System.lineSeparator());
            }

            StringBuilder message = new StringBuilder();
            for(StudyGroup studyGroup : studyGroupSet){
                message.append(studyGroup.toString()).append(System.lineSeparator());
            }

            return getSuccessfullyResponseDTO(message.toString());
        } catch (StudyGroupRepositoryException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }

    }
}
