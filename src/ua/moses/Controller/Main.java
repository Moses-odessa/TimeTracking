package ua.moses.Controller;

import ua.moses.Model.DataOperations;
import ua.moses.Model.PostgreeDB;
import ua.moses.View.Console;
import ua.moses.View.View;

public class Main {

    public static void main(String[] args) {
        View view = new Console();
        DataOperations data = new PostgreeDB("timetracking", "postgres", "tend");
        MainController controller = new MainController(data, view);
        controller.run();
    }
}
