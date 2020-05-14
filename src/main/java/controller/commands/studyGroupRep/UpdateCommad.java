package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.exception.VerifyException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.PersonDTO;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.time.LocalDateTime;
import java.util.Map;

public class UpdateCommad extends StudyGroupRepositoryCommand {
    public UpdateCommad(String type,
                        Map<String, String> args,
                        IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {

        Long id = Long.parseLong(args.get("id"));
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
        studyGroupDTO.id = id;
        studyGroupDTO.name = args.get("StudyGroupName");
        studyGroupDTO.coordinates = coordinatesDTO;
        studyGroupDTO.studentsCount = Integer.parseInt(args.get("studentsCount"));
        studyGroupDTO.shouldBeExpelled = Long.parseLong(args.get("shouldBeExpelled"));
        studyGroupDTO.formOfEducation = args.get("formOfEducation");
        studyGroupDTO.semesterEnum = args.get("semesterEnum");
        studyGroupDTO.groupAdmin = personDTO;
        studyGroupDTO.creationDate = LocalDateTime.now();

        try {
            StudyGroup studyGroupNew = StudyGroup.getStudyGroup(studyGroupDTO);
            studyGroupRepository.update(studyGroupNew);

            return getSuccessfullyResponseDTO("Группа обновлена." + System.lineSeparator());
        } catch (StudyGroupRepositoryException | VerifyException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
