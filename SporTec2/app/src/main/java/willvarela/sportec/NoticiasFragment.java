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
    final ArrayList<Entidad> arrayList = new ArrayList<Entidad>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_noticias, container, false);
        getActivity().setTitle("Noticias");

        mLvItems = (ListView) view.findViewById(R.id.lv_items);


        readData();

        adapterItems = new AdapterItems(getActivity().getApplicationContext(), arrayList);
        mLvItems.setAdapter(adapterItems);

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
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("futbol").child("1");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descripcion = String.valueOf(dataSnapshot.child("descripcion").getValue());
                                String fecha = String.valueOf(dataSnapshot.child("fecha").getValue());
                                String imageUrl = String.valueOf(dataSnapshot.child("imagen").getValue());
                                Log.e("descripcion:", descripcion);
                                Log.e("fecha:", fecha);
                                arrayList.add(new Entidad(imageUrl, descripcion, fecha));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else if (linea.equals("Basketbol")){
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("basket").child("1");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descripcion = String.valueOf(dataSnapshot.child("descripcion").getValue());
                                String fecha = String.valueOf(dataSnapshot.child("fecha").getValue());
                                String imageUrl = String.valueOf(dataSnapshot.child("imagen").getValue());
                                Log.e("descripcion:", descripcion);
                                Log.e("fecha:", fecha);
                                arrayList.add(new Entidad(imageUrl, descripcion, fecha));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else if (linea.equals("Beisbol")){
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("beisbol").child("1");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descripcion = String.valueOf(dataSnapshot.child("descripcion").getValue());
                                String fecha = String.valueOf(dataSnapshot.child("fecha").getValue());
                                String imageUrl = String.valueOf(dataSnapshot.child("imagen").getValue());
                                Log.e("descripcion:", descripcion);
                                Log.e("fecha:", fecha);
                                arrayList.add(new Entidad(imageUrl, descripcion, fecha));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else if (linea.equals("Tenis")){
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("tenis").child("1");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descripcion = String.valueOf(dataSnapshot.child("descripcion").getValue());
                                String fecha = String.valueOf(dataSnapshot.child("fecha").getValue());
                                String imageUrl = String.valueOf(dataSnapshot.child("imagen").getValue());
                                Log.e("descripcion:", descripcion);
                                Log.e("fecha:", fecha);
                                arrayList.add(new Entidad(imageUrl, descripcion, fecha));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else if (linea.equals("Voleybol")){
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("voleybol").child("1");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String descripcion = String.valueOf(dataSnapshot.child("descripcion").getValue());
                                String fecha = String.valueOf(dataSnapshot.child("fecha").getValue());
                                String imageUrl = String.valueOf(dataSnapshot.child("imagen").getValue());
                                Log.e("descripcion:", descripcion);
                                Log.e("fecha:", fecha);
                                arrayList.add(new Entidad(imageUrl, descripcion, fecha));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
