package com.jose.bbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtFecha, txtHora, txtAsunto, txtNumCita;
    Datos datos;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       datos = new Datos(MainActivity.this,"Datos",null,1);
       db = datos.getWritableDatabase();


        txtNumCita = findViewById((R.id.textNumCita));
        txtFecha = findViewById((R.id.textFecha));
        txtHora = findViewById((R.id.textHora));
        txtAsunto = findViewById(R.id.textAsunto);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String numCita="";
        String fecha="";
        String hora="";
        String asunto="";

        switch (item.getItemId()) {

            case R.id.action_insertar:

                if(txtFecha.getText().toString().isEmpty() || txtHora.getText().toString().isEmpty() ||
                txtAsunto.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Todos los campos son obligatorios",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    fecha = txtFecha.getText().toString();
                    hora = txtHora.getText().toString();
                    asunto = txtAsunto.getText().toString();

                    insertarCita(fecha, hora, asunto);
                }
                return true;
            case R.id.action_borrar:
                if(!txtNumCita.getText().toString().isEmpty()) {
                    numCita = txtNumCita.getText().toString();
                    borrarCita(numCita);
                }
                return true;
            case R.id.action_consultar:

                numCita = txtNumCita.getText().toString();
                if(numCita != null)
                    consultarCita(numCita);
                return true;
            case R.id.action_modificar:
                //metodo modificarCita
                numCita = txtNumCita.getText().toString();
                fecha = txtFecha.getText().toString();
                hora = txtHora.getText().toString();
                asunto = txtAsunto.getText().toString();
                modificarCita(numCita,fecha,hora, asunto);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    private void modificarCita(String numCita, String fecha, String hora, String asunto) {
        try {
            String args[]={numCita};
            ContentValues valores = new ContentValues();
            valores.put("fecha", fecha);
            valores.put("hora", hora);
            valores.put("asunto", asunto);
            if (db.update("citas", valores, "id=?", args) == 1)
                Toast.makeText(this, "Se ha actualizado un registro", Toast.LENGTH_LONG).show();
            resetearCampos();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void consultarCita(String idCita) {
        try {
            String args[] = {idCita};
            Cursor c = db.rawQuery("SELECT * FROM citas WHERE id=?", args);
            //mover el cursor al principio
            if(c.moveToFirst()) {
                txtFecha.setText(c.getString(c.getColumnIndex("fecha")));
                txtHora.setText(c.getString(c.getColumnIndex("hora")));
                txtAsunto.setText(c.getString(c.getColumnIndex("asunto")));
            }else{
                Toast.makeText(this,"No existe este registro",Toast.LENGTH_SHORT).show();
            }

        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void borrarCita(String idCita) {
        try {
            String args[] = {idCita};
            Log.d("TAG", idCita);
            if (db.delete("citas", "id=?", args) == 1)
                Toast.makeText(this, "Se ha borrado un registro",
                        Toast.LENGTH_LONG).show();
            resetearCampos();
        }catch(SQLException ex){
            ex.printStackTrace();
        }


    }

    private void insertarCita(String fecha, String hora, String asunto) {
        try{
            ContentValues registro = new ContentValues();
            registro.put("fecha", fecha);
            registro.put("hora", hora);
            registro.put("asunto", asunto);
            if(db.insert("citas", null, registro)== 1)
                Toast.makeText(this, "Se ha insertado un registro", Toast.LENGTH_LONG).show();
                txtFecha.setText("");
                txtHora.setText("");
                txtAsunto.setText("");

        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void resetearCampos(){
        txtNumCita.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtAsunto.setText("");
    }
}