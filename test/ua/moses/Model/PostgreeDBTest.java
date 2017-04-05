package ua.moses.Model;

/**
 * Created by Admin on 05.04.2017.
 */
public class PostgreeDBTest extends DataOperationsTest {


    @Override
    public DataOperations getDataOperations() {
        return new PostgreeDB("timetracking", "postgres", "tend");
    }
}
