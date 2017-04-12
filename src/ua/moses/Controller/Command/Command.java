package ua.moses.Controller.Command;

/**
 * Created by Admin on 11.04.2017.
 */
public interface Command {

    boolean check(String[] command);

    void run(String[] command);
}

