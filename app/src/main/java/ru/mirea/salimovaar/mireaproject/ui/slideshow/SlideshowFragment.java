package ru.mirea.salimovaar.mireaproject.ui.slideshow;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.salimovaar.mireaproject.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private int PermissionCode = 200;
    private Context mContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        mContext = inflater.getContext();
        if (ContextCompat.checkSelfPermission(mContext, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            Log.d(SlideshowFragment.class.getSimpleName().toString(), "Разрешения получены");
        } else {
            Log.d(SlideshowFragment.class.getSimpleName().toString(), "Нет разрешений!");

            ActivityCompat.requestPermissions(requireActivity(), new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE},
                    PermissionCode);

        }
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(mContext, player.class);
                ContextCompat.startForegroundService(mContext, serviceIntent);
            }
        });
        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.stopService(new Intent(mContext, player.class));
            }
        });
        return binding.getRoot();
    }
}