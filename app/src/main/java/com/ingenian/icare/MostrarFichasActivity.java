package com.ingenian.icare;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MostrarFichasActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private String identificador;
    private ImageView imageView;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_fichas);
        identificador=getIntent().getStringExtra(PerfilActivity.ID_PACIENTE);
        context=this;
        imageView=findViewById(R.id.imageView5);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getFile();
    }

    private void getFile(){

        String dir="images/"+identificador+"/Ficha médica/"+"JPEG_20180523_102220_489455707.jpg";
        final StorageReference ref = mStorageRef.child(dir);
        System.out.println("DIIIIIR "+dir+"REFFFF "+ref);

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(getApplicationContext()).load(imageURL).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
