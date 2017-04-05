package ua.moses.Model;


import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;


/**
 * Created by Admin on 04.04.2017.
 */
public abstract class DataOperationsTest {
    private DataOperations data;

    @Before
    public void setup() {
        data = getDataOperations();
    }

    public abstract DataOperations getDataOperations();

    @Test
    public void testAddRemoveList(){

        String testName = "Test Test Test";
        data.addWorker(testName);
        assertThat(Arrays.toString(data.getWorkersList()), containsString(testName));
        data.removeWorker(testName);
        assertThat(Arrays.toString(data.getWorkersList()), CoreMatchers.not(containsString(testName)));
    }

}
