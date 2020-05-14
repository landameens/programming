package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.PersonDTO;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;
import domain.studyGroupRepository.concreteSet.MinSet;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AddIfMinCommand extends StudyGroupRepositoryCommand {
    public AddIfMinCommand(String type,
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
        }

        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();
        studyGroupDTO.name =  args.get("StudyGroupName");
        studyGroupDTO.coordinates = coordinatesDTO;
        studyGroupDTO.studentsCount = Integer.parseInt(args.get("studentsCount"));
        studyGroupDTO.shouldBeExpelled = Long.parseLong(args.get("shouldBeExpelled"));
        studyGroupDTO.formOfEducation = args.get("formOfEducation");
        studyGroupDTO.semesterEnum = args.get("semesterEnum");
        studyGroupDTO.groupAdmin = personDTO;
        studyGroupDTO.creationDate = LocalDateTime.now();

        try {
            studyGroupRepository.add(studyGroupDTO);

            ConcreteSet minSet = new MinSet();

            Set<StudyGroup> minStudyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(minSet);
            Comparator<StudyGroupDTO> studyGroupDTOComparator = new StudyGroup.StudyGroupDTOComparator();

            for (StudyGroup studyGroup : minStudyGroupSet){
                StudyGroupDTO currentStudyGroupDTO = StudyGroup.getStudyGroupDTO(studyGroup);

                if (studyGroupDTOComparator.compare(currentStudyGroupDTO, studyGroupDTO) == 0){
                    return getSuccessfullyResponseDTO("Группа добавлена." + System.lineSeparator());
                }

                ConcreteSet concreteSet = new ConcreteSetWithSpecialField(StudyGroup.class, "studentsCount", studyGroupDTO.studentsCount);
                Set<StudyGroup> studyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);

                Iterator<StudyGroup> iterator = studyGroupSet.iterator();
                studyGroupRepository.remove(iterator.next());
            }

            return getPreconditionFailedResponseDTO("Значение добавляемой группы не является наименьшим.");

        } catch (StudyGroupRepositoryException e) {
            return getBadRequestResponseDTO(e.getMessage());
        }
    }

}