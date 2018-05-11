package willvarela.sportec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class ActivityGridMultiple extends AppCompatActivity {

    private GridView mGvOpciones;
    private AdapterDeportes adapterDeportes;
    private ArrayList<Deportes> opciones;
    private Deportes deporte;
    private Deportes op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Opciones");
        setContentView(R.layout.activity_grid_multiple);
        mGvOpciones = (GridView) findViewById(R.id.gv_opciones);
        deporte = (Deportes) getIntent().getSerializableExtra("objeto");

        Log.e("Deporte", deporte.nombreDeporte);

        llenarLista();
        mostrar();

        mGvOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                op = opciones.get(position);

                if (op.getNombreDeporte().equals("Resultado")){
                    Intent intent = new Intent(ActivityGridMultiple.this, ActivityResultados.class);
                    intent.putExtra("objeto",deporte);
                    startActivity(intent);
                }
                else if(op.getNombreDeporte().equals("Retos")){
                    Intent intent = new Intent(ActivityGridMultiple.this, ActivityRetos.class);
                    intent.putExtra("objeto",deporte);
                    startActivity(intent);
                }
                //sigo con las demas

            }
        });

    }

    private void mostrar() {
        adapterDeportes = new AdapterDeportes(this,opciones);
        mGvOpciones.setAdapter(adapterDeportes);
        adapterDeportes.notifyDataSetChanged();
    }

    private void llenarLista() {
        opciones = new  ArrayList<>();
        Deportes resultados = new Deportes("Resultado",R.drawable.ic_action_resultados);
        Deportes equipo = new Deportes("Equipo",R.drawable.ic_action_perfil_equipo);
        Deportes retos = new Deportes("Retos",R.drawable.ic_action_retos);

        opciones.add(resultados);
        opciones.add(equipo);
        opciones.add(retos);
    }
}
