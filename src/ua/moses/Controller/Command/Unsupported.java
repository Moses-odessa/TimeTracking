package ua.moses.Controller.Command;

import ua.moses.View.View;

/**
 * Created by Admin on 12.04.2017.
 */
public class Unsupported implements Command {
    private View view;

    public Unsupported(View view) {
        this.view = view;
    }

    @Override
    public boolean check(String[] command) {
        return true;
    }

    @Override
    public void run(String[] command) {
        view.Write("Неизвестная комманда!!!");

    }

}
