package uk.aston.maprapp.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import uk.aston.maprapp.R;
import uk.aston.maprapp.data.location.LocationViewModel;
import uk.aston.maprapp.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private LocationViewModel mLocationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel userSettingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Button deleteAllButton = root.findViewById(R.id.button);
        Button returnButton = root.findViewById(R.id.returnButton3);
        Button helpButton = root.findViewById(R.id.helpButton);

        // Set up the LocationViewModel.
        mLocationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        deleteAllButton.setOnClickListener(view -> mLocationViewModel.deleteAll());
        returnButton.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());
        helpButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_navigation_user_to_helpFragment);
        });


        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}