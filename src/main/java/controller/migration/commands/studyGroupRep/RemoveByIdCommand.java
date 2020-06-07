package controller.migration.commands.studyGroupRep;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import domain.studyGroupRepository.concreteSet.ConcreteSetWithSpecialField;
import manager.LogManager;
import response.Response;

import java.util.Map;
import java.util.Set;

public class RemoveByIdCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(RemoveByIdCommand.class);
    public RemoveByIdCommand(String type,
                             Map<String, String> args,
                             IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute(){
        LOG_MANAGER.info("Выполнение команды remove_by_id...");
        Long id = Long.parseLong(args.get("id"));
        LOG_MANAGER.debug("Поле id заполнено.");
        ConcreteSet removableStudyGroupSet = new ConcreteSetWithSpecialField(StudyGroup.class, "id", id);

        try {
            Set<StudyGroup> groupSet = studyGroupRepository.getConcreteSetOfStudyGroups(removableStudyGroupSet);

            for (StudyGroup studyGroup : groupSet) {
                if (Integer.parseInt(args.get("userId")) == studyGroup.getUserId()) {
                    studyGroupRepository.remove(studyGroup);
                } else return getBadRequestResponseDTO("Группа создана другим пользователем, вы не можете её удалить.");
            }

            if (groupSet.isEmpty()) {
                return getPreconditionFailedResponseDTO("Группы с таким id не существует." + System.lineSeparator());
            }

            LOG_MANAGER.info("Группа удалена.");
            return getSuccessfullyResponseDTO("Группа удалена." + System.lineSeparator());

        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к коллекции.");
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
