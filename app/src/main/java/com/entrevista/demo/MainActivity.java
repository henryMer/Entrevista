package com.entrevista.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.entrevista.demo.adapter.EntrevistaAdapter;
import com.entrevista.demo.model.Entrevista;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    Button btn_add;
    RecyclerView mRecycler;
    EntrevistaAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.RecyclerViewSingle);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        Query query = mFirestore.collection("Entrevista");

        FirestoreRecyclerOptions<Entrevista> Options =
                new FirestoreRecyclerOptions.Builder<Entrevista>()
                        .setQuery(query, Entrevista.class)
                        .build();

        mAdapter = new EntrevistaAdapter(Options,this);
        mRecycler.setAdapter(mAdapter);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               startActivity (new Intent(MainActivity.this, AgregarEntrevistaActivity.class));
            }
        });

    }
        @Override
        protected void onStart() {
            super.onStart();
            mAdapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            mAdapter.stopListening();
        }
    }