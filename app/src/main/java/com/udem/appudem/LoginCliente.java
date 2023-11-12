package com.udem.appudem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginCliente extends AppCompatActivity {

    EditText identificaicon;
    EditText password;
    Button acceder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inicio Sesion");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        identificaicon = findViewById(R.id.txt_identificacion);
        password = findViewById(R.id.txt_password);
        acceder = findViewById(R.id.btn_acceder);

        acceder.setOnClickListener(view -> {
            if (identificaicon.getText().toString().equals("0000") && password.getText().toString().equals("0000")) {
                Toast.makeText(LoginCliente.this, "SUCCESSFULL!", Toast.LENGTH_SHORT).show();
                startActivities(new Intent[]{new Intent(LoginCliente.this, MainConsulta.class)});
            } else
                Toast.makeText(LoginCliente.this, "USERNAME OR PASSWORD IS WRONG", Toast.LENGTH_SHORT).show();

        });
    }
}