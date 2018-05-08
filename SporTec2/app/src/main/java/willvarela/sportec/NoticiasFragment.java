package willvarela.sportec;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class NoticiasFragment extends Fragment {

    View view;
    ListView mLvItems;
    private AdapterItems adapterItems;
    private int CAMERA_REQUEST_CODE = 0;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    ArrayList<Entidad> arrayList;
    boolean fut = false , bas = false ,beis = false ,tenis = false ,voley = false ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_noticias, container, false);
        getActivity().setTitle("Noticias");

        mLvItems = (ListView) view.findViewById(R.id.lv_items);


        readData();


        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoticiaActivity.class);
                intent.putExtra("objeto",arrayList.get(position));
                startActivity(intent);
            }
        });


        return view;
    }

    private void readData() {
        String linea;

        String[] archivos = getActivity().fileList();
        mStorage = FirebaseStorage.getInstance().getReference();

        if (existe(archivos, "deportes.txt")) {
            try {
                InputStreamReader archivo = new InputStreamReader( getActivity().openFileInput("deportes.txt"));
                BufferedReader br = new BufferedReader(archivo);
                linea = br.readLine();
                while (linea != null) {
                    if (linea.equals("Futbol")){
                        fut = true;
                    }
                    else if (linea.equals("Basketbol")){
                        bas = true;
                    }
                    else if (linea.equals("Beisbol")){
                        beis = true;
                    }
                    else if (linea.equals("Tenis")){
                        tenis = true;
                    }
                    else if (linea.equals("Voleybol")){
                        voley = true;
                    }
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mostrar();
    }

    private void mostrar(){
        arrayList = new ArrayList<>();
        /*Log.e("fut:" , String.valueOf(fut));
        Log.e("bas:" , String.valueOf(bas));
        Log.e("beis:" , String.valueOf(beis));
        Log.e("tenis:" , String.valueOf(tenis));
        Log.e("voley:" , String.valueOf(voley));*/
        mDatabase = FirebaseDatabase.getInstance().getReference().child("noticias");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("entra", "**************************");
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()){
                    String titulo = snapshot.child("titulo").getValue(String.class);
                    String descripcion = snapshot.child("descripcion").getValue(String.class);
                    String fecha = snapshot.child("fecha").getValue(String.class);
                    String imagen = snapshot.child("imagen").getValue(String.class);
                    String tipo = snapshot.child("tipo").getValue(String.class);
                    Entidad entidad = new Entidad(imagen, descripcion, fecha, tipo ,titulo);
                    /*Log.e("fut:" , String.valueOf(fut));
                    Log.e("bas:" , String.valueOf(bas));
                    Log.e("beis:" , String.valueOf(beis));
                    Log.e("tenis:" , String.valueOf(tenis));
                    Log.e("voley:" , String.valueOf(voley));*/
                    if (fut == true && tipo.equals("futbol")){
                        arrayList.add(entidad);
                    }
                    else if (bas == true && tipo.equals("basket")){
                        arrayList.add(entidad);
                    }
                    else if (beis == true && tipo.equals("beis")){
                        arrayList.add(entidad);
                    }
                    else if (tenis == true && tipo.equals("tenis")){
                        arrayList.add(entidad);
                    }
                    else if (voley == true && tipo.equals("voley")){
                        arrayList.add(entidad);
                    }
                }

                adapterItems = new AdapterItems(getActivity().getApplicationContext(), arrayList);
                mLvItems.setAdapter(adapterItems);
                adapterItems.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private ArrayList<Entidad> getArrayItems(){
        //ArrayList<Entidad>
        return
    }*/

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
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
