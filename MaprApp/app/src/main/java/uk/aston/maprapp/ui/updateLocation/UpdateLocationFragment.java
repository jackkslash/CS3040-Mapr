package uk.aston.maprapp.ui.updateLocation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uk.aston.maprapp.R;
import uk.aston.maprapp.data.location.Location;
import uk.aston.maprapp.ui.locations.LocationListAdapter;
import uk.aston.maprapp.data.location.LocationViewModel;
import uk.aston.maprapp.databinding.FragmentUpdateLocationBinding;



public class UpdateLocationFragment extends Fragment {


    private FragmentUpdateLocationBinding binding;
    private EditText mEditName;
    private EditText mEditLocation;

    private LocationViewModel mLocationViewModel;

    public UpdateLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateLocationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button saveButton = root.findViewById(R.id.saveButton);
        Button backButton = root.findViewById(R.id.backButton);
        mEditName = root.findViewById(R.id.name);
        mEditLocation = root.findViewById(R.id.location);
        TextView titleText = root.findViewById(R.id.updateTitle);
        Bundle bundle = this.getArguments();


        String name = bundle.getString("Name");
        String location = bundle.getString("Location");
        Integer position = bundle.getInt("ID", -1);
        final LocationListAdapter adapter = new LocationListAdapter(getContext());

        mLocationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        mLocationViewModel.getAllLocations().observe(getActivity(), adapter::setLocations);

        mEditName.setText(name);
        mEditLocation.setText(location);

        Log.d("bundle", "onCreateView: " + bundle);

        if (bundle.isEmpty()){
            titleText.setText("New Location");
        }else {
            titleText.setText("Update Location");
        }


        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });

        saveButton.setOnClickListener(view -> {
            String submittedEditName = mEditName.getText().toString();
            String submittedEditLocation = mEditLocation.getText().toString();
            if (TextUtils.isEmpty(submittedEditName) || TextUtils.isEmpty(submittedEditLocation) )
            {
                Toast.makeText(getContext(),R.string.emptyNameLocation, Toast.LENGTH_SHORT).show();
            }else {
                if (bundle.isEmpty() || position.equals(-1)) {

                    String insertName = mEditName.getText().toString();
                    String insertLoc = mEditLocation.getText().toString();
                    mLocationViewModel.insert(new Location(insertLoc, insertName));
                    Toast.makeText(getContext(),R.string.newLocation, Toast.LENGTH_SHORT).show();
                } else {
                    String insertName = mEditName.getText().toString();
                    String insertLoc = mEditLocation.getText().toString();
                    mLocationViewModel.update(new Location(position, insertLoc, insertName));
                    Toast.makeText(getContext(),R.string.updatedLocation, Toast.LENGTH_SHORT).show();
                }
                NavController navController = NavHostFragment.findNavController(this);
                navController.popBackStack();
            }
        });
        return root;
    }


}