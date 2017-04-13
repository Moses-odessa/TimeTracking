package ua.moses.Model;

import java.util.Date;

/**
 * Created by Admin on 23.03.2017.
 */
public interface DataOperations {
    Worker[] getWorkersList();
    boolean addWorker(String fullName);
    boolean removeWorker(String idOrFullName);
    boolean check(String idOrFullName, String type, Date datetime);
    Worker getWorker (String idOrFullName);
    WorkTime getWorkingHours(Worker worker, Date dateFrom, Date dateTo);
    TimeJournal getJournal(Worker worker, Date dateFrom, Date dateTo);
    boolean delRecord(String id);
    boolean updateRecord(String id, Date date);
}
