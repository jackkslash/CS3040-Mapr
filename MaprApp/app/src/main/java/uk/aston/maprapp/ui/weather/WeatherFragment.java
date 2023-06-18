package uk.aston.maprapp.ui.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import uk.aston.maprapp.R;
import uk.aston.maprapp.databinding.FragmentWeatherBinding;
import uk.aston.maprapp.util.CordsUtil;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private TextView mainTemp;
    private TextView feelsLikeTemp;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView windSpeed;
    private ImageView weatherImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button backPageButton = root.findViewById(R.id.backPageButton);
        mainTemp = root.findViewById(R.id.textTemp);
        feelsLikeTemp = root.findViewById(R.id.textFeelsTemp);
        maxTemp = root.findViewById(R.id.textMaxTemp);
        minTemp = root.findViewById(R.id.textMinTemp);
        windSpeed = root.findViewById(R.id.textWindSpeed);
        weatherImage = root.findViewById(R.id.weatherImage);

        Bundle bundle = this.getArguments();
        String mLocation = bundle.getString("Location");

        LatLng loglat = new CordsUtil(getContext())
                .getLocationFromAddress(getContext(),mLocation);

        if (loglat == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.weatherTitle))
                    .setMessage(R.string.weatherError)
                    .setNegativeButton(R.string.OKAlert, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Navigation.findNavController(getView()).popBackStack();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }else {
            String log = String.valueOf(loglat.longitude);
            String lat = String.valueOf(loglat.latitude);
            String API_KEY = getString(R.string.openweather_api_key);


            Uri buildUrl = Uri.parse("https://api.openweathermap.org/data/2.5/weather?")
                    .buildUpon()
                    .appendQueryParameter("lat", lat)
                    .appendQueryParameter("lon", log)
                    .appendQueryParameter("appid", API_KEY)
                    .build();

            Log.d("REQ", "!: Started ");
            String myUrl = buildUrl.toString();
            Log.d("LINK", "" + myUrl);

            StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                    response -> {
                        try {
                            //Create a JSON object containing information from the API.
                            JSONObject myJsonObject = new JSONObject(response);
                            Log.d("REQ", "!: " + myJsonObject.get("main"));
                            JSONObject mainTemps = myJsonObject.getJSONObject("main");
                            JSONObject wind = myJsonObject.getJSONObject("wind");


                            mainTemp.setText(new StringBuilder().append(getString(R.string.mainTemp)).append(tempsCal(mainTemps.getInt("temp"))).append(getString(R.string.Celcius)).toString());
                            feelsLikeTemp.setText(new StringBuilder().append(getString(R.string.feelsLikeTemp)).append(tempsCal(mainTemps.getInt("feels_like"))).append(getString(R.string.Celcius)).toString());
                            minTemp.setText(new StringBuilder().append(getString(R.string.minTemp)).append(tempsCal(mainTemps.getInt("temp_min"))).append(getString(R.string.Celcius)).toString());
                            maxTemp.setText(new StringBuilder().append(getString(R.string.maxTemp)).append(tempsCal(mainTemps.getInt("temp_max"))).append(getString(R.string.Celcius)).toString());
                            windSpeed.setText(new StringBuilder().append(getString(R.string.windSpeed)).append(wind.get("speed")).append(getString(R.string.MetersPerSecond)).toString());

                            if (tempsCal(mainTemps.getInt("temp")) >= 8){
                                weatherImage.setImageResource(R.drawable.ic_sun_svgrepo_com);
                            }else if(tempsCal(mainTemps.getInt("temp"))< 8){
                                weatherImage.setImageResource(R.drawable.ic_thermometer_cold_fill_svgrepo_com);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    },
                    volleyError -> Toast.makeText(
                            getContext(),
                            volleyError.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show());

            RequestQueue requestQueue
                    = Volley.newRequestQueue(getContext());
            requestQueue.add(myRequest);
        }
        backPageButton.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());
        return root;

    }


    private double tempsCal(int i){

        double c = 273.15;
        double result = (double) i - c;
        return Math.round(result * 100.0) / 100.0;

    }
}