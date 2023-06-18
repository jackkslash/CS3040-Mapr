package uk.aston.maprapp.ui.map;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ThreadLocalRandom;

import uk.aston.maprapp.R;


import uk.aston.maprapp.util.CordsUtil;
import uk.aston.maprapp.databinding.FragmentMapBinding;



public class MapFragment extends Fragment implements SensorEventListener{


    private FragmentMapBinding binding;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable, notFirstTime = false;
    private float lastXPos;
    private float lastYPos;
    private float lastZPos;
    private GoogleMap mMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        Bundle mapBundle = this.getArguments();


        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.clear();
            LatLng startPos = new LatLng(52,-1);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    startPos, 7
            ));


             Log.d("IN IF", "" + mapBundle);
            if (!mapBundle.isEmpty()) {

                try {
                    Log.d("IN IF", "" + mapBundle);
                    MarkerOptions m = new MarkerOptions();
                    m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


                    LatLng latLng = new LatLng(mapBundle.getDouble("Latitude"), mapBundle.getDouble("Longitude"));
                    m.position(latLng);

                    Double lat = mapBundle.getDouble("Latitude");
                    Double log = mapBundle.getDouble("Longitude");
                    String name = mapBundle.getString("Name");


                    String address = new CordsUtil(getContext()).getAddressFromCord(getContext(), lat, log);

                    m.title(name);
                    m.snippet(address);
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            latLng, 10
                    ));
                    mMap.addMarker(m);
                } catch (IndexOutOfBoundsException | NullPointerException exception) {
                    exception.printStackTrace();
                }
            }

            googleMap.setOnMapClickListener(latLng -> {

                MarkerOptions m = new MarkerOptions();
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                m.position(latLng);

                Double latDub = latLng.latitude;
                Double longDub = latLng.longitude;

                String address = new CordsUtil(getContext()).getAddressFromCord(getContext(), latDub, longDub);

                m.title(address);
                m.snippet(getString(R.string.addLocationMarker));

                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng, 10
                ));

                mMap.setOnInfoWindowClickListener(marker -> {
                    Bundle bundle = new Bundle();

                    bundle.putString("Name", "");
                    bundle.putString("Location", address);

                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_map_to_updateLocation, bundle);
                });
                mMap.addMarker(m);
            });


        });


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        } else {
            isAccelerometerSensorAvailable = false;
        }

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentX = sensorEvent.values[0];
        float currentY = sensorEvent.values[1];
        float currentZ = sensorEvent.values[2];

        if (notFirstTime) {
            float xDiff = Math.abs(lastXPos - currentX);
            float yDiff = Math.abs(lastYPos - currentY);
            float zDiff = Math.abs(lastZPos - currentZ);

            float shakeTolerance = 9f;
            if ((xDiff > shakeTolerance && yDiff > shakeTolerance) ||
                    (xDiff > shakeTolerance && zDiff > shakeTolerance) ||
                    (yDiff > shakeTolerance && zDiff > shakeTolerance)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    MarkerOptions m = new MarkerOptions();
                    m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    double bboxLat = ThreadLocalRandom.current().nextDouble(49, 59);
                    double bboxLng = ThreadLocalRandom.current().nextDouble(-6, 1.6);
                    LatLng bboxLL = new LatLng(bboxLat, bboxLng);

                    m.position(bboxLL);
                    m.title(bboxLL.latitude + " ; " + bboxLL.longitude);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            bboxLL, 15
                    ));

                }
            }

        }

        lastXPos = currentX;
        lastYPos = currentY;
        lastZPos = currentZ;
        notFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAccelerometerSensorAvailable)
            sensorManager.registerListener(this, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isAccelerometerSensorAvailable)
            sensorManager.unregisterListener(this);
    }
}
