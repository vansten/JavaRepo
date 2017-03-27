package com.budget;

import com.budget.data.DatabaseController;
import com.budget.data.User;
import org.junit.*;

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

    @AfterClass
    public static void cleanup()
    {
        db.initialize();
        db.clear();
        db.shutdown();
    }
}
