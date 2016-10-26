package br.edu.ifspsaocarlos.agendacp.activity;

import android.os.Bundle;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecylerView(null);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupRecylerView(null);

    }

}
