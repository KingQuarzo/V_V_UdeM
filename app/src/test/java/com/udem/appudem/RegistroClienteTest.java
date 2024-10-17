package com.udem.appudem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import com.udem.appudem.RegistroCliente;

public class RegistroClienteTest {

    @Test
    public void testEsValido() {
        // Arrange (Configurar): Configuramos la instancia de RegistroCliente y los valores de los campos.
        RegistroCliente registroCliente = new RegistroCliente();
        registroCliente.tipoIdentificacion.setText("CC");
        registroCliente.identificacion.setText("123456789");
        registroCliente.correo.setText("test@gmail.com");
        registroCliente.nombre.setText("Juan");
        registroCliente.apellido.setText("Pérez");
        registroCliente.password.setText("contraseña123");
        registroCliente.fechaNacimiento.setText("01/01/2000");

        // Act (Actuar): Llamamos al método que se quiere probar.
        boolean resultado = registroCliente.esValido();

        // Assert (Comprobar): Verificamos que el resultado sea verdadero.
        // Aquí se utiliza un Fluent Assertion con AssertJ:
        // `assertThat(resultado).isTrue()` es fluido y expresivo, y se lee como una oración natural.
        assertThat(resultado).isTrue();
    }

    @Test
    public void testRegistrarCliente() {
        // Arrange (Configurar): Creamos los mocks necesarios usando Mockito para simular FirebaseAuth y FirebaseUser.
        FirebaseAuth authMock = mock(FirebaseAuth.class);
        FirebaseUser userMock = mock(FirebaseUser.class);

        // Creamos una instancia de RegistroCliente y reemplazamos la dependencia real de FirebaseAuth con nuestro mock.
        RegistroCliente registroCliente = new RegistroCliente();
        registroCliente.auth = authMock;

        // Configuramos el comportamiento del mock de FirebaseAuth para que devuelva nuestro mock de FirebaseUser cuando se llame a getCurrentUser.
        when(authMock.getCurrentUser()).thenReturn(userMock);
        // Configuramos el comportamiento del mock de FirebaseUser para que devuelva un UID simulado.
        when(userMock.getUid()).thenReturn("mocked-uid");

        // Act (Actuar): Llamamos al método registrarCliente con datos de prueba.
        registroCliente.registrarCliente("test@gmail.com", "password123");

        // Assert (Comprobar): Verificamos que el método `createUserWithEmailAndPassword` se llamó con los parámetros correctos usando el mock.
        // También verificamos que el UID devuelto por el mock es el esperado.
        // Esto es un Test Doble porque estamos simulando el comportamiento de Firebase para evitar una conexión real.
        verify(authMock).createUserWithEmailAndPassword("test@gmail.com", "password123");
        // Usamos un Fluent Assertion con AssertJ para verificar que el UID sea "mocked-uid".
        assertThat(userMock.getUid()).isEqualTo("mocked-uid");
    }
}
