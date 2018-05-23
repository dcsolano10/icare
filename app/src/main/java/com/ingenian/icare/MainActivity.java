package com.ingenian.icare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            //Esta iniciada la sesión - Debe pasar a la siguiente activity
        }else{
            //Esperar a que inicie sesión
        }
    }

    public void iniciarSesion(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void escanear(View view) {
        Intent intent = new Intent(this, QRLectorActivity.class);
        startActivity(intent);
    }

    public void alerta(View view) {
        Intent intent = new Intent(this, AlertaActivity.class);
        startActivity(intent);
    }
}
