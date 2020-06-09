package controller.getController;

import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import controller.services.JSONAdapter;
import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import org.apache.commons.configuration2.Configuration;
import response.Response;
import response.Status;

import java.util.Map;
import java.util.Set;

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
        ConcreteSet concreteSet = new ConcreteSet() {
            @Override
            public Set<StudyGroup> execute(Set<StudyGroup> studyGroups) {
                return studyGroups;
            }
        };

        Set<StudyGroup> studyGroups;
        try {
            studyGroups = studyGroupRepository.getConcreteSetOfStudyGroups(concreteSet);
        } catch (StudyGroupRepositoryException e) {
            throw new CommandExecutionException(e);
        }

        return new Response(Status.SUCCESSFULLY, jsonAdapter.toJson(studyGroups));
    }
}
