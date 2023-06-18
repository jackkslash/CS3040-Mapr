package uk.aston.maprapp.ui.locations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.aston.maprapp.R;

import uk.aston.maprapp.data.location.Location;
import uk.aston.maprapp.data.location.LocationViewModel;
import uk.aston.maprapp.databinding.FragmentLocationsBinding;


public class LocationsFragment extends Fragment {

    private FragmentLocationsBinding binding;
    private LocationViewModel mLocationViewModel;

    private LocationListAdapter adapter;

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        LocationsViewModel locationsViewModel =
                new ViewModelProvider(this).get(LocationsViewModel.class);

        binding = FragmentLocationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Set up the RecyclerView.
        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        adapter = new LocationListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Set up the LocationViewModel.
        mLocationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        // Get all locations
        // and associate them to the adapter.
        // Update the cached copy in adapter.

        mLocationViewModel.getAllLocations().observe(getViewLifecycleOwner(), adapter::setLocations);

        Button addLocationButton = root.findViewById(R.id.addLocation);
        Button oldButton = root.findViewById(R.id.oldButton);
        Button newButton = root.findViewById(R.id.newButton);
        Button abcButton = root.findViewById(R.id.abcButton);
        Button returnButton = root.findViewById(R.id.returnButton2);

        returnButton.setOnClickListener(view->{
            Navigation.findNavController(view).popBackStack();
        });


        oldButton.setOnClickListener(view -> {
            mLocationViewModel.getAllLocationsByOldest().observe(getViewLifecycleOwner(), adapter::setLocations);
            adapter.notifyDataSetChanged();
        });

        newButton.setOnClickListener(view -> {
            mLocationViewModel.getAllLocationsByNewest().observe(getViewLifecycleOwner(), adapter::setLocations);
            adapter.notifyDataSetChanged();
        });

        abcButton.setOnClickListener(view -> {
            mLocationViewModel.getAllLocations().observe(getViewLifecycleOwner(), adapter::setLocations);
            adapter.notifyDataSetChanged();
        });


        // Add the functionality to swipe items in the
        // RecyclerView to delete the swiped item.
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app.
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Location myLocation = adapter.getLocationAtPosition(position);
                        mLocationViewModel.deleteLocation(myLocation);
                    }
                });
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener((v, position) -> {
            Location location = adapter.getLocationAtPosition(position);

            Bundle bundle = new Bundle();


            bundle.putString("Name", location.getName());
            bundle.putString("Location", location.getLocation());
            bundle.putInt("ID", location.getId());

            Navigation.findNavController(v).navigate(R.id.action_navigation_locations_to_updateLocation, bundle);


        });

        addLocationButton.setOnClickListener(view -> {

            Navigation.findNavController(view).navigate(R.id.action_navigation_locations_to_updateLocation);
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}