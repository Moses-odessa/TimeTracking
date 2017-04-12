package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.Model.WorkTime;
import ua.moses.Model.Worker;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 12.04.2017.
 */
public class Show implements Command {
    private View view;
    private DataOperations data;

    public Show(View view, DataOperations data) {
        this.view = view;
        this.data = data;
    }


    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("show");
    }

    @Override
    public void run(String[] command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = new Date(0);
        Date dateTo = new Date();
        Worker[] workers;
        if (command.length == 1 || command[1].equalsIgnoreCase("all")) {
            workers = data.getWorkersList();
        } else {
            workers = new Worker[] {data.getWorker(command[1])};
        }

        try {
            if (command.length > 3) dateTo = dateFormat.parse(command[3]);
            if (command.length > 2) dateFrom = dateFormat.parse(command[2]);
            view.Write("Отчет за период с " + dateFormat.format(dateFrom) + " по " + dateFormat.format(dateTo));
            view.Write("------------------------------------");
            for (Worker worker: workers) {
                WorkTime worktime = data.getWorkingHours(worker, dateFrom, dateTo);
                view.Write(worktime.toString());
            }
            view.Write("------------------------------------");
        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }

    }


    public static String description() {
        return "show|fullname_or_id|datefrom|dateto: выводит отработанное время сотрудника fullname по имени или id\n" +
                "\t(если вместо имени указано all или опущено вместе с датами - для всех сотрудников)\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты";
    }
}
