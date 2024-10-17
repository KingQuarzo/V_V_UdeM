package com.udem.appudem;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginUsuarioTest {

    @Mock
    LoginCliente loginCliente = new LoginCliente();

    @Test
    public void llamadoUsuarioTest(){
        loginCliente.logearUsuario("123456789","123456789");
        assertNotNull(loginCliente.user.toString());
    }

    @Test
    public void llamadoUsuarioInexistenteTest(){
        loginCliente.logearUsuario("noExiste","noExiste");
        assertNull(loginCliente.user);
        assertNotNull(loginCliente.builder.getContext());
    }

    @Test
    public void lanzarMenuPrincipalTest(){
        loginCliente.logearUsuario("123456789","123456789");
       assertNotNull(loginCliente.intent);
    }

    @Test
    public void lanzarRegistroTest(){
        loginCliente.registrarse();
        assertNotNull(loginCliente.intent);
    }
}
