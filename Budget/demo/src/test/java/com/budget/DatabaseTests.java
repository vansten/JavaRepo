package com.budget;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Maverick on 26.03.2017.
 */

public class DatabaseTests
{
    DatabaseController db;

    @Before
    public void Initialize()
    {
        db = new DatabaseController();
    }

    @Test
    public void TestConnection()
    {
        assertTrue(db.Initialize());
        assertTrue(db.Shutdown());
    }

    @Test
    public void TestClear()
    {
        assertTrue(db.Initialize());
        assertTrue(db.Clear());
        assertTrue(db.Shutdown());
    }

    @Test
    public void TestLoginEmpty()
    {
        // given
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        db.Initialize();
        db.Clear();

        // when
        User user = db.Login(userLogin, userPassword);

        // then
        assertTrue(user == null);

        db.Shutdown();
    }

    @Test
    public void TestRegister()
    {
        // given
        final String userLogin = "waclaw";
        final String userPassword = "silnehaslo1";
        db.Initialize();
        db.Clear();

        // when
        User user = db.Register(userLogin, userPassword);

        // then
        assertTrue(user != null);
        assertTrue(user.getLogin().equals(userLogin));
        assertTrue(user.getID() != User.INVALID_ID);

        db.Shutdown();
    }
}
