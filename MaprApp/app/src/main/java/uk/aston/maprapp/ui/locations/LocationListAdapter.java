package uk.aston.maprapp.ui.locations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import uk.aston.maprapp.R;
import uk.aston.maprapp.util.CordsUtil;
import uk.aston.maprapp.data.location.Location;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    private final LayoutInflater mInflater;
    private List<Location> mLocations;
    private static ClickListener clickListener;



    public LocationListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {

        if (mLocations != null) {
            Location current = mLocations.get(position);
            holder.locationItemView.setText(current.getLocation());
            holder.nameItemView.setText(current.getName());


            holder.locateButton.setOnClickListener(view -> {

                try {
                    LatLng loglat = new CordsUtil(view.getContext())
                            .getLocationFromAddress(view.getContext(), current.getLocation());

                    Bundle bundle = new Bundle();
                    bundle.putString("Name", current.getName());
                    bundle.putDouble("Longitude", loglat.longitude);
                    bundle.putDouble("Latitude", loglat.latitude);
                    Navigation.findNavController(view).navigate(R.id.action_navigation_locations_to_navigation_map, bundle);
                }catch (NullPointerException ex){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Sorry")
                            .setMessage("This location does not exist!")
                            .setNegativeButton(R.string.OKAlert, (dialogInterface, i) -> {
                                dialogInterface.cancel();
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            holder.infoButton.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("Location", current.getLocation());
                Navigation.findNavController(view).navigate(R.id.action_navigation_locations_to_navigation_Weather, bundle);

            });
            holder.shareButton.setOnClickListener(view -> {

                LatLng loglat = new CordsUtil(view.getContext())
                        .getLocationFromAddress(view.getContext(), current.getLocation());


                Bundle bundle = new Bundle();
                bundle.putString("Activity", "restaurant");
                try{
                    bundle.putString("Longitude", String.valueOf(loglat.longitude));
                    bundle.putString("Latitude", String.valueOf(loglat.latitude));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.action_navigation_locations_to_activitiesFragment, bundle);
            });

            holder.amuseButton.setOnClickListener(view -> {
                LatLng loglat = new CordsUtil(view.getContext())
                        .getLocationFromAddress(view.getContext(), current.getLocation());

                Bundle bundle = new Bundle();
                bundle.putString("Activity", "tourist_attraction");
                try{
                    bundle.putString("Longitude", String.valueOf(loglat.longitude));
                    bundle.putString("Latitude", String.valueOf(loglat.latitude));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                Navigation.findNavController(view).navigate(R.id.action_navigation_locations_to_activitiesFragment, bundle);

            });

            holder.dirButton.setOnClickListener(view -> {
                try{
                    LatLng loglat = new CordsUtil(view.getContext())
                            .getLocationFromAddress(view.getContext(), current.getLocation());

                    String log = String.valueOf(loglat.longitude);
                    String lat = String.valueOf(loglat.latitude);

                    Log.d("url", ""+String.valueOf(loglat.latitude)+String.valueOf(loglat.longitude));


                    String mURL = "google.navigation:q="+lat+","+log;

                    Log.d("url", ""+mURL);

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
                    intent.setPackage("com.google.android.apps.maps");
                    view.getContext().startActivity(intent);
                }catch (NullPointerException exception){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Sorry")
                            .setMessage("No directions could be found.")
                            .setNegativeButton(R.string.OKAlert, (dialogInterface, i) -> {
                                dialogInterface.cancel();
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

        } else {
            holder.locationItemView.setText(R.string.noLocationError);
        }
    }



    public void setLocations(List<Location> locations) {
        mLocations = locations;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        if (mLocations != null)
            return mLocations.size();
        else return 0;
    }




    public Location getLocationAtPosition(int position) {
        return mLocations.get(position);
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationItemView;
        private final TextView nameItemView;
        private final Button infoButton;
        private final Button shareButton;
        private final Button amuseButton;
        private final Button dirButton;
        private final Button locateButton;

        private LocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> clickListener.onItemClick(view, getAdapterPosition()));
            locationItemView = itemView.findViewById(R.id.textView);
            nameItemView = itemView.findViewById(R.id.textName);
            infoButton = itemView.findViewById(R.id.weatherButton);
            shareButton = itemView.findViewById(R.id.foodButton);
            amuseButton = itemView.findViewById(R.id.amuseButton);
            dirButton = itemView.findViewById(R.id.dirButton);
            locateButton = itemView.findViewById(R.id.locateButton);

        }


    }

    public void setOnItemClickListener(ClickListener clickListener) {
        LocationListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}


