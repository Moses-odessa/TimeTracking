package ua.moses.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 05.04.2017.
 */
public class TimeJournal {
    private Worker worker;
    private Date dateFrom;
    private Date dateTo;

    private ArrayList<Record> records = new ArrayList<>();

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }


    @Override
    public String toString(){
        String result = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result += this.worker.toString() + " с " + dateFormat.format(this.dateFrom) + " по " + dateFormat.format(this.dateTo) + "\n";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd в HH:mm");
        for (Record entry: this.records) {
            String type = (entry.type) ? ": Приход - " : ": Уход   - ";
            result += entry.id + type + dateFormat.format(entry.date) + "\n";
        }

        return result;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void addRecord(int id, Date date, boolean type) {
        this.records.add(new Record(id, date, type));
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    private class Record {
        private int id;
        private Date date;
        private boolean type;

        public Record(int id, Date date, boolean type) {
            this.id = id;
            this.date = date;
            this.type = type;
        }
    }
}
