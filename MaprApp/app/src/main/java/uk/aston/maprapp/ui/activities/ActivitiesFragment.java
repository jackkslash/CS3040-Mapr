package uk.aston.maprapp.ui.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import uk.aston.maprapp.R;
import uk.aston.maprapp.databinding.FragmentActivitiesBinding;



public class ActivitiesFragment extends Fragment {

    private FragmentActivitiesBinding binding;
    private List<Activities> acts;
    private RecyclerView actRecy;
    private ActivitiesListAdapter adapter;
    private Button returnButton;
    private TextView titleText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        returnButton = root.findViewById(R.id.returnButton);


        returnButton.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());


        actRecy = root.findViewById(R.id.actRecycleViewer);
        acts = new ArrayList<>();
        Bundle bundle = this.getArguments();


        titleText = root.findViewById(R.id.activitiesTitle);

        if (bundle.getString("Activity") == "restaurant"){
            titleText.setText("Restaurants");
        }else if (bundle.getString("Activity") == "tourist_attraction"){
            titleText.setText("Attractions");
        }


        Log.d("bundle", "bundle" + bundle);

        Uri builtUri = Uri.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                .buildUpon()
                .appendQueryParameter("location", bundle.getString("Latitude") + "," + bundle.getString("Longitude"))
                .appendQueryParameter("radius" , "5000")
                .appendQueryParameter("type", bundle.getString("Activity"))
                .appendQueryParameter("key", getString(R.string.places_api_key))
                .build();

        String JSON_URL = builtUri.toString();

        Log.d("url", "url built "+ JSON_URL);


        extractData(JSON_URL);

        Log.d("data", "extractDATA");

        return root;
    }

    private void extractData(String JSON_URL){

        String link = JSON_URL;

        StringRequest myRequest = new StringRequest(Request.Method.GET,link,
                response -> {
                    try {
                        //Create a JSON object containing information from the API.
                        JSONObject data = new JSONObject(response);
                        JSONArray  results = data.getJSONArray("results");
                        String status = data.getString("status");

                        if (results.length() == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle(getString(R.string.activitiesError))
                                    .setMessage(R.string.activitiesErrorMessage)
                                    .setNegativeButton(R.string.OKAlert, (dialogInterface, i) -> {
                                        dialogInterface.cancel();
                                        Navigation.findNavController(getView()).popBackStack();
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{

                            for (int i =0; i <= 10; i++){
                            JSONObject activity = results.getJSONObject(i);
                            Log.d("OBJ", "JSON " + results.getJSONObject(i));
                            Activities act = new Activities();
                            act.setPlaceName(activity.getString("name"));
                            act.setRating(activity.getString("rating"));
                            act.setVicinity(activity.getString("vicinity"));
                            JSONObject geo = activity.getJSONObject("plus_code");
                            act.setPlusCode(geo.getString("global_code"));
                            Log.d("RESP", "activity " + act + i);
                            acts.add(act);
                        }}

                    } catch (JSONException |NullPointerException e) {
                        e.printStackTrace();
                    }

                    actRecy.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new ActivitiesListAdapter(getContext(), acts);
                    actRecy.setAdapter(adapter);

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

}