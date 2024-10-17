package com.udem.appudem.fragmentos;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.udem.appudem.R;

import java.util.HashMap;

public class PerfilUsuario extends Fragment {

    TextView tipoIdentificacion;
    TextView identificacion;
    TextView correo;
    TextView nombre;
    TextView apellido;
    TextView password;
    TextView fechaNacimiento;

    ImageView identificacionAdelante;
    ImageView identificacionAtras;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference BASE_DE_DATOS_REGISTRO;

    private ProgressDialog progressDialog;

    StorageReference storageRefence;
    String rutaAlmacenamiento = "Identificacion_Usuarios/*";

    private static final int CODIGO_DE_SOLICITUD_DE_CAMARA = 100;
    private static final int CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGENES = 200;

    private String[] permisos_de_la_camara;
    private Uri imagen_uri;

    int camara;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        construirFragment(view);
        iniciarConexionFirebase();

        identificacionAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camara = 0;
                tomarFoto();
            }
        });
        identificacionAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camara = 1;
                tomarFoto();
            }
        });

        return view;
    }

    private void iniciarConexionFirebase() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageRefence = getInstance().getReference();

        BASE_DE_DATOS_REGISTRO = FirebaseDatabase.getInstance().getReference("BASE DE DATOS REGISTRO");

        BASE_DE_DATOS_REGISTRO.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String bdid = "" + snapshot.child("id").getValue();
                    String bdtipoId = "" + snapshot.child("tipoIdentificacion").getValue();
                    String bdbdnombre = "" + snapshot.child("nombre").getValue();
                    String bdapellido = "" + snapshot.child("apellido").getValue();
                    String bdcorreo = "" + snapshot.child("correo").getValue();
                    String bdpassword = "" + snapshot.child("password").getValue();
                    String bdfechaNacimiento = "" + snapshot.child("fechaNacimiento").getValue();
                    String bddocumentoAdelante = "" + snapshot.child("documentoAdelante").getValue();
                    String bddocumentoAtras = "" + snapshot.child("documentosAtras").getValue();


                    identificacion.setText(bdid);
                    tipoIdentificacion.setText(bdtipoId);
                    nombre.setText(bdbdnombre);
                    apellido.setText(bdapellido);
                    correo.setText(bdcorreo);
                    password.setText(bdpassword);
                    fechaNacimiento.setText(bdfechaNacimiento);

                    try {
                        Picasso.get().load(bddocumentoAdelante).placeholder(R.drawable.escudo_udem).into(identificacionAdelante);
                        Picasso.get().load(bddocumentoAtras).placeholder(R.drawable.escudo_udem).into(identificacionAtras);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.escudo_udem).into(identificacionAdelante);
                        Picasso.get().load(R.drawable.escudo_udem).into(identificacionAtras);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void construirFragment(View view) {
        tipoIdentificacion = view.findViewById(R.id.EDIT_TIPO_ID);
        identificacion = view.findViewById(R.id.EDIT_ID);
        correo = view.findViewById(R.id.EDIT_CORREO);
        nombre = view.findViewById(R.id.EDIT_NOMBRES);
        apellido = view.findViewById(R.id.EDIT_APELLIDOS);
        password = view.findViewById(R.id.EDIT_PASSWORD);
        fechaNacimiento = view.findViewById(R.id.EDIT_FECHA_NACIMIENTO);

        identificacionAdelante = view.findViewById(R.id.EDIT_DOCUMENTO_ADELANTE);
        identificacionAtras = view.findViewById(R.id.EDIT_DOCUMENTO_ATRAS);

    }

    private void tomarFoto() {
        permisos_de_la_camara = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permisos_de_la_camara, CODIGO_DE_SOLICITUD_DE_CAMARA);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descricion");
        imagen_uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagen_uri);
        startActivityForResult(camaraIntent,CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGENES);
    }

    private void actualizarImagenBD(Uri uri, int posicion) {

        String Ruta_de_archivo_nombre;

        if (posicion == 0){

            Ruta_de_archivo_nombre = rutaAlmacenamiento + "documentoAdelante" + "_" + user.getUid();
        }else {

            Ruta_de_archivo_nombre = rutaAlmacenamiento + "documentosAtras" + "_" + user.getUid();
        }

        HashMap<String, Object> usuario = new HashMap<>();
        StorageReference storageReferenceDos = storageRefence.child(Ruta_de_archivo_nombre);
        storageReferenceDos.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                if (uriTask.isSuccessful()) {
                    if (posicion == 0) {
                        usuario.put("documentoAdelante", downloadUri.toString());
                        BASE_DE_DATOS_REGISTRO.child(user.getUid()).updateChildren(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        usuario.put("documentosAtras", downloadUri.toString());
                        BASE_DE_DATOS_REGISTRO.child(user.getUid()).updateChildren(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(getActivity(), "Actualizado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getActivity(), "Ocurrio un error!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CODIGO_DE_GALERIA_DE_SELECCION_DE_IMAGENES){
                actualizarImagenBD(imagen_uri,camara);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}