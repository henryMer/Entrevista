package com.entrevista.demo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.entrevista.demo.AgregarEntrevistaActivity;
import com.entrevista.demo.R;
import com.entrevista.demo.model.Entrevista;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EntrevistaAdapter extends FirestoreRecyclerAdapter<Entrevista, EntrevistaAdapter.ViewHolder> {
    private FirebaseFirestore mfirestore = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EntrevistaAdapter(@NonNull FirestoreRecyclerOptions<Entrevista> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Entrevista entrev) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.entre.setText(entrev.getEntre());
        viewHolder.Desc.setText(entrev.getDesc());
        viewHolder.Perio.setText(entrev.getPerio());
        viewHolder.Fecha.setText(entrev.getFecha());

        viewHolder.btn_editar.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AgregarEntrevistaActivity.class);
                i.putExtra("id_Entrevista", id);
                activity.startActivity(i);
            }
        });                 }

        viewHolder.btn_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarEntrevista(id);
            }
        });
    }
            private void EliminarEntrevista(String id) {
                mfirestore.collection("Entrevista").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(activity,"Eliminado exitosamente",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity,"error al eliminar",Toast.LENGTH_SHORT).show();
                    }
                });
            }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_entrevista_single, parent, false);
       return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView entre, Desc, Perio, Fecha;
        ImageView btn_Eliminar, btn_editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entre = itemView.findViewById(R.id.entrevista);
            Desc = itemView.findViewById(R.id.descripcion);
            Perio = itemView.findViewById(R.id.periodista);
            Fecha = itemView.findViewById(R.id.fecha);
            btn_Eliminar = itemView.findViewById((R.id.btn_Eliminar));
            btn_editar = itemView.findViewById(R.id.btn_editar);
        }
    }
}
