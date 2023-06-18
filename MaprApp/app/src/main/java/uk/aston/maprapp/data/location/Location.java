package uk.aston.maprapp.data.location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;


@Entity(tableName = "location_table")
public class Location {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "location")
    private String mLocation;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;



    public Location(@NonNull String location, @NonNull String name) {
        this.mLocation = location;
        this.mName = name;
    }


    @Ignore
    public Location(int id, @NonNull String location, @NonNull String name) {
        this.id = id;
        this.mLocation = location;
        this.mName = name;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public String getName(){return this.mName;}

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }
}
