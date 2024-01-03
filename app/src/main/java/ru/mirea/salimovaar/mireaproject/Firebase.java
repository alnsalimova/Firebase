package ru.mirea.salimovaar.mireaproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import ru.mirea.salimovaar.mireaproject.databinding.FirebaseBinding;

public class Firebase extends AppCompatActivity {
    private static final String TAG = Firebase.class.getSimpleName();
    private FirebaseBinding binding;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialization views
        binding = FirebaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // [START initialize_auth] Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        String uniqueID = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        binding.textView6.setText(uniqueID);


        PackageManager Manager = getPackageManager();
        try {
            PackageInfo Info = Manager.getPackageInfo("com.anydesk.anydeskandroid", 0);

            if (Info != null) {
                showAnyDeskWarning();
            }
        }
        catch (PackageManager.NameNotFoundException e) {
        }
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.editTextEmail.getText());
                String password = String.valueOf(binding.editTextPassword.getText());
                signIn(email, password, v);
            }
        });
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(binding.editTextEmail.getText());
                String password = String.valueOf(binding.editTextPassword.getText());
                createAccount(email, password, v);
            }
        });
        binding.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        // [END initialize_auth]
        ShaHasher shaHasher = new ShaHasher();
        Log.w(TAG, "Hash: " + shaHasher.hash(String.valueOf(binding.editTextPassword.getText())));

    }

    private void showAnyDeskWarning() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("На устройстве установлен AnyDesk!");
        alert.setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Действие при нажатии "ОК"
                android.os.Process.killProcess(android.os.Process.myPid()); // Закрыть
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            binding.textView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));

            binding.textViewUI.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            binding.create.setVisibility(View.GONE);
            binding.editTextPassword.setVisibility(View.GONE);
            binding.signout.setVisibility(View.VISIBLE);
            binding.signin.setVisibility(View.GONE);
            binding.verify.setEnabled(!user.isEmailVerified());
            binding.verify.setVisibility(View.VISIBLE);
        } else {
            binding.textView.setText(R.string.signed_out);
            binding.textViewUI.setText(null);
            binding.create.setVisibility(View.VISIBLE);
            binding.editTextPassword.setVisibility(View.VISIBLE);
            binding.signin.setVisibility(View.VISIBLE);
            binding.signout.setVisibility(View.GONE);
            binding.verify.setVisibility(View.GONE);
        }
    }
    private void createAccount(String email, String password, View v) {
        Log.d(TAG, "createAccount:" + email);
        //if (!validateForm()) {
        //    return;
        //}
// [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user'sinformation
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            goSystem(v);
                        } else {
// If sign in fails, display a message to the user.

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Firebase.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void signIn(String email, String password, View v) {
        Log.d(TAG, "signIn:" + email);
// [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user'sinformation
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            goSystem(v);
                        } else {
// If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(Firebase.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
// [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                            binding.textView.setText(R.string.auth_failed);
                        }
// [END_EXCLUDE]

                    }
                });
// [END sign_in_with_email]
    }
    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    private void sendEmailVerification() {
// Disable button
        binding.verify.setEnabled(false);
// Send verification email
// [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

// [START_EXCLUDE]
// Re-enable button
                        binding.verify.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(Firebase.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Log.e(TAG, "sendEmailVerification", task.getException());

                            Toast.makeText(Firebase.this,
                                    "Failed to send verification email.",

                                    Toast.LENGTH_SHORT).show();

                        }
// [END_EXCLUDE]
                    }
                });
// [END send_email_verification]
    }
    // [END create_user_with_email]

    public void goSystem(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }}