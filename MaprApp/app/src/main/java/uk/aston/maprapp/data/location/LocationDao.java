package uk.aston.maprapp.data.location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;


import java.util.List;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Location location);

    @Query("DELETE FROM location_table")
    void deleteAll();

    @Delete
    void deleteLocation(Location location);

    @Query("SELECT * from location_table LIMIT 1")
    Location[] getAnyLocation();

    @Query("SELECT * from location_table ORDER BY name ASC")
    LiveData<List<Location>> getAllLocations();

    @Query("SELECT * from location_table ORDER BY id ASC")
    LiveData<List<Location>> getAllLocationsByOldest();

    @Query("SELECT * from location_table ORDER BY id DESC")
    LiveData<List<Location>> getAllLocationsByNewest();

    @Update
    void update(Location... location);
}

