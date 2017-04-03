package ua.moses.Model;

import java.util.Date;

/**
 * Created by Admin on 23.03.2017.
 */
public interface DataOperations {
    Workers[] getWorkersList();
    boolean addWorker(String fullName);
    boolean removeWorker(String idOrFullName);
    boolean check(String idOrFullName, String type, Date datetime);

}
