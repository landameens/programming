package controller.migration.commands;

import controller.migration.commands.studyGroupRep.StudyGroupRepositoryCommand;
import domain.studyGroupRepository.IStudyGroupRepository;
import response.Response;
import manager.LogManager;
import storage.exception.DAOException;

import java.util.Map;

public class ExitCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ExitCommand.class);
    public ExitCommand(String name, Map<String, String> args, IStudyGroupRepository studyGroupRepository) {
        super(name, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Осуществляется выход из программы.");
        try {
            studyGroupRepository.save();

            LOG_MANAGER.info("Коллекция сохранена...");
            return getProgrammExitResponceDTO("Произведен выход из программы.");
        } catch (DAOException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к памяти...");
            return getInternalErrorResponseDTO(e.getMessage());
        }
    }
}
