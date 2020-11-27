package com.codes.mycafe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.codes.mycafe.MainViewModel;
import com.codes.mycafe.data.CafeMenu;
import com.codes.mycafe.databinding.FragmentHomeBinding;
import com.codes.mycafe.databinding.FragmentMenuBinding;
import com.codes.mycafe.databinding.FragmentMenuDetailBinding;

import java.util.Locale;


public class MenuDetailFragment extends Fragment {

    private MainViewModel viewModel;
    private FragmentMenuDetailBinding binding;
    private CafeMenu menu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding=FragmentMenuDetailBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getSelected().observe(getViewLifecycleOwner(), menu->{
            this.menu = menu;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(menu.name);

            binding.textViewName.setText(menu.name);
            binding.textViewPrice.setText(String.format(Locale.KOREA, "%,d원",menu.price));
            binding.textViewInfo.setText(menu.info);
        });

        binding.buttonSave.setOnClickListener(v->{
            menu.count = Integer.parseInt(binding.editTextCount.getText().toString());
            viewModel.setSelected(menu);
        });
    }
}
