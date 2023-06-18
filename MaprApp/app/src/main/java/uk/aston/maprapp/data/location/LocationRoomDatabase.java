package uk.aston.maprapp.data.location;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

@Database(entities = {Location.class}, version = 2, exportSchema = false)
public abstract class LocationRoomDatabase extends RoomDatabase {

    public abstract LocationDao locationDao();

    private static LocationRoomDatabase INSTANCE;

    public static LocationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationRoomDatabase.class, "location_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // called when database opened.
    // use to populate database
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database
    // only if no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final LocationDao mDao;

        // Initial data set
        private static String[] names = {"Home", "Theater", "Shop", "Mother", "Holiday",
                "First Date"};
        private static String[] locations = {"20 King Street\n" + "BRIGHTON\n" + "BN76 0GY",
                "53 Park Road\n" + "DARLINGTON\n" + "DL88 2LZ",
                "40 Kingsway\n" + "TELFORD\n" + "TF31 9US",
                "31 York Road\n" + "WAKEFIELD\n" + "WF35 8AE",
                "11 School Lane\n" + "CREWE\n" + "CW42 0HF",
                "292 Queens Road\n" + "CARLISLE\n" + "CA47 6WD"};

        PopulateDbAsync(LocationRoomDatabase db) {
            mDao = db.locationDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If no locations, create  list of locations.
            if (mDao.getAnyLocation().length < 1) {
                for (int i = 0; i <= names.length - 1; i++) {
                    Location location = new Location(locations[i], names[i]);
                    mDao.insert(location);
                }
            }
            return null;
        }
    }
}

