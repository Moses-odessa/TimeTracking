package ua.moses.Controller.Command;

import ua.moses.Model.DataOperations;
import ua.moses.Model.Worker;
import ua.moses.View.View;


public class List implements Command {

    private View view;
    private DataOperations data;

    public List(View view, DataOperations data) {
        this.view = view;
        this.data = data;
    }


    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("list");
    }

    @Override
    public void run(String[] command) {
        Worker[] workers = data.getWorkersList();
        view.Write("Список сотрудников:");
        view.Write("------------------------------------");
        for (Worker worker:workers) {
            view.Write(worker.toString());
        }
        view.Write("------------------------------------");

    }


    public static String description() {
        return "list: выводит список сотрудников";
    }
}
