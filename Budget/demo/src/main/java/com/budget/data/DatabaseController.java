package com.budget.data;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;

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
    public enum Database
    {
       USERS, EARNINGS, EXPENSES;
    }

    public enum UserDatatype
    {
        ID, LOGIN, PASSWORD;
    }

    public enum EarningDatatype
    {
        ID, VALUE, USERID, TIMESTAMP, NAME, SOURCENAME;
    }

    public enum ExpenseDatatype
    {
        ID, VALUE, USERID, TIMESTAMP, NAME, SOURCENAME;
    }


    private static final String USER_NAME = "budget";
    private static final String USER_PASSWORD = "koziadupa123";
    private static final String DATABASE_NAME = "budgetdb";
    private static final String DATABASE_SERVER = "localhost:3306";
    private static final String DATABASE_URL = "jdbc:mysql://" + DATABASE_SERVER + "/" + DATABASE_NAME;

    private Connection connection;

    private ArrayList<User> loggedInUsers = new ArrayList<>();


    // Creates and initializes connection to database.
    public DatabaseController() {}

    public boolean initialize() {
        boolean ret = true;
        ret |= initConnection();
        ret |= initDatabase();

        return ret;
    }

    public boolean shutdown()
    {
        return shutdownConnection();
    }

    // Beware of light-hearted using this function, as it erases all existing data!
    public boolean clear() {
        try
        {
            execSQL("drop table " + Database.USERS.name() + ";");
            execSQL("drop table " + Database.EARNINGS.name() + ";");
            execSQL("drop table " + Database.EXPENSES.name() + ";");

            return initDatabase();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public User registerUser(String login, String password) {
        try
        {
            execSQL(
                    "insert into " + Database.USERS.name() + " " +
                    "(login, password) values " +
                    "('" + login + "', '" + password +"');"
            );

            return loginUser(login, password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public User loginUser(String login, String password) {
        try
        {
            ResultSet rs = execQuerySQL("select id from " + Database.USERS.name() + " where login = '"
                    + login + "' and password = '" + password + "';");

            if(rs.isBeforeFirst())
            {
                rs.first();
                Integer id = rs.getInt(1);
                User usr = new User(id, login, password);
                loggedInUsers.add(usr);
                return usr;
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

    public void logout(User user) {
        loggedInUsers.remove(user);
    }

    public boolean removeUser(User user) {
        return removeUser(user.id);
    }

    public boolean removeUser(Integer id) {
        return removeFromDatabaseInternal(id, Database.USERS.name());
    }

    public Expense createExpense(User user, Float value, String name, String sourceName) {
        try
        {
            Instant timestamp = Instant.now();
            Integer userID = user.getID();
            String sql =
                    "insert into " + Database.EXPENSES.name() + " values " +
                    "(" +
                    value.toString() + ", " +
                    userID.toString() + ", " +
                    timestamp.toString() + ", " +
                    name + ", " +
                    sourceName +
                    ")";

            Statement insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insert.executeUpdate("");

            ResultSet rs = insert.getGeneratedKeys();
            if(rs.next())
            {
                Integer newID = rs.getInt(1);
                return new Expense(newID, value, userID, timestamp, name, sourceName);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Earning createEarning(User user, Float value, String name, String sourceName) {
        try
        {
            Instant timestamp = Instant.now();
            Integer userID = user.getID();
            String sql =
                    "insert into " + Database.EARNINGS.name() + " values " +
                            "(" +
                            value.toString() + ", " +
                            userID.toString() + ", " +
                            timestamp.toString() + ", " +
                            name + ", " +
                            sourceName +
                            ")";

            Statement insert = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insert.executeUpdate("");

            ResultSet rs = insert.getGeneratedKeys();
            if(rs.next())
            {
                Integer newID = rs.getInt(1);
                return new Earning(newID, value, userID, timestamp, name, sourceName);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeExpense(Expense expense) {
        return removeExpense(expense.getID());
    }

    public boolean removeEarning(Earning earning) {
        return removeEarning(earning.getID());
    }

    public boolean removeExpense(Integer id) {
        return removeFromDatabaseInternal(id, Database.EXPENSES.name());
    }

    public boolean removeEarning(Integer id) {
        return removeFromDatabaseInternal(id, Database.EARNINGS.name());
    }

    public final ArrayList<User> getLoggedUsers() {
        return loggedInUsers;
    }

    public final ArrayList<Object> getFromDatabase(UserDatatype datatype, Database database) {
        return getFromDatabaseInternal(datatype.name(), database.name());
    }

    public final ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> objects = new ArrayList<>();
        try
        {
            ResultSet rs = execQuerySQL("select * from " + Database.EXPENSES.name() + ";");
            loadExpensesFromResultSet(rs, objects);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return objects;
    }

    public final ArrayList<Earning> getAllEarnings() {
        ArrayList<Earning> objects = new ArrayList<>();
        try
        {
            ResultSet rs = execQuerySQL("select * from " + Database.EARNINGS.name() + ";");
            loadEarningsFromResultSet(rs, objects);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return objects;
    }

    // Entries are stored in a following order: first earnings, then expenses.
    public final ArrayList<Entry> getAllEntries() {
        ArrayList<Entry> objects = new ArrayList<>();
        ArrayList<Expense> expenses = getAllExpenses();
        ArrayList<Earning> earnings = getAllEarnings();
        objects.addAll(earnings);
        objects.addAll(expenses);

        return objects;
    }

    public final ArrayList<Expense> getExpensesForUser(User user) {
        ArrayList<Expense> objects = new ArrayList<>();
        try
        {
            ResultSet rs = execQuerySQL("select * from " + Database.EXPENSES.name() +
                    " where " + ExpenseDatatype.USERID.name() + " = " + user.getID().toString() + ";" );
            loadExpensesFromResultSet(rs, objects);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return objects;
    }

    public final ArrayList<Earning> getEarningsForUser(User user) {
        ArrayList<Earning> objects = new ArrayList<>();
        try
        {
            ResultSet rs = execQuerySQL("select * from " + Database.EARNINGS.name() +
                    " where " + ExpenseDatatype.USERID.name() + " = " + user.getID().toString() + ";" );
            loadEarningsFromResultSet(rs, objects);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return objects;
    }

    public final ArrayList<Entry> getEntriesForUser(User user) {
        ArrayList<Entry> objects = new ArrayList<>();
        ArrayList<Expense> expenses = getExpensesForUser(user);
        ArrayList<Earning> earnings = getEarningsForUser(user);
        objects.addAll(earnings);
        objects.addAll(expenses);

        return objects;
    }

    // This function takes only logged in users into account.
    // Returns null if none is found.
    public User getUserForEntry(Entry entry) {
        for(User user : loggedInUsers)
        {
            if(user.getID().equals(entry.getUserID()))
            {
                return user;
            }
        }
        return null;
    }

    private boolean initConnection() {
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

    private boolean shutdownConnection() {
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

    private boolean initDatabase() {
        if(connection == null)
        {
            return false;
        }

        try
        {
            execSQL("create table if not exists " + Database.USERS.name() + " (" +
                    UserDatatype.ID.name() + " int not null auto_increment, " +
                    UserDatatype.LOGIN.name() + " varchar(32) not null, " +
                    UserDatatype.PASSWORD.name() + " varchar(32) not null, " +
                    "primary key(" + UserDatatype.ID.name() + ")" +
                    ");");

            execSQL("create table if not exists " + Database.EARNINGS.name() + "(" +
                    EarningDatatype.ID.name() + " int not null auto_increment, " +
                    EarningDatatype.VALUE.name() + " double not null, " +
                    EarningDatatype.USERID.name() + " int not null, " +
                    EarningDatatype.TIMESTAMP.name() + " datetime, " +
                    EarningDatatype.NAME.name() + " varchar(32)," +
                    EarningDatatype.SOURCENAME.name() + " varchar(32)," +
                    "primary key(" + EarningDatatype.ID.name() + ")" +
                    ");");

            execSQL("create table if not exists " + Database.EXPENSES.name() + "(" +
                    ExpenseDatatype.ID.name() + " int not null auto_increment," +
                    ExpenseDatatype.VALUE.name() + " double not null," +
                    ExpenseDatatype.USERID.name() + " int not null," +
                    ExpenseDatatype.TIMESTAMP.name() + " datetime," +
                    ExpenseDatatype.NAME.name() + "  varchar(32)," +
                    ExpenseDatatype.SOURCENAME.name() + "  varchar(32)," +
                    "primary key(" + ExpenseDatatype.ID.name() + ")" +
                    ");");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean removeFromDatabaseInternal(Integer id, String tableName) {
        try
        {
            execSQL("delete from " + tableName + " where id " +
                    " = " + id.toString() + ";");
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private final ArrayList<Object> getFromDatabaseInternal(String colName, String tableName) {
        ArrayList<Object> logins = new ArrayList<>();
        try
        {
            ResultSet rs = execQuerySQL("select " + colName + " from " + tableName + ";");
            while (rs.next())
            {
                logins.add(rs.getObject(1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return logins;
    }

    private void loadEarningsFromResultSet(ResultSet rs, ArrayList<Earning> outObjects) throws SQLException
    {
        while(rs.next())
        {
            outObjects.add(new Earning(
                    rs.getInt(1),
                    rs.getFloat(2),
                    rs.getInt(3),
                    rs.getDate(4).toInstant(),
                    rs.getString(5),
                    rs.getString(6)
            ));
        }
    }

    private void loadExpensesFromResultSet(ResultSet rs, ArrayList<Expense> outObjects) throws SQLException
    {
        while(rs.next())
        {
            outObjects.add(new Expense(
                    rs.getInt(1),
                    rs.getFloat(2),
                    rs.getInt(3),
                    rs.getDate(4).toInstant(),
                    rs.getString(5),
                    rs.getString(6)
            ));
        }
    }

    private void execSQL(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        statement.execute(sql);
    }


    private ResultSet execQuerySQL(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.closeOnCompletion();
        return statement.executeQuery(sql);
    }
}
