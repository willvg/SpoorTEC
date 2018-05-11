package willvarela.sportec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityRetos extends AppCompatActivity {

    TabHost tabs;

    private Deportes deporte;
    private EntidadEquipo entidadEquipo;
    private DatabaseReference mDatabase;
    ListView mLvGanados;
    ArrayList<EntidadEquipo> listaEquipos;
    AdapterGanados adapterGanados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Retos");
        setContentView(R.layout.activity_retos);
        mLvGanados = (ListView) findViewById(R.id.lv_clasificacion);

        deporte = (Deportes) getIntent().getSerializableExtra("objeto");

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab_retos);
        spec.setIndicator("Retos");
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab_clasificacion);
        spec.setIndicator("Juegos Ganados");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        Log.e("El valir es", tabs.getCurrentTabTag());
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabs.getCurrentTabTag().equals("mitab1")){

                }
                else if(tabs.getCurrentTabTag().equals("mitab2")){
                    mostrar();
                }
            }
        });
    }

    private void mostrar() {
        listaEquipos = new ArrayList<>();
        String temporal = "";
        if (deporte.getNombreDeporte().equals("Voleibol")){
            temporal = "Voleybol";
        }
        else if (deporte.getNombreDeporte().equals("Futbol")){
            temporal = "Fulbol";
        }
        else{
            temporal = deporte.getNombreDeporte();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("equipos").child(temporal);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("entra", "+++++++++++++++++++");
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()){
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String imagen = snapshot.child("imagen").getValue(String.class);
                    String activo = snapshot.child("activo").getValue(String.class);
                    int ganados = snapshot.child("ganados").getValue(int.class);
                    ArrayList<String> temp = (ArrayList<String>) snapshot.child("integrantes").getValue();


                    EntidadEquipo entidadEquipoTemp = new EntidadEquipo(nombre,imagen,activo,temp,ganados);

                    listaEquipos.add(entidadEquipoTemp);

                }

                //Comparator<EntidadEquipo> comparador = Collections.reverseOrder();
                //Collections.sort(listaEquipos, comparador);

                adapterGanados = new AdapterGanados(getApplicationContext(),listaEquipos);
                mLvGanados.setAdapter(adapterGanados);
                adapterGanados.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //sacar a los integrantes

    }
}
