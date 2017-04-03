package ua.moses.Controller;

import ua.moses.Model.DataOperations;
import ua.moses.Model.Workers;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 24.03.2017.
 */
public class MainController {
    private DataOperations data;
    private View view;

    MainController(DataOperations data, View view){
        this.data = data;
        this.view = view;
    }

    public void run() {
        view.Write("Добро пожаловать!!!");
        while (true){
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

                case "exit": System.exit(0);
                    break;
                default: view.Write("Неизвестная команда. Повторите пожалуйста ввод");
                            break;

            }

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

        dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm");
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
        Workers[] workers = data.getWorkersList();
        view.Write("Список сотрудников:");

        for (int i = 0; i < workers.length ; i++) {
            view.Write(workers[i].getId() + ":" + workers[i].getFullname());

        }
    }

    private void printCommandList() {
        view.Write("Список доступных команд:");
        view.Write("workers: выводит список сотрудников");
        view.Write("add|fullname: добавляет сотрудника fullname в список сотрудников");
        view.Write("remove|fullname or id: удаляет сотрудника по имени или id");
        view.Write("checkin|fullname or id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD]n" +
                "\tесли дата и время опущены - используются текущие");
        view.Write("checkout|fullname or id|time|date: отмечает приход на работу сотрудника fullname по имени или id\n" +
                "\tгде time - время в формате hh:mm, и date - дата в формате YYYY-MM-DD]n" +
                "\tесли дата и время опущены - используются текущие");
        view.Write("help: для вывода этой справки");
        view.Write("exit: завершение программы");
    }
}
