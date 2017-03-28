package ua.moses.Model;

import java.sql.*;

/**
 * Created by Admin on 23.03.2017.
 */
public class PostgreeDB implements DataOperations {

    private Connection connection;
    private final String tableNameWorkers = "employees";

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
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableNameWorkers);
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
    public void addWorker(String fullname) {
        try {
            Statement stmt = connection.createStatement();

            String sql = "INSERT INTO public." + tableNameWorkers + " (fullname)" +
                    "VALUES ('" + fullname + "')";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
