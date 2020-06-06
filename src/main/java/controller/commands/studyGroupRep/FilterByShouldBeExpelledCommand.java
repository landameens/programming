package controller.commands.studyGroupRep;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;
import manager.LogManager;
import response.Response;

import java.util.Map;
import java.util.Set;

public class FilterByShouldBeExpelledCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(FilterByShouldBeExpelledCommand.class);
    public FilterByShouldBeExpelledCommand(String type,
                                           Map<String, String> args,
                                           IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды filter_by_should_be_expelled...");
        Long shouldBeExpelled = Long.parseLong(args.get("should_be_expelled"));
        LOG_MANAGER.debug("Поле shouldBeExpelled заполнено.");

        try {
            ConcreteSet concreteSet = new ConcreteSetWithSpecialField(StudyGroup.class, "shouldBeExpelled", shouldBeExpelled);
            Set<StudyGroup> studyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);

            if (studyGroupSet.isEmpty()){
                return getPreconditionFailedResponseDTO("Группы с таким значением поля should_be_expelled нет в коллекции." + System.lineSeparator());
            }

            StringBuilder message = new StringBuilder();
            for(StudyGroup studyGroup : studyGroupSet){
                message.append(studyGroup.toString()).append(System.lineSeparator()).append(System.lineSeparator());
            }

            LOG_MANAGER.debug("Группы с таким значением поля shouldBeExpelled выведены.");
            return getSuccessfullyResponseDTO(message.toString());
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("произошла ошибка при обращении к коллекции.");
            return getBadRequestResponseDTO(e.getMessage());
        }

    }
}
