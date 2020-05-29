package controller.commands.studyGroupRep;

import response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.PersonDTO;
import domain.studyGroupRepository.IStudyGroupRepository;
import manager.LogManager;

import java.time.LocalDateTime;
import java.util.Map;

public class AddCommand extends StudyGroupRepositoryCommand {

    private static final LogManager LOG_MANAGER = LogManager.createDefault(AddCommand.class);


    public AddCommand(String type,
                      Map<String, String> args,
                      IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Executing the add command...");
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = Integer.parseInt(args.get("xCoordinate"));
        coordinatesDTO.y = Integer.parseInt(args.get("yCoordinate"));

        PersonDTO personDTO = new PersonDTO();
        personDTO.name = args.get("groupAdminName");
        personDTO.passportID = args.get("groupAdminPassportID");
        personDTO.nationality = args.get("groupAdminNationality");
        personDTO.height = Integer.parseInt(args.get("groupAdminHeight"));

        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();
        studyGroupDTO.name =  args.get("StudyGroupName");
        studyGroupDTO.coordinates = coordinatesDTO;
        studyGroupDTO.studentsCount = Integer.parseInt(args.get("studentsCount"));
        if (args.get("shouldBeExpelled") != null) {
            studyGroupDTO.shouldBeExpelled = Long.parseLong(args.get("shouldBeExpelled"));
        } else {
            studyGroupDTO.shouldBeExpelled = null;
        }
        studyGroupDTO.formOfEducation = args.get("formOfEducation")  ;
        studyGroupDTO.semesterEnum = args.get("semesterEnum");
        studyGroupDTO.groupAdmin = personDTO;
        studyGroupDTO.creationDate = LocalDateTime.now();

        try {
            studyGroupRepository.add(studyGroupDTO);
            LOG_MANAGER.info("Group was added SUCCESSFULLY");

            return getSuccessfullyResponseDTO("Группа добавлена" + System.lineSeparator());
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Ошибка при добавлении.");
            return getBadRequestResponseDTO(e.getMessage());
        }

    }
}
