package ua.moses.Controller;

import ua.moses.Controller.Command.*;
import ua.moses.Model.DataOperations;
import ua.moses.Model.TimeJournal;
import ua.moses.Model.WorkTime;
import ua.moses.Model.Worker;
import ua.moses.View.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainController {
    private DataOperations data;
    private View view;
    private Command[] commands;

    MainController(DataOperations data, View view){
        this.data = data;
        this.view = view;
        this.commands = new Command[] {new Help(view),
                new List(view, data),
                new Add(view, data),
                new Remove(view, data),
                new Check(view, data),
                new Show(view, data),
                new Journal(view, data),
                new DelRecord(view, data),
                new UpdRecord(view, data),
                new Unsupported(view)};
    }

    public void run() {

        view.Write("Добро пожаловать!!!");

        while (true){
            view.Write("Введите нужную команду или help для вывода списка доступных команд:");
            String[] target = view.Read();
            if (target[0].equals("exit")) break;

            for (Command command:commands) {
                if (command.check(target)) {
                    command.run(target);
                    break;
                }
            }

        }
        view.Write("До скорой встречи!");

    }

    private void showJournal(String[] command) {

    }


}
