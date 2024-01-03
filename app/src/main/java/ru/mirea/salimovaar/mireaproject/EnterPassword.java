package ru.mirea.salimovaar.mireaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPassword extends AppCompatActivity {

    EditText editTextTextPassword;
    Button button;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        //загрузить пароль
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        password = settings.getString("password", "");

        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        button = (Button) findViewById(R.id.button6);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextTextPassword.getText().toString();

                if (text.equals(password)) {
                    //зайти в приложение
                    Intent intent = new Intent(getApplicationContext(), Firebase.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPassword.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}