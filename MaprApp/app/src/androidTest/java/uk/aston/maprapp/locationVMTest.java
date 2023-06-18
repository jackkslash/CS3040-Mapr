package uk.aston.maprapp;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import uk.aston.maprapp.data.location.LocationDao;
import uk.aston.maprapp.data.location.LocationRoomDatabase;

@RunWith(AndroidJUnit4.class)
public class locationVMTest {
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


}
