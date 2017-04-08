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
    WorkTime getWorkingHours(String idOrFullName, Date dateFrom, Date dateTo);
    TimeJournal getJournal(String idOrFullName, Date dateFrom, Date dateTo);


}
