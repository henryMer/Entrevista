package com.entrevista.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class AgregarEntrevistaActivity extends AppCompatActivity {
    private static final int COD_SEL_IMAGE = 300;
    ImageView photo_entrevista;
LinearLayout linearLayout_image_btn;
    Button btn_add_Et, btn_cu_photo, btn_r_photo;
    EditText entre, Desc, Perio, Fecha;
  private FirebaseFirestore mFirebase;

  StorageReference storageReference;
  String storage_path = "Entrevista/*";

    private Uri image_url;
    String photo = "photo";
    String idd;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_entrevista);

        this.setTitle("Crear Entrevista");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebase = FirebaseFirestore.getInstance();

        String id = getIntent().getStringExtra("id_Entrevista");
        mFirebase = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        entre = findViewById(R.id.entre);
        Desc = findViewById(R.id.Desc);
        Perio = findViewById(R.id.Perio);
        Fecha = findViewById(R.id.Fecha);
        btn_add_Et = findViewById(R.id.btn_add_Et);
        btn_cu_photo = findViewById(R.id.btn_cu_photo);
        btn_r_photo = fingViewById(R.id.btn_r_photo);
        photo_entrevista = findViewById(R.id.photo_Entrevista);

        btn_cu_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        if (id == null || id == ""){
            btn_add_Et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String entreEt = entre.getText().toString().trim();
                    String DescEt = Desc.getText().toString().trim();
                    String PerioEt = Perio.getText().toString().trim();
                    String FechaEt = Fecha.getText().toString().trim();

                    if (entreEt.isEmpty() && DescEt.isEmpty() && PerioEt.isEmpty() && FechaEt.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingresar datos", Toast.LENGTH_SHORT).show();
                    } else {
                        postEt(entreEt, DescEt, PerioEt, FechaEt);
                    }
                }
            });
        }else {
            btn_add_Et.setText("Update");
            getEt(id);

            btn_add_Et.setOnClickListener(new View.OnClickListener(){
                @Override
                        public void onClick(View v){
                    String entreEt = entre.getText().toString().trim();
                    String DescEt = Desc.getText().toString().trim();
                    String PerioEt = Perio.getText().toString().trim();
                    String FechaEt = Fecha.getText().toString().trim();

                if (entreEt.isEmpty() && DescEt.isEmpty() && PerioEt.isEmpty() && FechaEt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingresar datos", Toast.LENGTH_SHORT).show();
                } else {
                    updateEt(entreEt, DescEt, PerioEt, FechaEt, id);
                }}
            });
        }


    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (resultCode == RESULT_OK){
           if (requestCode == COD_SEL_IMAGE){
               image_url = data.getData();
               subirPhoto(image_url);
           }
       }
       super.onActivityResult(requestCode, resultCode,data);
    }

    private void subirPhoto(Uri imageUrl) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if (uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mFirebase.collection("pet").document(idd).update(map);
                            Toast.makeText(AgregarEntrevistaActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarEntrevistaActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEt(String entreEt, String descEt, String perioEt, String fechaEt, String id) {
        Map <String, Object> map = new HashMap<>();
        map.put("entre" , entreEt);
        map.put("desc", descEt);
        map.put("perio", perioEt);
        map.put("fecha", fechaEt);

   mFirebase.collection("entre").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
       @Override
       public void onSuccess(Void unused) {
           Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();
           finish();
       }
   }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
           Toast.makeText(getApplicationContext(), "Error Al Actualizar", Toast.LENGTH_SHORT).show();
       }
   });

    }

    private void postEt(String entreEt, String descEt, String perioEt, String fechaEt) {
        Map <String, Object> map = new HashMap<>();
        map.put("entre" , entreEt);
        map.put("desc", descEt);
        map.put("perio", perioEt);
        map.put("fecha", fechaEt);



    mFirebase.collection("Entrevistas").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Toast.makeText(getApplicationContext(), "Creado Exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(), "Error Al Ingresar", Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void getEt(String id){
        mFirebase.collection("Entrevista").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String entreET = documentSnapshot.getString("entre");
                    String DescEt = documentSnapshot.getString("Desc");
                    String PerioEt = documentSnapshot.getString("Perio");
                    String FechaEt = documentSnapshot.getString("Fecha");
                  String photo_entrevista = documentSnapshot.getString("photo");
                    entre.setText(entreET);
                    Desc.setText(DescEt);
                    Perio.setText(PerioEt);
                    Fecha.setText(FechaEt);
                try {
                    if(!photo_entrevista.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        Picasso.with(AgregarEntrevistaActivity.this)
                                .load(photo_entrevista)
                                .resize(150, 150)
                                .into(photo_entrevista);
                    }
                }catch (Exception e){
                    Log.v("Error", "e: " + e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
