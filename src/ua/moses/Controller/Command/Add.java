package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.View.View;

/**
 * Created by Admin on 11.04.2017.
 */
public class Add implements Command {
    private View view;
    private  DataOperations data;

    public Add(View view, DataOperations data) {
        this.data = data;
        this.view = view;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("add");
    }

    @Override
    public void run(String[] command) {

        if (command.length > 1 && command[1] != ""){
            if (data.addWorker(command[1])){
                view.Write("Сотрудник " + command[1] + " добавлен в список." );
            } else {
                view.Write("Ошибка добавления");
            };
        } else {
            view.Write("Неверный формат команды");
        }
    }


    public static String description() {
        return "add|fullname: добавляет сотрудника fullname в список сотрудников";
    }
}
