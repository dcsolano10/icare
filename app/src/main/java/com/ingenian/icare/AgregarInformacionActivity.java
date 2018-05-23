package com.ingenian.icare;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AgregarInformacionActivity extends AppCompatActivity {

    private Spinner spinnerInformacion;
    private Spinner spinnerComo;
    public final static String TIPO_INFO="tipo_informacion";
    private String identificador;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_informacion);
        identificador=getIntent().getStringExtra(PerfilActivity.ID_PACIENTE);

        spinnerInformacion = (Spinner) findViewById(R.id.spinnerInfo);
        spinnerComo = (Spinner) findViewById(R.id.spinnerComo);
        llenarSpinner();

    }

    private void llenarSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.informacion, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInformacion.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.como, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComo.setAdapter(adapter2);
    }




    public void agregar(View view){
        String informacion = spinnerInformacion.getSelectedItem().toString();
        String como = spinnerComo.getSelectedItem().toString();

        if(como.equals("Tomar foto")){
            Intent intent = new Intent(this, AgregarFotoActivity.class);
            intent.putExtra(TIPO_INFO,informacion);
            intent.putExtra(PerfilActivity.ID_PACIENTE,identificador);
            startActivity(intent);
        }

    }
}
