package com.udem.appudem;

import com.udem.appudem.fragmentos.PerfilUsuario;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

import android.Manifest;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Task;



import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class PerfilUsuarioTest {

    @Mock
    FirebaseAuth auth;
    @Mock
    FirebaseUser user;
    @Mock
    FirebaseDatabase firebaseDatabase;
    @Mock
    DatabaseReference databaseReference;
    @Mock
    DataSnapshot dataSnapshot;
    @Mock
    View mockView; // Mock del View para simular findViewById
    @Mock
    TextView mockTipoIdentificacion;
    @Mock
    TextView mockIdentificacion;
    @Mock
    TextView mockCorreo;
    @Mock
    TextView mockNombre;
    @Mock
    TextView mockApellido;
    @Mock
    TextView mockPassword;
    @Mock
    TextView mockFechaNacimiento;
    @Mock
    ImageView mockIdentificacionAdelante;
    @Mock
    ImageView mockIdentificacionAtras;
    @Mock
    FragmentActivity mockActivity;
    @Mock
    ContentResolver mockContentResolver;
    @Mock
    Uri mockImageUri;
    @Mock
    StorageReference storageReference;
    @Mock
    UploadTask uploadTask;

    @InjectMocks
    PerfilUsuario perfilUsuario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //arrange
        // Simula el comportamiento de Firebase Auth
        when(auth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("user123");
        when(firebaseDatabase.getReference("BASE DE DATOS REGISTRO")).thenReturn(databaseReference);

        // Mockeamos el comportamiento de findViewById en el View
        when(mockView.findViewById(R.id.EDIT_TIPO_ID)).thenReturn(mockTipoIdentificacion);
        when(mockView.findViewById(R.id.EDIT_ID)).thenReturn(mockIdentificacion);
        when(mockView.findViewById(R.id.EDIT_CORREO)).thenReturn(mockCorreo);
        when(mockView.findViewById(R.id.EDIT_NOMBRES)).thenReturn(mockNombre);
        when(mockView.findViewById(R.id.EDIT_APELLIDOS)).thenReturn(mockApellido);
        when(mockView.findViewById(R.id.EDIT_PASSWORD)).thenReturn(mockPassword);
        when(mockView.findViewById(R.id.EDIT_FECHA_NACIMIENTO)).thenReturn(mockFechaNacimiento);
        when(mockView.findViewById(R.id.EDIT_DOCUMENTO_ADELANTE)).thenReturn(mockIdentificacionAdelante);
        when(mockView.findViewById(R.id.EDIT_DOCUMENTO_ATRAS)).thenReturn(mockIdentificacionAtras);

        MockitoAnnotations.initMocks(this);

        // Simulamos que la actividad asociada al fragmento está presente
        when(perfilUsuario.requireActivity()).thenReturn(mockActivity);
        when(mockActivity.getContentResolver()).thenReturn(mockContentResolver);

        MockitoAnnotations.initMocks(this);

        // Simulación de FirebaseAuth y FirebaseUser
        when(auth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("user123");

        MockitoAnnotations.initMocks(this);
        perfilUsuario = new PerfilUsuario();
        perfilUsuario.storageRefence = storageReference;
        perfilUsuario.auth = auth;
        perfilUsuario.BASE_DE_DATOS_REGISTRO = databaseReference;

        mockImageUri = mock(Uri.class);

        when(auth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("user123");
    }

    @Test
    public void testIniciarConexionFirebase() {
        // Arrange: Configura los mocks correctamente

        // Simulación del comportamiento de child() para el snapshot de Firebase
        DataSnapshot idSnapshot = mock(DataSnapshot.class);
        DataSnapshot tipoIdSnapshot = mock(DataSnapshot.class);
        DataSnapshot nombreSnapshot = mock(DataSnapshot.class);
        DataSnapshot apellidoSnapshot = mock(DataSnapshot.class);
        DataSnapshot correoSnapshot = mock(DataSnapshot.class);

        // Configura los valores para cada campo
        when(idSnapshot.getValue()).thenReturn("123456");
        when(tipoIdSnapshot.getValue()).thenReturn("CC");
        when(nombreSnapshot.getValue()).thenReturn("Juan");
        when(apellidoSnapshot.getValue()).thenReturn("Perez");
        when(correoSnapshot.getValue()).thenReturn("juan@example.com");

        // Simula el comportamiento de dataSnapshot.child()
        when(dataSnapshot.child("id")).thenReturn(idSnapshot);
        when(dataSnapshot.child("tipoIdentificacion")).thenReturn(tipoIdSnapshot);
        when(dataSnapshot.child("nombre")).thenReturn(nombreSnapshot);
        when(dataSnapshot.child("apellido")).thenReturn(apellidoSnapshot);
        when(dataSnapshot.child("correo")).thenReturn(correoSnapshot);

        // Simula el listener para que dispare el onDataChange con el dataSnapshot simulado
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(dataSnapshot); // Simula la recepción de datos
            return null;
        }).when(databaseReference).addValueEventListener(any(ValueEventListener.class));

        // Act: Ejecuta la acción a probar
        perfilUsuario.iniciarConexionFirebase();

        // Assert: Verifica los resultados en las vistas
        assertEquals("123456", perfilUsuario.identificacion.getText().toString());
        assertEquals("CC", perfilUsuario.tipoIdentificacion.getText().toString());
        assertEquals("Juan", perfilUsuario.nombre.getText().toString());
        assertEquals("Perez", perfilUsuario.apellido.getText().toString());
        assertEquals("juan@example.com", perfilUsuario.correo.getText().toString());

        // Verify: Comprueba que el listener fue añadido correctamente
        verify(databaseReference).addValueEventListener(any(ValueEventListener.class));
    }

    @Test
    public void testConstruirFragment() {
        // Act: Llamamos a la función construirFragment con la vista mockeada
        perfilUsuario.construirFragment(mockView);

        // Assert: Verificamos que las referencias a las vistas no sean null
        assertNotNull(perfilUsuario.tipoIdentificacion);
        assertNotNull(perfilUsuario.identificacion);
        assertNotNull(perfilUsuario.correo);
        assertNotNull(perfilUsuario.nombre);
        assertNotNull(perfilUsuario.apellido);
        assertNotNull(perfilUsuario.password);
        assertNotNull(perfilUsuario.fechaNacimiento);
        assertNotNull(perfilUsuario.identificacionAdelante);
        assertNotNull(perfilUsuario.identificacionAtras);

        // Verify: Verificamos que findViewById fue llamado correctamente para cada vista
        verify(mockView).findViewById(R.id.EDIT_TIPO_ID);
        verify(mockView).findViewById(R.id.EDIT_ID);
        verify(mockView).findViewById(R.id.EDIT_CORREO);
        verify(mockView).findViewById(R.id.EDIT_NOMBRES);
        verify(mockView).findViewById(R.id.EDIT_APELLIDOS);
        verify(mockView).findViewById(R.id.EDIT_PASSWORD);
        verify(mockView).findViewById(R.id.EDIT_FECHA_NACIMIENTO);
        verify(mockView).findViewById(R.id.EDIT_DOCUMENTO_ADELANTE);
        verify(mockView).findViewById(R.id.EDIT_DOCUMENTO_ATRAS);
    }

    @Test
    public void testTomarFoto() {
        // Arrange
        // Simulamos el comportamiento de insertar una URI en el ContentResolver
        ContentValues values = new ContentValues();
        when(mockContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)).thenReturn(mockImageUri);

        // Act: Llamamos al método tomarFoto
        perfilUsuario.tomarFoto();

        // Assert: Verificamos que la URI se haya creado correctamente
        assertNotNull(mockImageUri);

        // Verify: Verificamos que los permisos de la cámara sean solicitados
        verify(mockActivity).requestPermissions(
                eq(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}),
                eq(100) // Verifica que el código de solicitud es correcto
        );

        // Verify: Verificamos que se creó la URI correctamente
        verify(mockContentResolver).insert(eq(MediaStore.Images.Media.EXTERNAL_CONTENT_URI), any(ContentValues.class));

        // Verify: Verificamos que se lanzó el Intent para abrir la cámara
        Intent expectedIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        expectedIntent.putExtra(MediaStore.EXTRA_OUTPUT, mockImageUri);
        verify(mockActivity).startActivityForResult(eq(expectedIntent), eq(200)); // Verifica que se usa el código correcto
    }

    @Test
    public void testActualizarImagenBD_Success() {
        // Arrange
        int posicion = 0; // 0 para documentoAdelante, 1 para documentoAtras
        String rutaAlmacenamientoEsperada = "Identificacion_Usuarios/documentoAdelante_user123";

        // Simulamos el comportamiento de Firebase Storage
        when(storageReference.child(rutaAlmacenamientoEsperada)).thenReturn(storageReference);
        when(storageReference.putFile(mockImageUri)).thenReturn(uploadTask);

        // Simulamos el éxito de la subida
        UploadTask.TaskSnapshot taskSnapshot = mock(UploadTask.TaskSnapshot.class);
        when(uploadTask.addOnSuccessListener(any(OnSuccessListener.class)))
                .thenAnswer(invocation -> {
                    OnSuccessListener<UploadTask.TaskSnapshot> listener = invocation.getArgument(0);
                    listener.onSuccess(taskSnapshot);
                    return uploadTask;
                });

        // Simulamos la obtención de la URI de descarga
        when(taskSnapshot.getStorage()).thenReturn(storageReference);
        when(storageReference.getDownloadUrl()).thenReturn(mock(Task.class));
        when(mock(Task.class).isSuccessful()).thenReturn(true);
        when(mock(Task.class).getResult()).thenReturn(mockImageUri);

        // Act
        perfilUsuario.actualizarImagenBD(mockImageUri, posicion);

        // Assert: Verificamos que la URI de la imagen no es null
        assertNotNull(mockImageUri);

        // Verify: Verifica que se subió el archivo correctamente
        verify(storageReference).putFile(mockImageUri);
        verify(databaseReference).updateChildren(any(HashMap.class)); // Verifica que se actualizó la base de datos
    }

    @Test
    public void testActualizarImagenBD_Failure() {
        // Arrange
        int posicion = 1; // 0 para documentoAdelante, 1 para documentoAtras
        String rutaAlmacenamientoEsperada = "Identificacion_Usuarios/documentosAtras_user123";

        // Simulamos el comportamiento de Firebase Storage
        when(storageReference.child(rutaAlmacenamientoEsperada)).thenReturn(storageReference);
        when(storageReference.putFile(mockImageUri)).thenReturn(uploadTask);

        // Simulamos el fallo de la subida
        when(uploadTask.addOnFailureListener(any(OnFailureListener.class)))
                .thenAnswer(invocation -> {
                    OnFailureListener listener = invocation.getArgument(0);
                    listener.onFailure(new Exception("Upload failed"));
                    return uploadTask;
                });

        // Act
        perfilUsuario.actualizarImagenBD(mockImageUri, posicion);

        // Assert: Verifica que no se subió la imagen ni se actualizó la base de datos
        assertNull(perfilUsuario.identificacionAdelante.getDrawable());  // Verifica que no hay imagen cargada

        // Verify: Verifica que se intentó subir el archivo
        verify(storageReference).putFile(mockImageUri);
        verify(databaseReference, never()).updateChildren(any(HashMap.class)); // Verifica que no se actualizó la base de datos
    }


}
