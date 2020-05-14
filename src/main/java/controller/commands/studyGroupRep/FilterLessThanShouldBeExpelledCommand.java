package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;

import java.util.Map;
import java.util.Set;

public class FilterLessThanShouldBeExpelledCommand extends StudyGroupRepositoryCommand {
    public FilterLessThanShouldBeExpelledCommand(String type,
                                                 Map<String, String> args,
                                                 IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        Long shouldBeExpelled = Long.parseLong(args.get("should_be_expelled"));

        try {
            ConcreteSet allSet = new AllSet();
            Set<StudyGroup> allStudyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(allSet);

            StringBuilder message = new StringBuilder();
            for (StudyGroup studyGroup : allStudyGroupSet) {
                if(studyGroup.getShouldBeExpelled() - shouldBeExpelled < 0 ){
                    message.append(studyGroup.toString()).append(System.lineSeparator()).append(System.lineSeparator());
                }
            }

            if(message.length() == 0){
                return getPreconditionFailedResponseDTO("Групп с мешьшим значением поля shold_be_expelled в коллекции нет." + System.lineSeparator());
            }

            return getSuccessfullyResponseDTO(message.toString());
        } catch (StudyGroupRepositoryException e) {
            e.printStackTrace();
        }

        return null;
    }
}
