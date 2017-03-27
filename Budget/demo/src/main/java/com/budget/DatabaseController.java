package com.budget;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Maverick on 26.03.2017.
 *
 * Responsible for creating and maintaining connection to database.
 * It will log in to MySQL via hard-coded credentials.
 * It will create any necessary databases and tables if they do not exist.
 * It translates all function calls into MySQL queries for database and retrieves or sets data.
 */
public class DatabaseController
{
    private static final String USER_NAME = "budget";
    private static final String USER_PASSWORD = "koziadupa123";
    private static final String DATABASE_NAME = "budgetDB";
    private static final String DATABASE_SERVER = "localhost:3306";
    private static final String DATABASE_URL = "jdbc:mysql://" + DATABASE_SERVER + "/" + DATABASE_NAME;

    private Connection connection;

    // Creates and initializes connection to database.
    public DatabaseController()
    {
    }

    public boolean Initialize()
    {
        boolean ret = true;
        ret |= InitConnection();
        ret |= InitDatabase();

        return ret;
    }

    public boolean Shutdown()
    {
        return ShutdownConnection();
    }

    // Beware of light-hearted using this function, as it erases all existing data!
    public boolean Clear()
    {
        try
        {
            ExecSQL("drop table users;");
            ExecSQL("drop table earnings;");
            ExecSQL("drop table expenses;");

            return InitDatabase();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public User Register(String login, String password)
    {
        try
        {
            ExecSQL(
                    "insert into users " +
                    "(name, password) values " +
                    "('" + login + "', '" + password +"');"
            );

            return Login(login, password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public User Login(String login, String password)
    {
        try
        {
            ResultSet rs = ExecQuerySQL("select id from users where name = '"
                    + login + "' and password = '" + password + "';");

            if(rs.isBeforeFirst())
            {
                rs.first();
                Integer id = rs.getInt(1);
                return new User(id, login, password);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    private boolean InitConnection()
    {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(USER_NAME);
        dataSource.setPassword(USER_PASSWORD);
        dataSource.setURL(DATABASE_URL);


        try
        {
            connection = dataSource.getConnection();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean ShutdownConnection()
    {
        if(connection == null)
        {
            return false;
        }
        try
        {
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean InitDatabase()
    {
        if(connection == null)
        {
            return false;
        }

        try
        {
            ExecSQL("create table if not exists users (" +
                    "id int not null auto_increment, " +
                    "name varchar(32) not null, " +
                    "password varchar(32) not null, " +
                    "primary key(id)" +
                    ");");

            ExecSQL("create table if not exists earnings(" +
                    "id int not null auto_increment," +
                    "money bigint not null," +
                    "userID int not null," +
                    "timestamp datetime," +
                    "name varchar(32)," +
                    "primary key(id)" +
                    ");");

            ExecSQL("create table if not exists expenses(" +
                    "id int not null auto_increment," +
                    "money bigint not null," +
                    "userID int not null," +
                    "timestamp datetime," +
                    "name varchar(32)," +
                    "primary key(id)" +
                    ");");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void ExecSQL(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        statement.execute(sql);
    }


    private ResultSet ExecQuerySQL(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        return statement.executeQuery(sql);
    }
}
