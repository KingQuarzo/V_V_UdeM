package com.udem.appudem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginCliente extends AppCompatActivity {

    EditText identificaicon;
    EditText password;
    Button acceder;
    Button registrarse;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    Intent intent;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        construir();

        acceder.setOnClickListener(view -> {
            logearUsuario(identificaicon.getText().toString(), password.getText().toString());
        });

        registrarse.setOnClickListener(view -> {
            registrarse();
        });
    }

    private void construir(){
        setContentView(R.layout.login_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inicio Sesion");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        identificaicon = findViewById(R.id.txt_identificacion);
        password = findViewById(R.id.txt_password);
        acceder = findViewById(R.id.btn_acceder);
        registrarse = findViewById(R.id.Registrar);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginCliente.this);
        progressDialog.setMessage("Ingresando");
        progressDialog.setCancelable(false);

    }

    public void registrarse(){
        intent = new Intent(LoginCliente.this, RegistroCliente.class);
        startActivity(intent);
        finish();
    }

    protected void logearUsuario(String id, String password) {
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseAuth.signInWithEmailAndPassword(id+"@gmail.com", password)
                .addOnCompleteListener(LoginCliente.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            user = firebaseAuth.getCurrentUser();
                            //user=null;
                            assert user != null : "Fail to get user";
                            Toast.makeText(LoginCliente.this, "SUCCESSFULL!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(LoginCliente.this, MainConsulta.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        usuarioInvalido();
                    }
                });
    }

    private void usuarioInvalido() {
        builder = new AlertDialog.Builder(LoginCliente.this);
        builder.setCancelable(false);
        builder.setTitle("Ha ocurrido un error");
        builder.setMessage("Verifique los datos ingresados")
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}