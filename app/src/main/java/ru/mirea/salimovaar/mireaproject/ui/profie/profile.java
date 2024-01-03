package ru.mirea.salimovaar.mireaproject.ui.profie;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.security.keystore.KeyGenParameterSpec;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.salimovaar.mireaproject.R;
import ru.mirea.salimovaar.mireaproject.databinding.FragmentProfileBinding;

public class profile extends Fragment {

    FragmentProfileBinding binding;
    private SharedPreferences preferences;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mContext = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;

        try {
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    mContext,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            secureSharedPreferences.edit().putString("Poet", "Иосиф Бродский").apply();
            secureSharedPreferences.edit().putString("Poem", "Прощай").apply();
            binding.poet.setText(secureSharedPreferences.getString("Poet", "not found"));
            binding.poem.setText(secureSharedPreferences.getString("Poem", "not found"));
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return binding.getRoot();

    }
}