package com.udem.appudem;

import org.junit.Test;
import static org.junit.Assert.*;


public class RegistroClienteTest {

    @Test
    public void testEsValido() {
        // Crear una instancia de RegistroCliente
        RegistroCliente registroCliente = new RegistroCliente();

        // Simular la entrada de datos en los campos
        registroCliente.tipoIdentificacion.setText("CC");
        registroCliente.identificacion.setText("123456789");
        registroCliente.correo.setText("test@gmail.com");
        registroCliente.nombre.setText("Juan");
        registroCliente.apellido.setText("Pérez");
        registroCliente.password.setText("contraseña123");
        registroCliente.fechaNacimiento.setText("01/01/2000");

        // Verificar que es válido
        assertTrue(registroCliente.esValido());
    }

    @Test
    public void testEsValidoConCamposVacios() {
        RegistroCliente registroCliente = new RegistroCliente();

        // Simular campos vacíos
        registroCliente.tipoIdentificacion.setText("");
        registroCliente.identificacion.setText("");
        registroCliente.correo.setText("");
        registroCliente.nombre.setText("");
        registroCliente.apellido.setText("");
        registroCliente.password.setText("");
        registroCliente.fechaNacimiento.setText("");

        // Verificar que no es válido
        assertFalse(registroCliente.esValido());
    }
}
