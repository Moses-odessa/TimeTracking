package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.View.View;

/**
 * Created by Admin on 11.04.2017.
 */
public class Remove implements Command {
    private View view;
    private DataOperations data;

    public Remove(View view, DataOperations data) {
        this.data = data;
        this.view = view;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("remove");
    }

    @Override
    public void run(String[] command) {
        if (command.length > 1 && !command[1].equals("")){
            if (data.removeWorker(command[1])){
                view.Write("Сотрудник " + command[1] + " удален." );
            } else {
                view.Write("Ошибка удаления");
            }
        } else {
            view.Write("Неверный формат команды");
        }
    }


    public static String description() {
        return "remove|fullname_or_id: удаляет сотрудника по имени или id";
    }

}
