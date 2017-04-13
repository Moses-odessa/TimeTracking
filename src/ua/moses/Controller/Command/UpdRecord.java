package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 13.04.2017.
 */
public class UpdRecord implements Command {
    private View view;
    private DataOperations data;

    public UpdRecord(View view, DataOperations data) {
        this.view = view;
        this.data = data;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("updrecord");
    }

    @Override
    public void run(String[] command) {
        if (command.length > 3) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date setDateTime = dateFormat.parse(command[3] + " " + command[2]);
                if (data.updateRecord(command[1], setDateTime)) {
                    view.Write("Запись с id " + command[1] + " обновлена успешно");

                } else {
                    view.Write("Невозможно обновить запись с id " + command[1]);
                }
            } catch (ParseException e) {
                view.Write("Неверный формат даты/времени");
            }
        } else {
            view.Write("Неверный формат комманды");
        }

    }

    public static String description() {
        return "updrecord|id|new_time|new_date: обновляет в журнале запись с номером id новым значением даты и времени,\n" +
                "\tгде new_time - время в формате hh:mm, и new_date - дата в формате YYYY-MM-DD";
    }
}
