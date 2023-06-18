package uk.aston.maprapp.data.location;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationRepository {

    private LocationDao mLocationDao;
    private LiveData<List<Location>> mAllLocations;
    private LiveData<List<Location>> mAllLocationsOldest;
    private LiveData<List<Location>> mAllLocationsNewest;

    LocationRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        mLocationDao = db.locationDao();
        mAllLocations = mLocationDao.getAllLocations();
        mAllLocationsNewest = mLocationDao.getAllLocationsByNewest();
        mAllLocationsOldest = mLocationDao.getAllLocationsByOldest();
    }

    LiveData<List<Location>> getAllLocations() {
        return mAllLocations;
    }

    LiveData<List<Location>> getAllLocationsByOldest() {
        return mAllLocationsOldest;
    }

    LiveData<List<Location>> getAllLocationsByNewest() {
        return mAllLocationsNewest;
    }


    public void insert(Location location) {
        new insertAsyncTask(mLocationDao).execute(location);
    }

    public void update(Location location)  {
        new updateLocationAsyncTask(mLocationDao).execute(location);
    }

    public void deleteAll()  {
        new deleteAllLocationsAsyncTask(mLocationDao).execute();
    }


    public void deleteLocation(Location location) {
        new deleteLocationAsyncTask(mLocationDao).execute(location);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts a location
     */
    private static class insertAsyncTask extends AsyncTask<Location, Void, Void> {

        private LocationDao mAsyncTaskDao;

        insertAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all locations
     */
    private static class deleteAllLocationsAsyncTask extends AsyncTask<Void, Void, Void> {
        private LocationDao mAsyncTaskDao;

        deleteAllLocationsAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes single location.
     */
    private static class deleteLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDao mAsyncTaskDao;

        deleteLocationAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            mAsyncTaskDao.deleteLocation(params[0]);
            return null;
        }
    }

    /**
     *  Updates a location.
     */
    private static class updateLocationAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDao mAsyncTaskDao;

        updateLocationAsyncTask(LocationDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Location... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
