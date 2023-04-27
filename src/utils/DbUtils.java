package utils;

import model.Driver;
import model.Manager;
import model.User;

import java.sql.*;

public class DbUtils {
    public static Connection connectToDb() {
        Connection conn = null;
        String DB_URL = "jdbc:mysql://localhost/transport_system";
        String USER = "root";
        String PASS = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static void disconnect(Connection connection, Statement statement) {
        try {
            if (connection != null && statement != null) {
                statement.close();
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    //public static boolean validateUser(String login, String password) throws SQLException {
    //    boolean userExists = false;
    //    Connection connection = connectToDb();
    //    String selectDriver = "SELECT count(*) FROM drivers WHERE login = ? AND password = ? UNION SELECT count(*) FROM managers WHERE login = ? AND password = ?";
    //    PreparedStatement preparedStatement = connection.prepareStatement(selectDriver);
    //    preparedStatement.setString(1, login);
    //    preparedStatement.setString(2, password);
    //    preparedStatement.setString(3, login);
    //    preparedStatement.setString(4, password);
    //    ResultSet rs = preparedStatement.executeQuery();
    //    while (rs.next()) {
    //        if (rs.getInt(1) != 0) userExists = true;
    //    }
    //    return userExists;
    //}


    public static User validateUser(String login, String password, boolean isManager) throws SQLException {
        Connection connection = connectToDb();
        String sql;
        if (isManager) sql = "SELECT * FROM managers WHERE login = ? AND password = ?";
        else sql = "SELECT * FROM drivers WHERE login = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();

        //Optimize later

        if (isManager) {
            Manager manager = null;
            while (rs.next()) {
                manager = new Manager(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("name"),
                        rs.getString("surname"), rs.getDate("birth_date").toLocalDate(), rs.getString("phone_num"),
                        rs.getString("email"), rs.getDate("employment_date").toLocalDate(), rs.getBoolean("is_admin"));
            }
            disconnect(connection, preparedStatement);
            return manager;
        } else {
            Driver driver = null;
            while (rs.next()) {
                driver = new Driver(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("name"),
                        rs.getString("surname"), rs.getDate("birth_date").toLocalDate(), rs.getString("phone_num"),
                        rs.getDate("med_date").toLocalDate(), rs.getString("med_number"), rs.getString("driver_license"));
            }
            disconnect(connection, preparedStatement);
            return driver;
        }
    }

}





