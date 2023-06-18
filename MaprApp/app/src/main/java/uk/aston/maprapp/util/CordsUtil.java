package uk.aston.maprapp.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CordsUtil {

    private Context mContext;


    public CordsUtil(Context applicationContext) {
        mContext = applicationContext;
    }

    public String getAddressFromCord(Context context, Double log, Double lat){

        String addressStr = "";
        try {
            Geocoder myLocation = new Geocoder(context, Locale.getDefault());
            List<Address> myList = myLocation.getFromLocation(log,lat, 1);
            Address address = (Address) myList.get(0);
            addressStr += address.getAddressLine(0);

        }catch (IOException ex){
            ex.printStackTrace();
        }

        return addressStr;

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng cord = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() == 0) {
                if (address.isEmpty()) {
                    Log.e("empty", ""+ address);
                }
            }

            Address location = address.get(0);
            cord = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IndexOutOfBoundsException | IOException ex) {

            ex.printStackTrace();
        }

        return cord;
    }

}
