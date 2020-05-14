package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;

import java.util.Map;
import java.util.Set;

public class ShowCommand extends StudyGroupRepositoryCommand {

    public ShowCommand(String type,
                       Map<String, String> args,
                       IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        try {
            ConcreteSet allSet = new AllSet();
            Set<StudyGroup> studyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(allSet);

            return getSuccessfullyResponseDTO(getMessage(studyGroupSet));
        } catch (StudyGroupRepositoryException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }
    }

    private String getMessage(Set<StudyGroup> studyGroupSet){

        if(!studyGroupSet.isEmpty()) {
            StringBuilder allStudyGroups = new StringBuilder();

            for (StudyGroup studyGroup : studyGroupSet){
                allStudyGroups.append(studyGroup.toString()).append(System.lineSeparator()).append(System.lineSeparator());
            }

            return allStudyGroups.toString();
        }

        return "Коллекция пуста.".concat(System.lineSeparator());
    }
}
