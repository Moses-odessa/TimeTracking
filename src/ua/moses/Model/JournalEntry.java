package ua.moses.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin on 05.04.2017.
 */
public class JournalEntry {
    private Worker worker;
    private Date date;
    private boolean type;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString(){
        String type = (this.type) ? ": Приход - " : ": Уход - ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd в HH:mm");
        return this.worker.toString() + type + dateFormat.format(this.date);
    }
}
