package ua.moses.Model;

import java.sql.*;
import java.util.Arrays;


/**
 * Created by Admin on 23.03.2017.
 */
public class PostgreeDB implements DataOperations {

    private Connection connection;
    private final String workersTableName = "employees";
    private final String recordingTableName = "notation";
    private final String[] workersTableColumns = {"id","fullname"};
    private final String[] recordingTableColumns = {"id", "worker_id", "type", "date", "time"};

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
    public Workers[] getWorkersList() {
        try {
            int size = getSize("employees");

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + workersTableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            Workers[] result = new Workers[size];
            int index = 0;
            while (rs.next()) {
                Workers dataSet = new Workers();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.setId(rs.getInt("id"));
                    dataSet.setFullname(rs.getString("fullname"));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Workers[0];
        }
    }

    @Override
    public boolean addWorker(String fullname) {
        String[] columns = Arrays.copyOfRange(workersTableColumns,1,2);

        String[] values = new String[1];
        values[0] = fullname;
        return addValue(workersTableName, columns, values);

    }

    @Override
    public boolean removeWorker(String idOrFullname) {
        int id;
        if (isInt(idOrFullname)){
            id = Integer.parseInt(idOrFullname);
        } else {
            id = getIdByFullname(idOrFullname);;
        }

        removeValue(recordingTableName, "worker_id = " + id);
        return removeValue(workersTableName, "id = " + id);
    }



    private int getIdByFullname(String fullname)  {
        int result=-1;
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT id FROM public." + workersTableName + " WHERE fullname='" + fullname +"'");
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

            String sql = "DELETE FROM public." + workersTableName + " WHERE " + criteria;
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

            String sql = "INSERT INTO public." + workersTableName + " (" + extractValues(columns, "") + ")" +
                    "VALUES (" + extractValues(values, "'") + ")";
            if (stmt.executeUpdate(sql)>0) result = true;
            stmt.close();

        } catch (SQLException e) {
            //e.printStackTrace();

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
