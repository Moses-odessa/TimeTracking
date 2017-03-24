package ua.moses.Controller;

import ua.moses.Model.DataOperations;
import ua.moses.Model.Workers;
import ua.moses.View.View;

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
                case "exit": System.exit(0);
                    break;
                default: view.Write("Неизвестная команда. Повторите пожалуйста ввод");
                            break;

            }

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
        view.Write("help: для вывода этой справки");
        view.Write("exit: завершение программы");
    }
}
