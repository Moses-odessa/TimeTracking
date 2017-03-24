package ua.moses.View;

import java.util.Scanner;

/**
 * Created by Admin on 23.03.2017.
 */
public class Console implements View {
    @Override
    public String Read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void Write(String s) {
        System.out.println(s);
    }
}
