package willvarela.sportec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ActivityEquipo extends AppCompatActivity {

    private Button mBtAgregarIntegrante, mBtCrearEquipo;
    private TextView mTvNombreEquipo, mTvIntegrantes;
    private ImageView mIvFotoEquipo;
    private DatabaseReference mDatabase;
    private String Total;

    private ArrayList<EntidadEquipo> listaEquipos;
    private AdapterGanados adapterGanados;
    private Deportes deporte;
    private EntidadEquipo entidadEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo);
        setTitle("Equipo");

        mTvIntegrantes = (TextView) findViewById(R.id.tv_integrantes);
        mBtAgregarIntegrante = (Button) findViewById(R.id.bt_agregar_miembro);
        mBtCrearEquipo = (Button) findViewById(R.id.bt_crear_equipo);
        mTvNombreEquipo = (TextView) findViewById(R.id.tv_nombre_equipo);
        mIvFotoEquipo = (ImageView) findViewById(R.id.iv_imagen_equipo);

        deporte = (Deportes) getIntent().getSerializableExtra("objeto");

        leerEquipos();
    }

    private void leerEquipos() {
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

                validarEquipo(listaEquipos);



                //adapterGanados = new AdapterGanados(getApplicationContext(),listaEquipos);
                //mLvGanados.setAdapter(adapterGanados);
                //adapterGanados.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validarEquipo(ArrayList<EntidadEquipo> lisEquipo) {

        String linea;

        String[] archivos = fileList();

        if (existe(archivos, "user.txt")) {
            try {
                InputStreamReader archivo = new InputStreamReader( openFileInput("user.txt"));
                BufferedReader br = new BufferedReader(archivo);
                linea = br.readLine();
                Log.e("entra", "leer el archivo");
                Log.e("entra", linea);
                for(int i = 0; i < lisEquipo.size(); i++){
                    EntidadEquipo equipoTem= lisEquipo.get(i);

                    ArrayList<String> integrantesTemp = equipoTem.getIntegrantes();

                    Log.e("entra", "la lista es");
                    Log.e("entra", String.valueOf(integrantesTemp));
                    for (int j = 0; j < integrantesTemp.size(); j++){
                        String integrante = integrantesTemp.get(j);
                        if (integrante != null){
                            if (integrante.equals(linea)){
                                Log.e("entra", "si lee el nombre");
                                Log.e("entra", integrante);
                                mostrar(equipoTem);
                            }
                        }
                    }

                }
                br.close();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrar(EntidadEquipo equipoTem) {

        String integrantes = "Integrantes: "+"\n";
        Log.e("entra", "mostrar");
        Log.e("entra", String.valueOf(equipoTem));
        mTvNombreEquipo.setText(equipoTem.getNombre());
        Glide.with(this).load(equipoTem.getImagen())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvFotoEquipo);
        ArrayList<String> integrantesTemp = equipoTem.getIntegrantes();

        for (int j = 0; j < integrantesTemp.size(); j++){
            String integrante = integrantesTemp.get(j);
            if(integrante!=null)
            integrantes =integrantes + integrante + "\n";
        }
        mTvIntegrantes.setText(integrantes);

        mBtCrearEquipo.setVisibility(View.GONE);
        mTvIntegrantes.setVisibility(View.VISIBLE);
    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }
}
