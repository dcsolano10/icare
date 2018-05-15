package com.ingenian.icare;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuentaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        mAuth = FirebaseAuth.getInstance();
        context = this;
    }

    public void iniciarSesion(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void actionCrearCuenta(View view){
        EditText txtEmail = findViewById(R.id.editTextEmailCrear);
        String email = txtEmail.getText().toString();
        EditText txtContrasena = findViewById(R.id.editTextPasswordCrear);
        String contrasena = txtContrasena.getText().toString();
        if(!email.trim().isEmpty() && !contrasena.trim().isEmpty())
        {
            createAccount(email,contrasena);
        }else{
            Toast.makeText(context, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }


    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null)
        Toast.makeText(context, "Se creó tu cuenta correctamente", Toast.LENGTH_SHORT).show();
    }
}
