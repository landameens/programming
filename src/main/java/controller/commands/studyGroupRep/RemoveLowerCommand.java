package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.exception.VerifyException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.PersonDTO;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWhichLessThanThatStudyGroup;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class RemoveLowerCommand extends StudyGroupRepositoryCommand {
    public RemoveLowerCommand(String type,
                              Map<String, String> args,
                              IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = Integer.parseInt(args.get("xCoordinate"));
        coordinatesDTO.y = Integer.parseInt(args.get("yCoordinate"));

        PersonDTO personDTO = new PersonDTO();
        personDTO.name = args.get("groupAdminName");
        if (personDTO.name != null) {
            personDTO.passportID = args.get("groupAdminPassportID");
            personDTO.nationality = args.get("groupAdminNationality");
            personDTO.height = Integer.parseInt(args.get("groupAdminHeight"));
        } else personDTO = null;

        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();
        studyGroupDTO.name =  args.get("StudyGroupName");
        studyGroupDTO.coordinates = coordinatesDTO;
        studyGroupDTO.studentsCount = Integer.parseInt(args.get("studentsCount"));
        studyGroupDTO.shouldBeExpelled = Long.parseLong(args.get("shouldBeExpelled"));
        studyGroupDTO.formOfEducation = args.get("formOfEducation")  ;
        studyGroupDTO.semesterEnum = args.get("semesterEnum");
        studyGroupDTO.groupAdmin = personDTO;
        studyGroupDTO.creationDate = LocalDateTime.now();
        studyGroupDTO.id = 101L;

        try {
            StudyGroup comparedStudyGroup = StudyGroup.getStudyGroup(studyGroupDTO);
            ConcreteSet lowerStudyGroupSet = new ConcreteSetWhichLessThanThatStudyGroup(comparedStudyGroup);

            Set<StudyGroup> removableStudyGroup = studyGroupRepository.getConcreteSetOfStudyGroups(lowerStudyGroupSet);

            if (!removableStudyGroup.isEmpty()) {
                for (StudyGroup studyGroup : removableStudyGroup) {
                    studyGroupRepository.remove(studyGroup);
                }

                return getSuccessfullyResponseDTO("Группы, меньшие, чем заданная, удалены." + System.lineSeparator());
            }

            return getPreconditionFailedResponseDTO("В коллекци нет групп, меньших, чем заданная." + System.lineSeparator());
        } catch (VerifyException | StudyGroupRepositoryException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
