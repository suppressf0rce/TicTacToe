package control.database;

import java.sql.*;

/**
 * This class handles the controls of the MySQL database<br>
 * Example use:<br>
 * <code>
 * DBBroker broker = new DBBroker();
 * broker.openConnection();
 * broker.executeUpdate(query);
 * broker.closeConnection();
 * </code>
 * Created by SuppresSForcE on 04/09/
 *
 * @see #openConnection()
 * @see #closeConnection()
 * @see #executeUpdate(String)
 * @see #getData(String)
 * @see #testConnection()
 */
public class DBBroker {


    //Variables
    //------------------------------------------------------------------------------------------------------------------
    private static String HOST = "localhost";
    private String PORT = "3306";
    private String USERNAME = "root";
    private String PASSWORD = "";
    private String DATABASE = "";

    Connection connection = null;

    private Exception lastException;


    //Methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * This method opens connection to the Database if connection doesn't exist or is  closed
     */
    public void openConnection() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
            } else {
                if (connection.isClosed())
                    connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Could not open Databasae connection: " + e.getMessage());
            lastException = e;
        }
    }

    /**
     * This method is used for getting data from the database<br>
     * To use this method you musf firstly open connection to the database<br>
     * After completing the database task connection must be closed!
     *
     * @param query an <code>String</code> with query that contains query for getting the data
     * @return an <code>Result set of data</code>
     * @see #openConnection();
     * @see #closeConnection()
     */
    public ResultSet getData(String query) {
        try {

            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(query);
            connection.commit();

            return rs;

        } catch (SQLException e) {
            System.out.println("Could not execute query Get Data: " + e.getMessage());
            lastException = e;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return null;
    }

    /**
     * This method is used when you want to make changes to the database<br>
     * To use this method you must firstly open connection to the database<br>
     * After completing the database task connection must be closed!
     *
     * @param query an <code>String</code> that contains the update query
     * @see #openConnection()
     * @see #closeConnection()
     */
    public void executeUpdate(String query) {
        try {
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

            connection.commit();

        } catch (SQLException e) {
            System.out.println("Could not execute query: " + e.getMessage());
            lastException = e;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * This method checks whether connection is open and closes it accordingly
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                if (!connection.isClosed())
                    connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close connection: " + e.getMessage());
            lastException = e;
        }
    }

    /**
     * This method tests if the connection is successful or not
     *
     * @return true if successful and false if not
     */
    public boolean testConnection() {
        try {
            openConnection();


            if(connection == null)
                return false;

            boolean isValid = connection.isValid(10);//10 seconds timeout
            return isValid;
        } catch (SQLException e) {
            return false;
        } finally {
            closeConnection();
        }
    }


    //Getters & Setters
    //------------------------------------------------------------------------------------------------------------------
    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        DBBroker.HOST = HOST;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getDATABASE() {
        return DATABASE;
    }

    public void setDATABASE(String DATABASE) {
        this.DATABASE = DATABASE;
    }

    public Exception getLastException() {
        return lastException;
    }
}