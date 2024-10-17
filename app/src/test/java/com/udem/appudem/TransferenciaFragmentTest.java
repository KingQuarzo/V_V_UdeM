package com.udem.appudem;


import android.view.LayoutInflater;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.udem.appudem.fragmentos.TransferenciaFragment;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class TransferenciaFragmentTest {

    public static class FakeActivity extends androidx.fragment.app.FragmentActivity {
        // Esta es una clase vacía para simular una actividad
    }

    private TransferenciaFragment fragment;

    @Before
    public void setUp() {
        // Arrange: Crear la instancia del fragmento y simular el ciclo de vida
        fragment = new TransferenciaFragment();
    }

    @Test
    public void testSpinnerIsNotNull() {
        // Arrange: Simular el ciclo de vida del fragmento usando FakeActivity
        LayoutInflater inflater = LayoutInflater.from(Robolectric.setupActivity(FakeActivity.class));
        fragment.onCreateView(inflater, null, null);

        // Act: Obtener la vista del Spinner
        Spinner tipoCuenta = fragment.getView().findViewById(R.id.tipoCuenta);

        // Assert: Verificar que el Spinner no sea nulo
        assertNotNull(tipoCuenta);
    }

    @Test
    public void testSpinnerHasCorrectAdapter() {
        // Arrange: Preparar el entorno y simular el ciclo de vida del fragmento
        LayoutInflater inflater = LayoutInflater.from(Robolectric.setupActivity(FakeActivity.class));
        fragment.onCreateView(inflater, null, null);

        // Act: Obtener la vista del spinner y su adaptador
        Spinner tipoCuenta = fragment.getView().findViewById(R.id.tipoCuenta);
        ArrayAdapter adapter = (ArrayAdapter) tipoCuenta.getAdapter();

        // Assert: Verificar que el adaptador no sea nulo y tiene el número correcto de elementos
        assertNotNull(adapter);
        assertEquals(3, adapter.getCount()); // Asumiendo que tienes 3 elementos en el array tipoCuenta
    }
}
