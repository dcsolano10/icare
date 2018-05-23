package com.ingenian.icare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText txtID;
    private Context context;
    private ProgressBar spinner;
    private TextView txtInfo;
    private Button bBuscar;
    private Button bIntentar;

    private boolean mostrar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrlector);

        txtID= findViewById(R.id.editText);
        txtInfo=findViewById(R.id.textView7);
        bBuscar= findViewById(R.id.button6);
        bIntentar= findViewById(R.id.button5);

        context = this;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReferencePacientes=mFirebaseDatabase.getReference().child("Pacientes");

        mostrar=true;
        ocultarVista();
        leerQR();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mostrar)
        mostrarVista();
        else
            ocultarVista();
        System.out.println("ON RESUMEEEEEE");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ocultarVista();
        System.out.println("ON PAUSEEEEEE");
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
            mostrar=false;
            if (resultCode == RESULT_OK) {
                mostrar=false;
                ocultarVista();
                String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println("SE ESCANEÓ SÚPER HIPER DUPER BIEN: " + contents);
                buscarPaciente(contents);
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
                System.out.println("NO LO SÉ Y NUNCA LO SABRÉ");
                mostrar=true;
            }
        }
    }

    private void leerQR(){
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0);
            mostrar=false;

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
            mostrar=true;
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
                    dialogoError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mostrar=false;
            }
        };
        mDatabaseReferencePacientes.addValueEventListener(listenerConexiones);
    }

    public void perfilPaciente(String identificador) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra(IDENTIFICADOR,identificador);
        startActivity(intent);
        mostrar=true;
    }

    public void escanearNuevamente(View view){
        leerQR();
    }

    public void buscarID(View view){
        String id = txtID.getText().toString().trim();
        if(id.isEmpty()){
            Toast toast = Toast.makeText(this, "Debes escribir un identificador.", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            buscarPaciente(id);
        }

    }

    private void dialogoError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No se encontró al paciente. Intenta nuevamente.");
        // Add the buttons
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarVista(){
        txtID.setVisibility(View.VISIBLE);
        txtInfo.setVisibility(View.VISIBLE);
        bBuscar.setVisibility(View.VISIBLE);
        bIntentar.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
    }

    private void ocultarVista(){
        txtID.setVisibility(View.GONE);
        txtInfo.setVisibility(View.GONE);
        bBuscar.setVisibility(View.GONE);
        bIntentar.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }
}

