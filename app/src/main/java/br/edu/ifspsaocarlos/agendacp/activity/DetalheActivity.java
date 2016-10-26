package br.edu.ifspsaocarlos.agendacp.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider;
import br.edu.ifspsaocarlos.agendacp.model.Contato;
import br.edu.ifspsaocarlos.agendacp.R;


public class DetalheActivity extends AppCompatActivity {
    private Contato c;


    Uri uri = ContatoProvider.AgendaCPContrato.CONTENT_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("contato"))
        {
            this.c = (Contato) getIntent().getSerializableExtra("contato");
            EditText nameText = (EditText)findViewById(R.id.editText1);
            nameText.setText(c.getNome());
            EditText foneText = (EditText)findViewById(R.id.editText2);
            foneText.setText(c.getFone());
            EditText emailText = (EditText)findViewById(R.id.editText3);
            emailText.setText(c.getEmail());
            int pos =c.getNome().indexOf(" ");
            if (pos==-1)
                pos=c.getNome().length();
            setTitle(c.getNome().substring(0,pos));
        }
      //  cDAO = new ContatoDAO(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contato"))
        {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void apagar()
    {
        getContentResolver().delete(ContentUris.withAppendedId(uri, c.getId()), null,null);
        //getContentResolver().delete(uri, ContatoProvider.AgendaCPContrato.KEY_ID + "=?",new String[] {Long.toString(c.getId())});

        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    public void salvar()
    {
        String name = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText3)).getText().toString();

        ContentValues valores=new ContentValues();
        valores.put(ContatoProvider.AgendaCPContrato.KEY_NOME,name);
        valores.put(ContatoProvider.AgendaCPContrato.KEY_FONE,fone);
        valores.put(ContatoProvider.AgendaCPContrato.KEY_EMAIL,email);


        if (c==null)
        {
            getContentResolver().insert(uri,valores);
        }
        else
        {
            getContentResolver().update(ContentUris.withAppendedId(uri, c.getId()), valores, null, null);
            //getContentResolver().update(uri, valores, ContatoProvider.AgendaCPContrato.KEY_ID + "=?",new String[] {Long.toString(c.getId())});
        }
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}

