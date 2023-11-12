package com.udem.appudem;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.udem.appudem.fragmentos.ConsultaFragment;

public class MainConsulta extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_consultas_layout);

        Toolbar toolbar = findViewById(R.id.toobarMain);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.menu_layout);

        NavigationView navigationView = findViewById(R.id.nav_viewA);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,
                    new ConsultaFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_consultas);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_consultas){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,
                    new ConsultaFragment()).commit();
        }else if (itemId == R.id.Salir){

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
