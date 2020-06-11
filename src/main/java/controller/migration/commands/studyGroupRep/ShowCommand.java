package controller.migration.commands.studyGroupRep;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;
import response.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ShowCommand.class);
    public ShowCommand(String type,
                       Map<String, String> args,
                       IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды show...");
        try {
            List<StudyGroup> studyGroupSet = studyGroupRepository.getAll();

            LOG_MANAGER.info("Вывод информации о коллекции.");
            return getSuccessfullyResponseDTO(getMessage(studyGroupSet));
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к коллекции...");
            return getBadRequestResponseDTO(e.getMessage());
        }
    }

    private String getMessage(List<StudyGroup> studyGroupSet){

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
