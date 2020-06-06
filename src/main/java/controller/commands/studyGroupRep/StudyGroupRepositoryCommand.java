package controller.commands.studyGroupRep;

import controller.commands.Command;
import domain.studyGroupRepository.IStudyGroupRepository;
import response.Response;

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
