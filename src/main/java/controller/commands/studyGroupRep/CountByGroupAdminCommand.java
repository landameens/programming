package controller.commands.studyGroupRep;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.person.Country;
import domain.studyGroupRepository.IStudyGroupRepository;
import domain.studyGroupRepository.concreteSet.AllSet;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;
import response.Response;

import java.util.Map;
import java.util.Set;

public class CountByGroupAdminCommand extends StudyGroupRepositoryCommand {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(CountByGroupAdminCommand.class);
    public CountByGroupAdminCommand(String type,
                                    Map<String, String> args,
                                    IStudyGroupRepository studyGroupRepository) {
        super(type, args, studyGroupRepository);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Выполнение команды count_by_group_admin...");
        String passportID = args.get("groupAdminPassportID");
        LOG_MANAGER.debug("Поле passportId заполнено.");
        String name = args.get("groupAdminName");
        LOG_MANAGER.debug("Поле name заполнено.");
        Country nationality = Country.getCountry(args.get("groupAdminNationality") == null ? null : args.get("groupAdminNationality").toLowerCase());
        LOG_MANAGER.debug("Поле nationality заполнено.");
        int height = Integer.parseInt(args.get("groupAdminHeight"));
        LOG_MANAGER.debug("Поле height заполнено.");

        try {
            ConcreteSet allSet = new AllSet();
            Set<StudyGroup> allStudyGroupSet = studyGroupRepository.getConcreteSetOfStudyGroups(allSet);
//            int count = 0;
//            if(nationality == null){
//                for (StudyGroup studyGroup : allStudyGroupSet) {
//                    if (studyGroup.getGroupAdmin().getName().equals(name) &&
//                            studyGroup.getGroupAdmin().getHeight() == height &&
//                            studyGroup.getGroupAdmin().getNationality() == null &&
//                            studyGroup.getGroupAdmin().getPassportID().equals(passportID)) {
//                        count += 1;
//                    }
//                }
//            } else {
//                for (StudyGroup studyGroup : allStudyGroupSet) {
//                    if (studyGroup.getGroupAdmin().getName().equals(name) &&
//                            studyGroup.getGroupAdmin().getHeight() == height &&
//                            studyGroup.getGroupAdmin().getNationality().equals(nationality) &&
//                            studyGroup.getGroupAdmin().getPassportID().equals(passportID)) {
//                        count += 1;
//                    }
//                }
//            }
            Long count = allStudyGroupSet.stream().filter(studyGroup -> {
                if (nationality == null) {
                    if (studyGroup.getGroupAdmin().getName().equals(name) &&
                            studyGroup.getGroupAdmin().getHeight() == height &&
                            studyGroup.getGroupAdmin().getNationality() == null &&
                            studyGroup.getGroupAdmin().getPassportID().equals(passportID)) {
                        return true;
                    }
                } else {
                    if (studyGroup.getGroupAdmin().getName().equals(name) &&
                            studyGroup.getGroupAdmin().getHeight() == height &&
                            studyGroup.getGroupAdmin().getNationality().equals(nationality) &&
                            studyGroup.getGroupAdmin().getPassportID().equals(passportID)) {
                        return true;
                    }
                }
                return false;
            }).count();

            if(count == 0){
                return getPreconditionFailedResponseDTO("Групп с равным значением groupAdmin в коллекции нет." + System.lineSeparator());
            }

            LOG_MANAGER.info("Количество групп с таким полем groupAdmin получено.");
            String mesage = new StringBuilder().append("Групп с таким полем groupAdmin в коллекции - ").append(count).append(System.lineSeparator()).toString();
            return getSuccessfullyResponseDTO(mesage);
        } catch (StudyGroupRepositoryException e) {
            LOG_MANAGER.error("Произошла ошибка при обращении к коллекции.");
            return getBadRequestResponseDTO(e.getMessage());
        }
    }
}
