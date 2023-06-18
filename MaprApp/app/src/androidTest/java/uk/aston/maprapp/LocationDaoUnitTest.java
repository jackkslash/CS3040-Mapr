package uk.aston.maprapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import uk.aston.maprapp.data.location.Location;
import uk.aston.maprapp.data.location.LocationDao;
import uk.aston.maprapp.data.location.LocationRoomDatabase;
import uk.aston.maprapp.data.location.LocationViewModel;

@RunWith(AndroidJUnit4.class)
public class LocationDaoUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LocationRoomDatabase mDb;
    private LocationDao mLocationDao;


    @Before
    public void dbCreate(){
        Context ctx = ApplicationProvider.getApplicationContext();

        mDb = Room.inMemoryDatabaseBuilder(ctx, LocationRoomDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();
        mLocationDao = mDb.locationDao();

    }

    @After
    public void closeDb() {
        mDb.close();
    }


    @Test
    public void insertLocationAndReturnIt() throws Exception {
        Location location = new Location("Name", "Address");
        mLocationDao.insert(location);
        List<Location> allLocations = LiveDataTestUtil.getValue(mLocationDao.getAllLocations());
        assertEquals(allLocations.get(0).getLocation(), location.getLocation());
    }

    @Test
    public void insertLocationsAndReturnAll() throws Exception {
        Location location = new Location("Address", "Name");
        mLocationDao.insert(location);
        Location location2 = new Location("Address2", "Name2");
        mLocationDao.insert(location2);
        List<Location> allLocations = LiveDataTestUtil.getValue(mLocationDao.getAllLocations());
        assertEquals(allLocations.size(), 2);
    }



    @Test
    public void deleteSpecificLocation() throws Exception {
        Location location = new Location("Address", "Name");
        mLocationDao.insert(location);
        Location location2 = new Location("Address2", "Name2");
        mLocationDao.insert(location2);
        Location location3 = new Location("Address3", "Name3");
        mLocationDao.insert(location3);
        List<Location> allLocations = LiveDataTestUtil.getValue(mLocationDao.getAllLocations());
        mLocationDao.deleteLocation(location2);
        List<Location> alldelete = LiveDataTestUtil.getValue(mLocationDao.getAllLocations());
        assertNotEquals(allLocations, alldelete);
        assertEquals(alldelete.get(2).getLocation(),location3.getLocation());
    }

    @Test
    public void deleteLocationsAndReturnEmpty() throws Exception {
        Location location = new Location("Address", "Name");
        mLocationDao.insert(location);
        Location location2 = new Location("Address2", "Name2");
        mLocationDao.insert(location2);
        mLocationDao.deleteAll();
        List<Location> allLocs = LiveDataTestUtil.getValue(mLocationDao.getAllLocations());
        assertTrue(allLocs.isEmpty());
    }
}