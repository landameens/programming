package controller.commands.studyGroupRep;

import response.Response;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;

import java.util.Map;
import java.util.Set;

public class ClearCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ClearCommand.class);
    public ClearCommand(String type,
                        Map<String, String> args,
                        IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды clear...");

        try {
            ConcreteSet allSet = new AllSet();
            LOG_MANAGER.debug("Создан сет со всеми study group.");
            Set<StudyGroup> groupSet = studyGroupRepository.getConcreteSetOfStudyGroups(allSet);

            for (StudyGroup removableStudyGroup : groupSet) {
                studyGroupRepository.remove(removableStudyGroup);
            }
            LOG_MANAGER.info("Коллекция очищена.");

            return getSuccessfullyResponseDTO("Коллекция очищена." + System.lineSeparator());

        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Ошибка при очистке.");
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
