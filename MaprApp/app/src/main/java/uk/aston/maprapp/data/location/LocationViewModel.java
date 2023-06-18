package uk.aston.maprapp.data.location;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class LocationViewModel extends AndroidViewModel {

    private LocationRepository mRepository;
    private LiveData<List<Location>> mAllLocations;
    private LiveData<List<Location>> mAllLocationsOldest;
    private LiveData<List<Location>> mAllLocationsNewest;

    public LocationViewModel(Application application) {
        super(application);
        mRepository = new LocationRepository(application);
        mAllLocations = mRepository.getAllLocations();
        mAllLocationsNewest = mRepository.getAllLocationsByNewest();
        mAllLocationsOldest = mRepository.getAllLocationsByOldest();
    }


    public LiveData<List<Location>> getAllLocations() {
        return mAllLocations;
    }

    public LiveData<List<Location>> getAllLocationsByNewest() {
        return mAllLocationsNewest;
    }

    public LiveData<List<Location>> getAllLocationsByOldest() {
        return mAllLocationsOldest;
    }

    public void insert(Location location) {
        mRepository.insert(location);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteLocation(Location location) {
        mRepository.deleteLocation(location);
    }

    public void update(Location location) {
        mRepository.update(location);
    }
}