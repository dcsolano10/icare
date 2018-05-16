package com.ingenian.icare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class QRLectorActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String IDENTIFICADOR ="Identificador_paciente";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferencePacientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlector);

        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReferencePacientes=mFirebaseDatabase.getReference().child("Pacientes");


        leerQR();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println("SE ESCANEÓ SÚPER HIPER DUPER BIEN: " + contents);
                buscarPaciente(contents);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
                System.out.println("NO LO SÉ Y NUNCA LO SABRÉ");
            }
        }
    }

    private void leerQR(){
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }
    }

    private void buscarPaciente(String contents){
        final String identificador=contents;

        ValueEventListener listenerConexiones = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(identificador)){
                    perfilPaciente(identificador);
                    System.out.println("Existe");
                }else{
                    System.out.println("NOOOOOOOOO Existe");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReferencePacientes.addValueEventListener(listenerConexiones);
    }

    public void perfilPaciente(String identificador) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra(IDENTIFICADOR,identificador);
        startActivity(intent);
    }
}

