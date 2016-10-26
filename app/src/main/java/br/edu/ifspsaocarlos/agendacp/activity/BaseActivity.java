package br.edu.ifspsaocarlos.agendacp.activity;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;

import br.edu.ifspsaocarlos.agendacp.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider;
import br.edu.ifspsaocarlos.agendacp.model.Contato;
import br.edu.ifspsaocarlos.agendacp.R;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    List<Contato> contatos = new ArrayList<Contato>();

    private RecyclerView recyclerView;

    private SearchView searchView;

    private ContatoAdapter adapter;

    Uri uri = ContatoProvider.AgendaCPContrato.CONTENT_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
            if (resultCode == RESULT_OK)
                showSnackBar("Contato inserido");

        if (requestCode == 2) {
                if (resultCode == RESULT_OK)
                    showSnackBar("Informações do contato alteradas");
                if (resultCode == 3)
                    showSnackBar("Contato removido");

            }

        setupRecylerView(null);
    }

    public void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout=(CoordinatorLayout) findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }


    protected void setupRecylerView(String nomeContato) {

       contatos.clear();

        String where=null;
        String[] argWhere=null;
        if (nomeContato!=null)
        {
            where = ContatoProvider.AgendaCPContrato.KEY_NOME + " like ?";
            argWhere = new String[]{nomeContato + "%"};
        }

        Cursor cursor = getContentResolver().query(uri,null,where,argWhere,ContatoProvider.AgendaCPContrato.KEY_NOME);
        if (cursor!=null)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Contato contato = new Contato();
                    contato.setId(cursor.getInt(0));
                    contato.setNome(cursor.getString(1));
                    contato.setFone(cursor.getString(2));
                    contato.setEmail(cursor.getString(3));
                    contatos.add(contato);
                    cursor.moveToNext();
                }
                cursor.close();
         }


        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);


        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Contato contato = contatos.get(position);
                Intent i = new Intent(getApplicationContext(), DetalheActivity.class);
                i.putExtra("contato", contato);
                startActivityForResult(i, 2);
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    getContentResolver().delete(ContentUris.withAppendedId(uri,contato.getId()), null,null);
                    //getContentResolver().delete(uri, ContatoProvider.AgendaCPContrato.KEY_ID + "=?",new String[] {Long.toString(contato.getId())});

                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();

                    showSnackBar("Contato removido");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
