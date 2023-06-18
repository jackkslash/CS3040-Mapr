package uk.aston.maprapp.ui.help;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.aston.maprapp.R;
import uk.aston.maprapp.databinding.FragmentHelpBinding;

public class helpFragment extends Fragment {

           private FragmentHelpBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button returnButton = root.findViewById(R.id.returnButton4);

        returnButton.setOnClickListener(view -> Navigation.findNavController(view).popBackStack());

        return root;
    }
}