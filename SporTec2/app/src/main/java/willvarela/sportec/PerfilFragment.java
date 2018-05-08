package willvarela.sportec;

import android.app.ProgressDialog;
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
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;


public class PerfilFragment extends Fragment {
    View view;
    private String tipo = "";
    private Button mBtCambiarimagen, mBtGuardar;
    private CheckBox mCbFutbol, mChbasket, mCbTenis, mChBeisbol, mCbVolleybol;
    private ImageView mIvImagen;
    private EditText mEtNombre;
    private String allSport = "";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private ProgressDialog mProgress;
    private int CAMERA_REQUEST_CODE = 0;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;

    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        getActivity().setTitle("Perfil");
        mBtCambiarimagen = (Button) view.findViewById(R.id.bt_cambiar_imagen);
        mBtGuardar = (Button) view.findViewById(R.id.bt_guardar_datos);
        mCbFutbol = (CheckBox) view.findViewById(R.id.cb_futbol_perfil);
        mChbasket = (CheckBox) view.findViewById(R.id.cb_basketbol_perfil);
        mChBeisbol = (CheckBox) view.findViewById(R.id.cb_beisbol_perfil);
        mCbTenis = (CheckBox) view.findViewById(R.id.cb_tenis_perfil);
        mCbVolleybol = (CheckBox) view.findViewById(R.id.cb_volleybol_perfil);
        mIvImagen = (ImageView) view.findViewById(R.id.iv_imagen_perfil_modificar);
        mEtNombre = (EditText) view.findViewById(R.id.et_nombre_perfil);


        mProgress = new ProgressDialog(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Seccion para la comunicacion del firebase y la plaicacion
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.e("entra", "********************");
                    actualizar();
                    readData();
                }
            }
        };

        mBtCambiarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select a picture for your profile"), CAMERA_REQUEST_CODE);
                }
            }
        });

        mBtGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNombre();
                guadarDeportes();
                Toast.makeText(getActivity(), "Los datos fueron guardados con exito", Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }

    private void guadarDeportes() {
        try {
            OutputStreamWriter archivo = new OutputStreamWriter(getActivity().openFileOutput(
                    "deportes.txt", Context.MODE_PRIVATE));
            if (mCbFutbol.isChecked() == true){
                allSport+="Futbol\n";
            }
            if (mChbasket.isChecked() == true){
                allSport+="Basketbol\n";
            }
            if (mChBeisbol.isChecked() == true){
                allSport+="Beisbol\n";
            }
            if (mCbTenis.isChecked() == true){
                allSport+="Tenis\n";
            }
            if (mCbVolleybol.isChecked() == true){
                allSport+="Voleybol\n";
            }
            archivo.write(allSport);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void guardarNombre() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference currentUserDB = mDatabase.child(mFirebaseAuth.getCurrentUser().getUid());
        currentUserDB.child("Nombre").setValue(mEtNombre.getText().toString());
    }

    private void actualizar(){

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mEtNombre.setText(String.valueOf(dataSnapshot.child("Nombre").getValue()));
                String imageUrl = String.valueOf(dataSnapshot.child("FotoPerfil").getValue());
                if (URLUtil.isValidUrl(imageUrl))
                    Glide.with(getActivity()).load(imageUrl)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(getActivity()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mIvImagen);
                //Picasso.with(MenuSporTec.this).load(Uri.parse(imageUrl)).into(imageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readData() {
        String linea;

        String[] archivos = getActivity().fileList();

        if (existe(archivos, "deportes.txt")) {
            try {
                InputStreamReader archivo = new InputStreamReader( getActivity().openFileInput("deportes.txt"));
                BufferedReader br = new BufferedReader(archivo);
                linea = br.readLine();
                while (linea != null) {
                    if (linea.equals("Futbol")){
                        mCbFutbol.setChecked(true);
                    }
                    else if (linea.equals("Basketbol")){
                        mChbasket.setChecked(true);
                    }
                    else if (linea.equals("Beisbol")){
                        mChBeisbol.setChecked(true);
                    }
                    else if (linea.equals("Tenis")){
                        mCbTenis.setChecked(true);
                    }
                    else if (linea.equals("Voleybol")){
                        mCbVolleybol.setChecked(true);
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
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);

    }

    //terminar la comunicaci[on con firebase
    @Override
    public void onStop() {
        super.onStop();

        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            final Uri sourceUri = data.getData();
            //Log.e("foto", sourceUri.toString());

            uploadImage(sourceUri);
        }
    }


    public void uploadImage(final Uri fileUri) {
        if (mFirebaseAuth.getCurrentUser() == null)
            return;

        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance().getReference();
        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final StorageReference filepath = mStorage.child("Photos").child(getRandomString());/*uri.getLastPathSegment()*/
        final DatabaseReference currentUserDB = mDatabase.child(mFirebaseAuth.getCurrentUser().getUid());

        mProgress.setMessage("Cargardo la imagen");
        mProgress.show();

        currentUserDB.child("FotoPerfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.getValue().toString();

                if (!image.equals("default") && !image.isEmpty()) {
                    Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(getActivity(), "Borrado de la imagen exitoso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Borrado de la imagen fallido", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                currentUserDB.child("FotoPerfil").removeEventListener(this);

                filepath.putFile(fileUri).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getActivity(), "Finalizado", Toast.LENGTH_SHORT).show();
                        Glide.with(getActivity()).load(fileUri)
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(getActivity()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mIvImagen);
                        //Picasso.with(MenuSporTec.this).load(fileUri).fit().centerCrop().into(imageProfile);
                        DatabaseReference currentUserDB = mDatabase.child(mFirebaseAuth.getCurrentUser().getUid());
                        currentUserDB.child("FotoPerfil").setValue(downloadUri.toString());
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

}
