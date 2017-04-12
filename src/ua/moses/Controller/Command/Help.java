package ua.moses.Controller.Command;

import ua.moses.View.View;

/**
 * Created by Admin on 11.04.2017.
 */
public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean check(String[] command) {
        return command[0].equalsIgnoreCase("help");
    }

    @Override
    public void run(String[] command) {
        view.Write("Список доступных команд:");
        view.Write(List.description());
        view.Write(Add.description());
        view.Write(Remove.description());
        view.Write(Check.description());
        view.Write(Show.description());
        view.Write(Journal.description());
        view.Write(Help.description());
        view.Write("exit: завершение программы");

    }


    public static String description() {
        return "help: для вывода этой справки";
    }
}
