package controller.commands.studyGroupRep;

import controller.response.Response;
import domain.studyGroupRepository.IStudyGroupRepository;
import manager.LogManager;
import storage.exception.DAOException;

import java.util.Map;

public class SaveCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(SaveCommand.class);
    public SaveCommand(String type,
                       Map<String, String> args,
                       IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды save...");
        try {
            studyGroupRepository.save();

            LOG_MANAGER.info("Коллекция сохранена...");
            return getSuccessfullyResponseDTO("Коллекция сохранена в файл." + System.lineSeparator());
        } catch (DAOException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к памяти...");
            return getInternalErrorResponseDTO(e.getMessage());
        }
    }

}
