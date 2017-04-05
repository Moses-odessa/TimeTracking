package ua.moses.Model;

import java.sql.*;
import java.util.Date;
import java.util.*;


/**
 * Created by Admin on 23.03.2017.
 */
public class PostgreeDB implements DataOperations {

    private Connection connection;
    private final String workersTableName = "employees";
    private final String recordingTableName = "notation";
    private final String[] workersTableColumns = {"id","fullname"};
    private final String[] recordingTableColumns = {"id", "worker_id", "type", "milisec"};

    public PostgreeDB(String database, String userName, String password){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project.", e);
        }
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + database, userName,
                    password);
        } catch (SQLException e) {
            this.connection = null;
            throw new RuntimeException(
                    String.format("Cant get connection for model:%s user:%s",
                            database, userName),
                    e);
        }
    }

    @Override
    public Worker[] getWorkersList() {
        try {
            int size = getSize(workersTableName);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + workersTableName);
            Worker[] result = new Worker[size];
            int index = 0;
            while (rs.next()) {
                result[index] = new Worker();
                result[index].setId(rs.getInt("id"));
                result[index].setFullName(rs.getString("fullname"));
                index++;
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Worker[0];
        }
    }

    @Override
    public boolean addWorker(String fullName) {
        String[] columns = Arrays.copyOfRange(workersTableColumns,1,2);

        String[] values = new String[1];
        values[0] = fullName;
        return addValue(workersTableName, columns, values);

    }

    @Override
    public boolean removeWorker(String idOrFullName) {
        int id = getWorkerID(idOrFullName);

        removeValue(recordingTableName, "worker_id = " + id);
        return removeValue(workersTableName, "id = " + id);
    }


    @Override
    public boolean check(String idOrFullName, String type, Date datetime) {
        int workerID = getWorkerID(idOrFullName);

        String[] columns = Arrays.copyOfRange(recordingTableColumns,1,recordingTableColumns.length);
        Object[] values = new Object[recordingTableColumns.length-1];
        values[0] = workerID;
        values[1] = type;
        values[2] = datetime.getTime();

        return addValue(recordingTableName, columns, values);
    }

    private int getWorkerID(String idOrFullName) {
        int workerID;
        if (isInt(idOrFullName)){
            workerID = Integer.parseInt(idOrFullName);
        } else {
            workerID = getIdByFullName(idOrFullName);;
        }
        return workerID;
    }

    @Override
    public WorkTime getWorkingHours(String idOrFullName, Date dateFrom, Date dateTo) {
        WorkTime result = new WorkTime();
        Worker worker = new Worker();
        worker.setId(getWorkerID(idOrFullName));
        worker.setFullName(getFullNameById(worker.getId()));
        result.setWorker(worker);
        try {
            Statement stmt;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT worker_id,\n" +
                    "SUM(CASE WHEN type THEN (-milisec)\n" +
                    "     ELSE (milisec)\n" +
                    "     END) as mili\n" +
                    "FROM public." + recordingTableName + "\n" +
                    "WHERE worker_id=" + result.getWorker().getId() +
                    " AND milisec >= " + dateFrom.getTime() +
                    " AND milisec < " + (dateTo.getTime()+ 24 * 60 * 60 * 1000)+"\n" +
                    "GROUP BY worker_id");

            if (rsCount.next()){
                result.setWorkingHours(rsCount.getLong("mili")/ (60 * 60 * 1000));
            } else {
                result.setWorkingHours(0);
            }
            rsCount.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<JournalEntry> getJournal(String idOrFullName, Date dateFrom, Date dateTo) {
        ArrayList<JournalEntry> result = new ArrayList<>();
        Worker worker = new Worker();
        worker.setId(getWorkerID(idOrFullName));
        worker.setFullName(getFullNameById(worker.getId()));

        try {
            Statement stmt;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT type, milisec\n" +
                    "FROM public." + recordingTableName + "\n" +
                    "WHERE worker_id=" + worker.getId() +
                    " AND milisec >= " + dateFrom.getTime() +
                    " AND milisec < " + (dateTo.getTime()+ 24 * 60 * 60 * 1000)+ "\n" +
                    "ORDER BY milisec");

            while (rsCount.next()){
                JournalEntry currentEntry = new JournalEntry();
                currentEntry.setWorker(worker);
                currentEntry.setType(rsCount.getBoolean("type"));
                currentEntry.setDate(new Date(rsCount.getLong("milisec")));
                result.add(currentEntry);
            }
            rsCount.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getFullNameById(int id) {
        String result="";
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT fullname FROM public." + workersTableName
                    + " WHERE id=" + id);
            if (rsCount.next()) {
                result = rsCount.getString("fullname");
            }
            rsCount.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private int getIdByFullName(String fullName)  {
        int result=-1;
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT id FROM public." + workersTableName + " WHERE fullName='" + fullName +"'");
            rsCount.next();
            result = rsCount.getInt("id");
            rsCount.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean removeValue(String tableName, String criteria) {
        boolean result = false;
        try {
            Statement stmt = connection.createStatement();

            String sql = "DELETE FROM public." + tableName + " WHERE " + criteria;
            if (stmt.executeUpdate(sql)>0) result = true;
            stmt.close();

        } catch (SQLException e) {
            //e.printStackTrace();

        }
        return result;
    }

    private boolean addValue(String tableName, String [] columns, Object[] values) {
        boolean result = false;
        try {
            Statement stmt = connection.createStatement();

            String sql = "INSERT INTO public." + tableName + " (" + extractValues(columns, "") + ")" +
                    "VALUES (" + extractValues(values, "'") + ")";
            if (stmt.executeUpdate(sql)>0) result = true;
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }

    private String extractValues(Object[] values, String framing) {
        String result = "";
        for (int i = 0; i < values.length; i++) {
            result+=framing + values[i] + framing;
            if (i < values.length - 1){
                result+= ", ";
            }
        }
        return result;
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }
}
