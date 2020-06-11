package controller.getController;

import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import controller.services.JSONAdapter;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import org.apache.commons.configuration2.Configuration;
import response.Response;
import response.Status;

import java.util.List;
import java.util.Map;

public final class GetAllStudyGroupsCommand extends Command {
    private IStudyGroupRepository studyGroupRepository;
    private JSONAdapter jsonAdapter;

    /**
     * Use for creating command via factories.
     */
    public GetAllStudyGroupsCommand(String commandName, Map<String, String> arguments, Configuration configuration) {
        super(commandName, arguments, configuration);
    }

    @Override
    protected Response processExecution() throws CommandExecutionException {
        List<StudyGroup> studyGroups;
        try {
            studyGroups = studyGroupRepository.getAll();
        } catch (StudyGroupRepositoryException e) {
            throw new CommandExecutionException(e);
        }

        return new Response(Status.SUCCESSFULLY, jsonAdapter.toJson(studyGroups));
    }
}
