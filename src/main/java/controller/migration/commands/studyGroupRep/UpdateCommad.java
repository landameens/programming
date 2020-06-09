package controller.migration.commands.studyGroupRep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.services.GsonPersonSerializer;
import controller.services.GsonStudyGroupSerializer;
import domain.exception.StudyGroupRepositoryException;
import domain.exception.VerifyException;
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

public class UpdateCommad extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(UpdateCommad.class);

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(StudyGroup.class, new GsonStudyGroupSerializer())
            .registerTypeAdapter(Person.class, new GsonPersonSerializer())
            .create();

    public UpdateCommad(String type,
                        Map<String, String> args,
                        IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Executing the update command...");
        Long id = Long.parseLong(args.get("id"));
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.x = Integer.parseInt(args.get("xCoordinate"));
        coordinatesDTO.y = Integer.parseInt(args.get("yCoordinate"));

        PersonDTO personDTO = new PersonDTO();
        personDTO.name = args.get("groupAdminName");
        personDTO.passportID = args.get("groupAdminPassportID");
        personDTO.nationality = args.get("groupAdminNationality");
        personDTO.height = Integer.parseInt(args.get("groupAdminHeight"));

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
        studyGroupDTO.userId = Integer.parseInt(args.get("userId"));

        try {
            ConcreteSet concreteSet = new ConcreteSetWithSpecialField(StudyGroup.class, "id", id);
            Set<StudyGroup> groups = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);
            StudyGroup oldGroup = null;
            for (StudyGroup studyGroup : groups) {
                oldGroup = studyGroup;
            }
            StudyGroup studyGroupNew = StudyGroup.getStudyGroup(studyGroupDTO);
            if(oldGroup.getUserId() == studyGroupNew.getUserId()) {
                studyGroupRepository.update(studyGroupNew);
            } else return getBadRequestResponseDTO("Группа создана другим пользователем, вы не можете обновить ее данные.");
            LOG_MANAGER.info("The group info updating...");

            return new Response(Status.SUCCESSFULLY, gson.toJson(studyGroupNew));
        } catch (StudyGroupRepositoryException | VerifyException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к коллекции.");
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
