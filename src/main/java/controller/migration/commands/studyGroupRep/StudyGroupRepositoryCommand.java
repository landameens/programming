package controller.migration.commands.studyGroupRep;

import controller.migration.commands.Command;
import response.Response;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.util.Map;

public abstract class StudyGroupRepositoryCommand extends Command {
    protected IStudyGroupRepository studyGroupRepository;

    public StudyGroupRepositoryCommand(String type,
                                       Map<String, String> args,
                                       IStudyGroupRepository studyGroupRepository) {
        super(type, args);
        this.studyGroupRepository = studyGroupRepository;
    }


    @Override
    public abstract Response execute();

}
