package com.udem.appudem.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.udem.appudem.R;

public class TransferenciaFragment extends Fragment {

    Spinner tipoCuenta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transferencia, container, false);
        tipoCuenta = view.findViewById(R.id.tipoCuenta);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.tipoCuenta, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);


        tipoCuenta.setAdapter(adapter);
        return view;
    }
}