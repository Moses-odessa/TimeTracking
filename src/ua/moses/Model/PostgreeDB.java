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
    private final String[] workersTableColumns = {"id", "fullname"};
    private final String[] recordingTableColumns = {"id", "worker_id", "type", "milisec"};

    public PostgreeDB(String database, String userName, String password) {
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

        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, workersTableName, null);
            if (!tables.next()) {
                createWorkersTable();
            }
            tables = dbm.getTables(null, null, recordingTableName, null);
            if (!tables.next()) {
                createRecordingTable();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't check tables exists", e);
        }

    }

    private void createRecordingTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE SEQUENCE public." + recordingTableName + "_id_seq\n" +
                "    INCREMENT 1\n" +
                "    START 1\n" +
                "    MINVALUE 1\n" +
                "    MAXVALUE 9223372036854775807\n" +
                "    CACHE 1;";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE public." + recordingTableName + "\n" +
                "(\n" +
                "    id integer NOT NULL DEFAULT nextval('" + recordingTableName + "_id_seq'::regclass),\n" +
                "    worker_id integer NOT NULL,\n" +
                "    type boolean NOT NULL,\n" +
                "    milisec bigint NOT NULL,\n" +
                "    CONSTRAINT " + recordingTableName + "_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "WITH (\n" +
                "    OIDS = FALSE\n" +
                ")\n" +
                "TABLESPACE pg_default";
        stmt.executeUpdate(sql);

        stmt.close();
    }

    private void createWorkersTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE SEQUENCE public." + workersTableName + "_id_seq\n" +
                "    INCREMENT 1\n" +
                "    START 1\n" +
                "    MINVALUE 1\n" +
                "    MAXVALUE 9223372036854775807\n" +
                "    CACHE 1;";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE public." + workersTableName + "\n" +
                "(\n" +
                "    id integer NOT NULL DEFAULT nextval('" + workersTableName + "_id_seq'::regclass),\n" +
                "    fullname text COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    CONSTRAINT " + workersTableName + "_pkey PRIMARY KEY (id)\n" +
                ")\n" +
                "WITH (\n" +
                "    OIDS = FALSE\n" +
                ")\n" +
                "TABLESPACE pg_default";
        stmt.executeUpdate(sql);

        stmt.close();
    }

    @Override
    public Worker[] getWorkersList() {
        try {
            int size = getSize(workersTableName);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + workersTableName + " ORDER BY id");
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
        if (fullName.isEmpty() || fullName.trim().length() < 1 || getIdByFullName(fullName) > 0) return false;
        String[] columns = Arrays.copyOfRange(workersTableColumns, 1, 2);

        String[] values = new String[1];
        values[0] = fullName.trim();
        return addValue(workersTableName, columns, values);

    }

    @Override
    public boolean removeWorker(String idOrFullName) {
        Worker worker = getWorker(idOrFullName);
        if (!exist(worker)) return false;

        removeValue(recordingTableName, "worker_id = " + worker.getId());
        return removeValue(workersTableName, "id = " + worker.getId());
    }


    @Override
    public boolean check(String idOrFullName, String type, Date datetime) {
        Worker worker = getWorker(idOrFullName);
        if (!exist(worker)) return false;

        String[] columns = Arrays.copyOfRange(recordingTableColumns, 1, recordingTableColumns.length);
        Object[] values = new Object[recordingTableColumns.length - 1];
        values[0] = worker.getId();
        values[1] = type;
        values[2] = datetime.getTime();

        return addValue(recordingTableName, columns, values);
    }

    @Override
    public Worker getWorker(String idOrFullName) {
        Worker result = new Worker();
        result.setId(getWorkerID(idOrFullName));
        result.setFullName(getFullNameById(result.getId()));
        if (result.getId() < 1 || result.getFullName().isEmpty()){
            return null;
        } else {
            return result;
        }
    }

    private int getWorkerID(String idOrFullName) {
        int workerID = -1;
        if (isInt(idOrFullName)) {
            workerID = Integer.parseInt(idOrFullName);
        } else {
            workerID = getIdByFullName(idOrFullName);

        }
        return workerID;
    }

    @Override
    public WorkTime getWorkingHours(Worker worker, Date dateFrom, Date dateTo) {
        if (!exist(worker)) return null;
        WorkTime result = new WorkTime();
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
                    " AND milisec < " + (dateTo.getTime() + 24 * 60 * 60 * 1000) + "\n" +
                    "GROUP BY worker_id");

            if (rsCount.next()) {
                result.setWorkingHours(rsCount.getLong("mili") / (60 * 60 * 1000));
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

    private boolean exist(Worker worker) {
        boolean result = false;
        if (worker == null) return false;
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT COUNT(*) FROM public." + workersTableName +
                    " WHERE (id=" + worker.getId() + " AND fullname='" + worker.getFullName() + "')";
            ResultSet rsCount = stmt.executeQuery(sql);
            rsCount.next();
            if (rsCount.getInt(1) > 0) {
                result = true;
            }
            rsCount.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return result;

    }

    @Override
    public TimeJournal getJournal(Worker worker, Date dateFrom, Date dateTo) {
        TimeJournal result = new TimeJournal();
        if (!exist(worker)) return null;
        result.setWorker(worker);
        result.setDateFrom(dateFrom);
        result.setDateTo(dateTo);
        try {
            Statement stmt;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT id, type, milisec\n" +
                    "FROM public." + recordingTableName + "\n" +
                    "WHERE worker_id=" + worker.getId() +
                    " AND milisec >= " + dateFrom.getTime() +
                    " AND milisec < " + (dateTo.getTime() + 24 * 60 * 60 * 1000) + "\n" +
                    "ORDER BY milisec");

            while (rsCount.next()) {
                result.addRecord(rsCount.getInt("id"), new Date(rsCount.getLong("milisec")), rsCount.getBoolean("type"));
            }
            rsCount.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delRecord(String id) {
        if (isInt(id)) {
            return removeValue(recordingTableName, "id = " + id);
        } else {
            return false;
        }
    }

    @Override
    public boolean updateRecord(String id, Date date) {
        if (isInt(id)) {
            String[] columns = {"milisec"};
            Object[] values = {date.getTime()};
            return updateValue(recordingTableName, columns, values, "id = " + id);
        } else {
            return false;
        }
    }

    private String getFullNameById(int id) {
        String result = "";
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


    private int getIdByFullName(String fullName) {
        int result = -1;
        try {
            Statement stmt = null;
            stmt = connection.createStatement();
            ResultSet rsCount = stmt.executeQuery("SELECT id FROM public." + workersTableName + " WHERE fullName='" + fullName + "'");
            if (rsCount.next()) {
                result = rsCount.getInt("id");
            }
            rsCount.close();
        } catch (SQLException e) {
            //e.printStackTrace();
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
            if (stmt.executeUpdate(sql) > 0) result = true;
            stmt.close();

        } catch (SQLException e) {
            //e.printStackTrace();

        }
        return result;
    }

    private boolean addValue(String tableName, String[] columns, Object[] values) {
        boolean result = false;
        try {
            Statement stmt = connection.createStatement();

            String sql = "INSERT INTO public." + tableName + " (" + extractValues(columns, "") + ")" +
                    "VALUES (" + extractValues(values, "'") + ")";
            if (stmt.executeUpdate(sql) > 0) result = true;
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }

    private boolean updateValue(String tableName, String[] columns, Object[] values, String criteria) {
        boolean result = false;
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE public." + tableName + " SET " + extractSetValues(columns, values) +
                    " WHERE " + criteria;
            if (stmt.executeUpdate(sql) > 0) result = true;
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }

    private String extractSetValues(String[] columns, Object[] values) {
        String result = "";
        String framing = "'";
        for (int i = 0; i < values.length; i++) {
            result += columns[i] + " = " + framing + values[i] + framing;
            if (i < values.length - 1) {
                result += ", ";
            }
        }
        return result;
    }

    private String extractValues(Object[] values, String framing) {
        String result = "";
        for (int i = 0; i < values.length; i++) {
            result += framing + values[i] + framing;
            if (i < values.length - 1) {
                result += ", ";
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
