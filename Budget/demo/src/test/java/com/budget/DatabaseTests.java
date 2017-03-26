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
}
