package br.edu.ifspsaocarlos.agendacp.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider.AgendaCPContrato.KEY_EMAIL;
import static br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider.AgendaCPContrato.KEY_FONE;
import static br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider.AgendaCPContrato.KEY_ID;
import static br.edu.ifspsaocarlos.agendacp.contentprovider.ContatoProvider.AgendaCPContrato.KEY_NOME;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "agenda.db";
    public static final String DATABASE_TABLE = "contatos";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NOME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int    newVersion) {
    }
}

