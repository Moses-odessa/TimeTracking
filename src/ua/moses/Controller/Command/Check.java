package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 11.04.2017.
 */
public class Check implements Command {
    private View view;
    private DataOperations data;

    public Check(View view, DataOperations data) {
        this.data = data;
        this.view = view;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("checkin") || command[0].equalsIgnoreCase("checkout");
    }

    @Override
    public void run(String[] command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String setDate = dateFormat.format(new Date());
        dateFormat = new SimpleDateFormat("HH:mm");
        String setTime = dateFormat.format(new Date());
        if (command.length > 3) setDate = command[3];
        if (command.length > 2) setTime = command[2];
        String setType;
        String type;
        if (command[0].equalsIgnoreCase("checkin")){
            setType = "TRUE";
            type = "Приход";
        } else {
            setType = "FALSE";
            type = "Уход";
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date setDateTime = dateFormat.parse(setDate + " " + setTime);
            if (command.length > 1 && !command[1].equals("")){
                if (data.check(command[1], setType, setDateTime)) {
                    view.Write(type + " сотрудника " + command[1] + " в " + dateFormat.format(setDateTime) +" отмечен успешно" );

                } else {
                    view.Write("Невозможно отметить " + type + " сотрудника " + command[1] + " в " + dateFormat.format(setDateTime));
                }
            } else {
                view.Write("Неверный формат комманды");
            }

        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }

    }


    public static String description() {
        return "checkin(checkout)|fullname_or_id|time|date: отмечает приход (уход) на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD\n" +
                "\tесли дата и время опущены - используются текущие";
    }
}
