package ua.moses.Controller;

import ua.moses.Model.DataOperations;
import ua.moses.Model.TimeJournal;
import ua.moses.Model.WorkTime;
import ua.moses.Model.Worker;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainController {
    private DataOperations data;
    private View view;

    MainController(DataOperations data, View view){
        this.data = data;
        this.view = view;
    }

    public void run() {
        view.Write("Добро пожаловать!!!");
        boolean exit = false;
        while (!exit){
            view.Write("Введите команду или help для вывода списка доступных команд:");
            String[] command = view.Read();
            switch (command[0]){
                case "help": printCommandList();
                            break;
                case "workers": printWorkersList();
                            break;
                case "add": addWorker(command);
                    break;
                case "remove": removeWorker(command);
                    break;
                case "checkin": checkIn(command);
                    break;
                case "checkout": checkOut(command);
                    break;
                case "show": showTime(command);
                    break;
                case "showall": showTimeAll(command);
                    break;
                case "journal": showJournal(command);
                    break;

                case "exit": exit = true;
                    break;
                default: view.Write("Неизвестная команда. Повторите пожалуйста ввод");
                            break;

            }
        }
        view.Write("До скорой встречи!");

    }

    private void showJournal(String[] command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = new Date(0);
        Date dateTo = new Date();
        try {
            if (command.length > 3) dateTo = dateFormat.parse(command[3]);
            if (command.length > 2) dateFrom = dateFormat.parse(command[2]);
            if (command.length > 1 && command[1] != ""){
                TimeJournal journal = data.getJournal(command[1],dateFrom, dateTo);
                view.Write("Журнал посещений за период с " + dateFormat.format(dateFrom) + " по " + dateFormat.format(dateTo));
                view.Write("------------------------------------");
                view.Write(journal.toString());
                view.Write("------------------------------------");
            } else {
                view.Write("Неверный формат комманды");
            }

        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }
    }

    private void showTimeAll(String[] command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = new Date(0);
        Date dateTo = new Date();
        try {
            if (command.length > 2) dateTo = dateFormat.parse(command[2]);
            if (command.length > 1) dateFrom = dateFormat.parse(command[1]);
            Worker[] workers = data.getWorkersList();
            view.Write("Отчет за период с " + dateFormat.format(dateFrom) + " по " + dateFormat.format(dateTo));
            view.Write("------------------------------------");
            for (Worker worker: workers) {
                WorkTime worktime = data.getWorkingHours(Integer.toString(worker.getId()),dateFrom, dateTo);
                view.Write(worktime.toString());
            }
            view.Write("------------------------------------");
        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }

    }

    private void showTime(String[] command) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFrom = new Date(0);
        Date dateTo = new Date();
        try {
            if (command.length > 3) dateTo = dateFormat.parse(command[3]);
            if (command.length > 2) dateFrom = dateFormat.parse(command[2]);
            if (command.length > 1 && command[1] != ""){
                WorkTime worktime = data.getWorkingHours(command[1],dateFrom, dateTo);
                view.Write("Отчет за период с " + dateFormat.format(dateFrom) + " по " + dateFormat.format(dateTo));
                view.Write("------------------------------------");
                view.Write(worktime.toString());
                view.Write("------------------------------------");
            } else {
                view.Write("Неверный формат комманды");
            }

        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }
    }

    private void checkIn(String[] command) {
        check(command, "Приход");
    }

    private void checkOut(String[] command) {
        check(command, "Уход");
    }

    private void check(String[] command, String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String setDate = dateFormat.format(new Date());
        dateFormat = new SimpleDateFormat("HH:mm");
        String setTime = dateFormat.format(new Date());
        if (command.length > 3) setDate = command[3];
        if (command.length > 2) setTime = command[2];
        String setType;
        if (type.equalsIgnoreCase("Приход")){
            setType = "TRUE";
        } else {
            setType = "FALSE";
        }

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date setDateTime = dateFormat.parse(setDate + " " + setTime);
            if (command.length > 1 && command[1] != ""){
                if (data.check(command[1], setType, setDateTime)) {
                    view.Write(type + " сотрудника " + command[1] + " в " + setDateTime +" отмечен успешно" );

                } else {
                    view.Write("Невозможно отметить " + type + " сотрудника " + command[1] + " в " + setDateTime);
                }
            } else {
                view.Write("Неверный формат комманды");
            }

        } catch (ParseException e) {
            view.Write("Неверный формат даты/времени");
        }
    }


    private void addWorker(String[] command) {
        if (command.length > 1 && command[1] != ""){
            if (data.addWorker(command[1])){
                view.Write("Сотрудник " + command[1] + " добавлен в список." );
            } else {
                view.Write("Ошибка добавления");
            };
        } else {
            view.Write("Неверный формат комманды");
        }
    }

    private void removeWorker(String[] command) {
        if (command.length > 1 && command[1] != ""){
            if (data.removeWorker(command[1])){
                view.Write("Сотрудник " + command[1] + " удален." );
            } else {
                view.Write("Ошибка удаления");
            };
        } else {
            view.Write("Неверный формат комманды");
        }
    }

    private void printWorkersList() {
        Worker[] workers = data.getWorkersList();
        view.Write("Список сотрудников:");
        view.Write("------------------------------------");
        for (Worker worker:workers) {
            view.Write(worker.toString());
        }
        view.Write("------------------------------------");
    }

    private void printCommandList() {
        view.Write("Список доступных команд:");
        view.Write("workers: выводит список сотрудников");
        view.Write("add|fullname: добавляет сотрудника fullname в список сотрудников");
        view.Write("remove|fullname_or_id: удаляет сотрудника по имени или id");
        view.Write("checkin|fullname_or_id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD\n" +
                "\tесли дата и время опущены - используются текущие");
        view.Write("checkout|fullname_or_id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD\n" +
                "\tесли дата и время опущены - используются текущие");
        view.Write("show|fullname_or_id|datefrom|dateto: выводит отработанное время сотрудника fullname по имени или id\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты");
        view.Write("showall|datefrom|dateto: выводит отработанное время всех сотрудников\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты");
        view.Write("journal|fullname_or_id|datefrom|dateto: выводит журнал приходов/уходов сотрудника fullname по имени или id\n" +
                "\tгде datefrom и dateto - дата в формате YYYY-MM-DD\n" +
                "\tесли даты опущены - используются весь период до текущей даты");
        view.Write("help: для вывода этой справки");
        view.Write("exit: завершение программы");
    }
}
