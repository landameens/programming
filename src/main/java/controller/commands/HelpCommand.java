package controller.commands;

import controller.response.Response;

import java.util.Map;

public class HelpCommand extends Command {
    public HelpCommand(String type,
                       Map<String, String> args) {
        super(type, args);
    }

    @Override
    public Response execute() {
        return getSuccessfullyResponseDTO(getMessage());
    }

    private String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)").append(System.lineSeparator());
        stringBuilder.append("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении").append(System.lineSeparator());
        stringBuilder.append("add {element} : добавить новый элемент в коллекцию").append(System.lineSeparator());
        stringBuilder.append("update id {element} : обновить значение элемента коллекции, id которого равен заданному").append(System.lineSeparator());
        stringBuilder.append("remove_by_id id : удалить элемент из коллекции по его id").append(System.lineSeparator());
        stringBuilder.append("clear : очистить коллекцию").append(System.lineSeparator());
        stringBuilder.append("save : сохранить коллекцию в файл").append(System.lineSeparator());
        stringBuilder.append("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.").append(System.lineSeparator());
        stringBuilder.append("exit : завершить программу (без сохранения в файл)").append(System.lineSeparator());
        stringBuilder.append("add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции").append(System.lineSeparator());
        stringBuilder.append("remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный").append(System.lineSeparator());
        stringBuilder.append("history : вывести последние 15 команд (без их аргументов)").append(System.lineSeparator());
        stringBuilder.append("count_by_group_admin groupAdmin : вывести количество элементов, значение поля groupAdmin которых равно заданном").append(System.lineSeparator());
        stringBuilder.append("filter_by_should_be_expelled shouldBeExpelled : вывести элементы, значение поля shouldBeExpelled которых равно заданному").append(System.lineSeparator());
        stringBuilder.append("filter_less_than_should_be_expelled shouldBeExpelled : вывести элементы, значение поля shouldBeExpelled которых меньше заданного").append(System.lineSeparator());

        return stringBuilder.toString();
    }
}
