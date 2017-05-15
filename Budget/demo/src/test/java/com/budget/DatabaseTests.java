package com.budget;

import com.budget.data.DatabaseController;
import com.budget.data.Earning;
import com.budget.data.Expense;
import com.budget.data.User;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by Maverick on 26.03.2017.
 */

public class DatabaseTests {
    static DatabaseController db;

    @BeforeClass
    public static void initialize()
    {
        db = new DatabaseController();
    }

    @Test
    public void testConnection() {
        assertTrue(db.initialize());
        assertTrue(db.shutdown());
    }

    @Test
    public void testClear() {
        assertTrue(db.initialize());
        assertTrue(db.clear());
        assertTrue(db.shutdown());
    }

    @Test
    public void testLoginEmpty() {
        // given
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        db.initialize();
        db.clear();

        // when
        User user = db.loginUser(userLogin, userPassword);

        // then
        assertTrue(user == null);

        db.shutdown();
    }

    @Test
    public void testRegister() {
        // given
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        db.initialize();
        db.clear();

        // when
        User user = db.registerUser(userLogin, userPassword);

        // then
        assertTrue(user != null);
        assertTrue(user.getLogin().equals(userLogin));
        assertTrue(user.getID() != User.INVALID_ID);

        db.shutdown();
    }

    @Test
    public void testCreateExpense() {
        // given
        db.initialize();
        db.clear();
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        User user = db.registerUser(userLogin, userPassword);

        // when
        Expense exp = db.createExpense(user, 10.0f, "Bread", "Shopping");

        // then
        assertTrue(exp != null);
        assertTrue(exp.getID() != 0);
        assertTrue(exp.getUserID().equals(user.getID()));
        assertTrue(exp.getName().equals("Bread"));
        assertTrue(exp.getSourceName().equals("Shopping"));
        assertTrue(exp.getValue().equals(10.0f));
        assertTrue(exp.getValueAbsolute().equals(-10.0f));

        db.shutdown();
    }

    @Test
    public void testCreateEarning() {
        // given
        db.initialize();
        db.clear();
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        User user = db.registerUser(userLogin, userPassword);

        // when
        Earning ear = db.createEarning(user, 500.49f, "Drugs", "Selling");

        // then
        assertTrue(ear != null);
        assertTrue(ear.getID() != 0);
        assertTrue(ear.getUserID().equals(user.getID()));
        assertTrue(ear.getName().equals("Drugs"));
        assertTrue(ear.getSourceName().equals("Selling"));
        assertTrue(ear.getValue().equals(500.49f));
        assertTrue(ear.getValueAbsolute().equals(500.49f));

        db.shutdown();
    }

    @Test
    public void testGetFromDatabase() {
        // given
        db.initialize();
        initDatabase();

        // when
        ArrayList<Object> earnNames = db.getData(DatabaseController.EarningDatatype.NAME.ordinal(),
                DatabaseController.Database.EARNINGS);
        ArrayList<Object> expsValues = db.getData(
                DatabaseController.ExpenseDatatype.VALUE.ordinal(), DatabaseController.Database.EXPENSES);

        // then
        assertTrue(earnNames.size() == 3);
        assertTrue(((String)earnNames.get(0)).equals("Work"));
        assertTrue(((String)earnNames.get(1)).equals("Work"));
        assertTrue(((String)earnNames.get(2)).equals("Drugs"));

        assertTrue(expsValues.size() == 3);
        assertTrue(((Float)expsValues.get(0)).equals(100.0f));
        assertTrue(((Float)expsValues.get(1)).equals(50.0f));
        assertTrue(((Float)expsValues.get(2)).equals(1000.0f));

        db.shutdown();
    }

    @Test
    public void testGetFromDatabaseConditional() {
        // given
        db.initialize();
        initDatabase();

        // when
        ArrayList<Object> earnValues = db.getDataConditional(DatabaseController.EarningDatatype.VALUE.ordinal(),
                DatabaseController.Database.EARNINGS, DatabaseController.EarningDatatype.NAME.ordinal(),
                DatabaseController.Condition.EQUAL, "Work");
        ArrayList<Object> expsNames = db.getDataConditional(DatabaseController.ExpenseDatatype.NAME.ordinal(),
                DatabaseController.Database.EXPENSES, DatabaseController.ExpenseDatatype.SOURCENAME.ordinal(),
                DatabaseController.Condition.NOT_EQUAL, "Shopping");
        ArrayList<Object> nameValues = db.getDataConditional(DatabaseController.EarningDatatype.NAME.ordinal(),
                DatabaseController.Database.EARNINGS, DatabaseController.EarningDatatype.VALUE.ordinal(),
                DatabaseController.Condition.EQUAL, 500.0f);

        // then
        assertTrue(earnValues.size() == 2);
        assertTrue(((Float)earnValues.get(0)) == 5000.0f);
        assertTrue(((Float)earnValues.get(1)) == 1000.0f);

        assertTrue(expsNames.size() == 2);
        assertTrue(((String)expsNames.get(0)).equals("Petrol"));
        assertTrue(((String)expsNames.get(1)).equals("Flywheel"));

        assertTrue(nameValues.size() == 1);
        assertTrue(((String)nameValues.get(0)).equals("Drugs"));

        db.shutdown();
    }

    @Test
    public void testGetAllEarnings() {
        //given
        db.initialize();
        initDatabase();

        // when
        ArrayList<Earning> earns = db.getAllEarnings();

        // then
        assertTrue(earns.size() == 3);
        assertTrue(earns.get(0).getValue() == 5000.0f);
        assertTrue(earns.get(1).getName().equals("Work"));
        assertTrue(earns.get(2).getSourceName().equals("Selling"));

        db.shutdown();
    }

    @Test
    public void testGetAllExpenses() {
        //given
        db.initialize();
        initDatabase();

        // when
        ArrayList<Expense> exps = db.getAllExpenses();

        // then
        assertTrue(exps.size() == 3);
        assertTrue(exps.get(0).getValue() == 100.0f);
        assertTrue(exps.get(1).getName().equals("Petrol"));
        assertTrue(exps.get(2).getSourceName().equals("Car"));

        db.shutdown();
    }

    @Test
    public void testGetExpensesForUser() {
        // given
        db.initialize();
        User usr = initDatabase();

        // when
        ArrayList<Expense> exps = db.getExpensesForUser(usr);

        // then
        assertTrue(exps.size() == 2);
        assertTrue(exps.get(0).getUserID() == usr.getID());
        assertTrue(exps.get(1).getUserID() == usr.getID());

        db.shutdown();
    }

    @Test
    public void testGetEarningsForUser() {
        // given
        db.initialize();
        User usr = initDatabase();

        // when
        ArrayList<Earning> earns = db.getEarningsForUser(usr);

        // then
        assertTrue(earns.size() == 1);
        assertTrue(earns.get(0).getUserID() == usr.getID());

        db.shutdown();
    }

    @Test
    public void testRemoveUser() {
        // given
        db.initialize();
        User usr = initDatabase();

        // whenthen
        assertTrue(db.removeUser(usr));

        db.shutdown();
    }

    @Test
    public void testRemoveExpense() {
        // given
        db.initialize();
        initDatabase();

        // whenthen
        assertTrue(db.removeExpense(1));

        db.shutdown();
    }

    @Test
    public void testRemoveEarning() {
        // given
        db.initialize();
        initDatabase();

        // whenthen
        assertTrue(db.removeEarning(1));

        db.shutdown();
    }

    @AfterClass
    public static void cleanup()
    {
        db.initialize();
        db.clear();
        db.shutdown();
    }


    private User initDatabase()
    {
        db.clear();

        final String userLoginA = "waclaw";
        final String userPasswordA = "silnehaslo1";
        final String userLoginB = "konrad";
        final String userPasswordB = "1haslosilne";

        User usr1 = db.registerUser(userLoginA, userPasswordA);
        User usr2 = db.registerUser(userLoginB, userPasswordB);

        Expense exp1 = db.createExpense(usr1, 100.0f, "Bread", "Shopping");
        Expense exp2 = db.createExpense(usr1, 50.0f, "Petrol", "Car");
        Expense exp3 = db.createExpense(usr2, 1000.0f, "Flywheel", "Car");

        Earning ear1 = db.createEarning(usr1, 5000.0f, "Work", "Vescom");
        Earning ear2 = db.createEarning(usr2, 1000.0f, "Work", "Teyon");
        Earning ear3 = db.createEarning(usr2, 500.0f, "Drugs", "Selling");

        return usr1;
    }
}
