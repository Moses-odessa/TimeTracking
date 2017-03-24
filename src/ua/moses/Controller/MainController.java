package ua.moses.Controller;

import ua.moses.Model.DataOperations;
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

    }
}
