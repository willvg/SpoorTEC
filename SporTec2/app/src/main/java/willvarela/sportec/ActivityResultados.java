package willvarela.sportec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityResultados extends AppCompatActivity {

    private Deportes deporte, opcion;
    private DatabaseReference mDatabase;
    ListView mLvResultados;
    ArrayList<EntidadResuldatos> listaResultados;
    AdapterResultados adapterResultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);


        deporte = (Deportes) getIntent().getSerializableExtra("objeto");
        setTitle("Resultados de " + deporte.getNombreDeporte());
        mLvResultados = (ListView) findViewById(R.id.lv_resultados);

        mostrar();


     }


     private void mostrar(){
         listaResultados = new ArrayList<>();
         mDatabase = FirebaseDatabase.getInstance().getReference().child("resultados").child(deporte.getNombreDeporte());
         mDatabase.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Log.e("entra", "//////////////////////");
                 for (DataSnapshot snapshot :
                         dataSnapshot.getChildren()){
                     String equipo1 = snapshot.child("equipo1").getValue(String.class);
                     int resultado1 = snapshot.child("resultado1").getValue(int.class);
                     String equipo2 = snapshot.child("equipo2").getValue(String.class);
                     int  resultado2 = snapshot.child("resultado2").getValue(int.class);

                     EntidadResuldatos entidadResuldatos = new EntidadResuldatos(equipo1, equipo2, resultado1, resultado2);

                     listaResultados.add(entidadResuldatos);


                 }

                 adapterResultados = new AdapterResultados(getApplicationContext(),listaResultados);
                 mLvResultados.setAdapter(adapterResultados);
                 adapterResultados.notifyDataSetChanged();
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
     }

    @Override
    public void onStart() {
        super.onStart();
        //Iniciar la comunicacion


    }

    //terminar la comunicaci[on con firebase
    @Override
    public void onStop() {
        super.onStop();

    }
}
