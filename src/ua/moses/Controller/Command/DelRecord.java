package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.View.View;

/**
 * Created by Admin on 13.04.2017.
 */
public class DelRecord implements Command {
    private View view;
    private DataOperations data;

    public DelRecord(View view, DataOperations data) {
        this.view = view;
        this.data = data;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("delrecord");
    }

    @Override
    public void run(String[] command) {
        if (command.length > 1 && command[1] != ""){
            if (data.delRecord(command[1])){
                view.Write("Запись с id " + command[1] + " удалена." );
            } else {
                view.Write("Ошибка удаления");
            };
        } else {
            view.Write("Неверный формат команды");
        }

    }

    public static String description() {
        return "delrecord|id: удаляет в журнале запись с номером id";
    }
}
