package ua.moses.Model;

/**
 * Created by Admin on 23.03.2017.
 */
public interface DataOperations {
    Workers[] getWorkersList();
    boolean addWorker(String fullname);
    boolean removeWorker(String idOrFullname);

}
