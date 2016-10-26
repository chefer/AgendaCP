package br.edu.ifspsaocarlos.agendacp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import br.edu.ifspsaocarlos.agendacp.data.SQLiteHelper;

/**
 * Created by dalbem on 26/10/2016.
 */

public class ContatoProvider extends ContentProvider {
    public static final int CONTATOS_DIR = 1;
    public static final int CONTATOS_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AgendaCPContrato.AUTHORITY, "contatos", CONTATOS_DIR);
        sURIMatcher.addURI(AgendaCPContrato.AUTHORITY, "contatos/#", CONTATOS_ID);
    }

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    @Override
    public boolean onCreate()
    {
        dbHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor;
        switch(sURIMatcher.match(uri))
        {
            case CONTATOS_DIR:
                cursor = database.query(SQLiteHelper.DATABASE_TABLE, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            case CONTATOS_ID:
                cursor = database.query(SQLiteHelper.DATABASE_TABLE, projection, AgendaCPContrato.KEY_ID + "=" + uri.getLastPathSegment(), null,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI desconhecida");
        }
        return cursor;
    }






    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        database = dbHelper.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        int count;
        switch(uriType)
        {
            case CONTATOS_DIR:
                count=database.delete(SQLiteHelper.DATABASE_TABLE, where,whereArgs);
                break;
            case CONTATOS_ID:
                count=database.delete(SQLiteHelper.DATABASE_TABLE, AgendaCPContrato.KEY_ID + "=" + uri.getLastPathSegment(), null);
                break;
            default:
                throw new IllegalArgumentException("URI desconhecida");
        }
        database.close();
        return count;
    }

    @Override
    public String getType(Uri arg0) {
        switch(sURIMatcher.match(arg0))
        {
            case CONTATOS_DIR:
                return AgendaCPContrato.CONTENT_TYPE;
            case CONTATOS_ID:
                return AgendaCPContrato.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("URI desconhecida");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database = dbHelper.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        long id;
        switch(uriType)
        {
            case CONTATOS_DIR:
                id=database.insert(SQLiteHelper.DATABASE_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("URI desconhecida");
        }
        uri = ContentUris.withAppendedId(uri, id);
        return uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        database = dbHelper.getWritableDatabase();
        int uriType = sURIMatcher.match(uri);
        int count;
        switch(uriType)
        {
            case CONTATOS_DIR:
                count=database.update(SQLiteHelper.DATABASE_TABLE, values,where,whereArgs);
                break;
            case CONTATOS_ID:
                count=database.update(SQLiteHelper.DATABASE_TABLE, values, AgendaCPContrato.KEY_ID + "=" + uri.getLastPathSegment(), null);
                break;
            default:
                throw new IllegalArgumentException("URI desconhecida");
        }
        database.close();
        return count;
    }


    public static final class AgendaCPContrato {
        public static final String AUTHORITY = "br.edu.ifspsaocarlos.agendacp.provider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY  + "/contatos");
        public static final String CONTENT_TYPE= "vnd.android.cursor.dir/vnd.br.edu.ifspsaocarlos.agendacp.contatos";
        public static final String CONTENT_ITEM_TYPE= "vnd.android.cursor.item/vnd.br.edu.ifspsaocarlos.agendacp.contatos";
        public static final String KEY_ID = "id";
        public static final String KEY_NOME = "nome";
        public static final String KEY_FONE = "fone";
        public static final String KEY_EMAIL = "email";
    }
}

