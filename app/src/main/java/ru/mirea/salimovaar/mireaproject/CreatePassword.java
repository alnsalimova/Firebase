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

public class CreatePassword extends AppCompatActivity {

    EditText editTextTextPassword1, editTextTextPassword2;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editTextTextPassword1 = (EditText) findViewById(R.id.editTextTextPassword1);
        editTextTextPassword2 = (EditText) findViewById(R.id.editTextTextPassword2);
        button = (Button) findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = editTextTextPassword1.getText().toString();
                String text2 = editTextTextPassword2.getText().toString();

                if (text1.equals("") || text2.equals("")) {
                    Toast.makeText(CreatePassword.this, "Пароль не введен!", Toast.LENGTH_SHORT).show();
                } else {
                    if (text1.equals(text2)) {
                        // сохранить пароль
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", text1);
                        editor.apply();

                        //enter
                        Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // пароли не одинаковые
                        Toast.makeText(CreatePassword.this, "Пароли не совпадают!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}