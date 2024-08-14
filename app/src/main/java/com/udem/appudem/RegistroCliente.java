package com.udem.appudem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class RegistroCliente extends AppCompatActivity {

    EditText tipoIdentificacion;
    EditText identificacion;
    EditText correo;
    EditText nombre;
    EditText apellido;
    EditText password;
    EditText fechaNacimiento;

    Button registrarse;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;
    HashMap<Object, Object> usuario = new HashMap<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        construir();

        registrarse.setOnClickListener(view -> {
            registrarCliente(identificacion.getText().toString() + "@gmail.com", password.getText().toString());
        });
    }

    private void construir() {
        setContentView(R.layout.activity_registro_cliente);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Registro Cliente");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tipoIdentificacion = findViewById(R.id.tipoID);
        identificacion = findViewById(R.id.Identificacion);
        correo = findViewById(R.id.Correo);
        password = findViewById(R.id.Password);
        nombre = findViewById(R.id.Nombres);
        apellido = findViewById(R.id.Apellidos);
        fechaNacimiento = findViewById(R.id.FechaNacimiento);

        registrarse = findViewById(R.id.Registrarse);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegistroCliente.this);
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);
    }

    private void registrarCliente(String ident, String pass) {

        progressDialog.show();
        auth.createUserWithEmailAndPassword(ident, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;

                            String uid = user.getUid();
                            String id = identificacion.getText().toString();
                            String mail = correo.getText().toString();
                            String tid = tipoIdentificacion.getText().toString();
                            String nom = nombre.getText().toString();
                            String ape = apellido.getText().toString();
                            String pas = password.getText().toString();
                            String nacdate = fechaNacimiento.getText().toString();

                            usuario.put("uid", uid);
                            usuario.put("id", id);
                            usuario.put("tipoIdentificacion", tid);
                            usuario.put("correo", mail);
                            usuario.put("nombre", nom);
                            usuario.put("apellido", ape);
                            usuario.put("password", pas);
                            usuario.put("fechaNacimiento", nacdate);
                            usuario.put("documentoAdelante", "");
                            usuario.put("documentosAtras", "");
                            usuario.put("saldoCredito",0);
                            usuario.put("saldoAhorro",0);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("BASE DE DATOS REGISTRO");
                            reference.child(uid).setValue(usuario);

                            Toast.makeText(RegistroCliente.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistroCliente.this, LoginCliente.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistroCliente.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroCliente.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }






}