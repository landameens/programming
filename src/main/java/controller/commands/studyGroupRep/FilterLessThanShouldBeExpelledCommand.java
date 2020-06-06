package controller.commands.studyGroupRep;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;
import response.Response;

import java.util.Map;
import java.util.Set;

public class FilterLessThanShouldBeExpelledCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(FilterLessThanShouldBeExpelledCommand.class);
    public FilterLessThanShouldBeExpelledCommand(String type,
                                                 Map<String, String> args,
                                                 IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды filter_less_than_should_be_expelled...");
        LOG_MANAGER.debug("Поле shouldBeExpelled заполнено.");
        Long shouldBeExpelled = Long.parseLong(args.get("should_be_expelled"));

        try {
            ConcreteSet allSet = new AllSet();
            Set<StudyGroup> allStudyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(allSet);

            StringBuilder message = new StringBuilder();
            for (StudyGroup studyGroup : allStudyGroupSet) {
                if (studyGroup.getShouldBeExpelled() == null){
                    continue;
                }
                if(studyGroup.getShouldBeExpelled() - shouldBeExpelled < 0 ){
                    message.append(studyGroup.toString()).append(System.lineSeparator()).append(System.lineSeparator());
                }
            }

            if(message.length() == 0){
                return getPreconditionFailedResponseDTO("Групп с мешьшим значением поля shold_be_expelled в коллекции нет." + System.lineSeparator());
            }
            LOG_MANAGER.debug("Группы с меньшим значением поля shouldBeExpelled выведены.");
            return getSuccessfullyResponseDTO(message.toString());
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к коллекции.");
            e.printStackTrace();
        }

        return null;
    }
}
