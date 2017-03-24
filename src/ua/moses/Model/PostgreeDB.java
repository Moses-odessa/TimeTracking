package ua.moses.Model;

import java.sql.*;

/**
 * Created by Admin on 23.03.2017.
 */
public class PostgreeDB implements DataOperations {

    private Connection connection;

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
    public String[] getWorkersList() {
        try {
            int size = getSize("employees");

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public.employees");
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] result = new String[size];


            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }
}
