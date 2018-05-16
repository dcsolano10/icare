package com.ingenian.icare;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PerfilActivity extends AppCompatActivity {

    private String identificador;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferencePacientes;
    private TextView txtNombre;
    private TextView txtEPS;
    private TextView txtEdad;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        context=this;

        txtNombre = findViewById(R.id.textViewNombre);
        txtEPS = findViewById(R.id.textViewEPS);
        txtEdad = findViewById(R.id.textViewEdad);

        obtenerIdentificador();
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReferencePacientes=mFirebaseDatabase.getReference().child("Pacientes");

        if(identificador!=null){
            buscarPaciente(identificador);
        }else{
            dialogoError();
        }

    }

    private void buscarPaciente(String contents){
        final String identificador=contents;

        ValueEventListener listenerConexiones = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(identificador)){
                    System.out.println("Existe");
                    DataSnapshot dataPaciente=dataSnapshot.child(identificador);
                    try {
                        int anoNacimiento = Integer.parseInt(dataPaciente.child("Nacimiento").getValue().toString());
                        int edad = Calendar.getInstance().get(Calendar.YEAR) - anoNacimiento;
                        txtEdad.setText(edad + "");
                    }catch (Exception e){
                        Toast toast = Toast.makeText(context, "Hubo un error obteniendo la edad del usuario.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                        txtNombre.setText(dataPaciente.child("Nombre").getValue().toString());
                    txtEPS.setText(dataPaciente.child("EPS").getValue().toString());


                }else{
                    dialogoError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReferencePacientes.addValueEventListener(listenerConexiones);
    }

    private void obtenerIdentificador() {
        String newString;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString(QRLectorActivity.IDENTIFICADOR);
        }
        identificador=newString;
    }

    private void dialogoError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No se encontró al paciente");
        // Add the buttons
        builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                finish();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
