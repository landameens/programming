package controller.migration.commands.studyGroupRep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.services.GsonPersonSerializer;
import controller.services.GsonStudyGroupSerializer;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.Person;
import domain.studyGroup.person.PersonDTO;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;
import manager.LogManager;
import response.Response;
import response.Status;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class AddCommand extends StudyGroupRepositoryCommand {

    private static final LogManager LOG_MANAGER = LogManager.createDefault(AddCommand.class);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(StudyGroup.class, new GsonStudyGroupSerializer())
            .registerTypeAdapter(Person.class, new GsonPersonSerializer())
            .create();

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
        studyGroupDTO.name =  args.get("studyGroupName");
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
        studyGroupDTO.userId = Integer.parseInt(args.get("userId"));

        try {
            studyGroupRepository.add(studyGroupDTO);
            LOG_MANAGER.info("Group was added SUCCESSFULLY");

            ConcreteSet concreteSet = new ConcreteSetWithSpecialField(StudyGroup.class, "name", studyGroupDTO.name);

            Set<StudyGroup> studyGroups = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);

            if (studyGroups.isEmpty()) {
                return Response.createInternalError();
            }

            return new Response(Status.SUCCESSFULLY, gson.toJson(studyGroups.iterator().next()));
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Ошибка при добавлении.");
            return getBadRequestResponseDTO(e.getMessage());
        }

    }
}
