package willvarela.sportec;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;

public class MenuSporTec extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Variables para la comunicacion con firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private android.support.v4.app.FragmentManager fragmentManager;
    GoogleApiClient googleApiClient;
    private View hView;
    TextView nombre;
    ImageView imagePerfil;
    private String nomTem, imageTem, correoTem;
    private int CAMERA_REQUEST_CODE = 0;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private String tipo ="";
    private ProgressDialog mProgress;

    boolean bandera = false;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_spor_tec);
        tipo = getIntent().getExtras().getString("tipo");
        mProgress = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Declaracion del nombre e imagen de perfil
        hView = navigationView.getHeaderView(0);

        nombre = (TextView) hView.findViewById(R.id.tv_nombre);
        imagePerfil = (ImageView) hView.findViewById(R.id.iv_imagen_perfil);

        //login silecionso
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new  GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MenuSporTec.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOptions)
                .build();

        //Seccion para la comunicacion del firebase y la plaicacion
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (tipo.equals("cp")){
                        setUserData2();
                    }
                    else if (tipo.equals("gm")){
                        setUserData(user);
                        agregar();
                    }

                } else {
                    goLogInScreen();
                }
            }
        };

        //Fragment por defecto
        //FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new NoticiasFragment()).commit();



    }

    private void agregar(){
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()){
                    String nombre  = snapshot.child("Nombre").getValue(String.class);

                    Log.e("2:" , user.getDisplayName());
                    //Log.e("1:" , nombre);

                    if (nombre != null){
                        if (nombre.equals(user.getDisplayName())){
                            bandera = true;
                        }
                    }
                }
                Log.e("bas:" , String.valueOf(bandera));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (!bandera){
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            DatabaseReference currentUserDBTem = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
            currentUserDBTem.child("Nombre").setValue(user.getDisplayName());
            currentUserDBTem.child("FotoPerfil").setValue("default");

            Toast.makeText(getApplicationContext(), "Registro con exito", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUserData(FirebaseUser user) {


        nombre.setText(user.getDisplayName());
        nomTem = user.getDisplayName();
        correoTem = user.getEmail();
        imageTem = user.getPhotoUrl().toString();
        Glide.with(this).load(user.getPhotoUrl())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePerfil);


        String linea;
        try {
            InputStreamReader archivo = new InputStreamReader(
                    openFileInput("deportes.txt"));
            BufferedReader br = new BufferedReader(archivo);
            linea = br.readLine();
            String todo = "";
            while (linea != null) {
                Toast.makeText(this, linea, Toast.LENGTH_LONG).show();
                todo = todo + linea + "\n";
                linea = br.readLine();
            }
            br.close();

            archivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //emailTextView.setText(user.getEmail());
        //idTextView.setText(user.getUid());
        //Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }

    private void setUserData2(){
        /*Glide.with(MenuSporTec.this).load(R.drawable.ic_action_perfil)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePerfil);*/

        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select a picture for your profile"), CAMERA_REQUEST_CODE);
                }
            }
        });

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombre.setText(String.valueOf(dataSnapshot.child("Nombre").getValue()));
                String imageUrl = String.valueOf(dataSnapshot.child("FotoPerfil").getValue());
                if (URLUtil.isValidUrl(imageUrl))
                    Glide.with(MenuSporTec.this).load(imageUrl)
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(MenuSporTec.this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imagePerfil);
                    //Picasso.with(MenuSporTec.this).load(Uri.parse(imageUrl)).into(imageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Iniciar la comunicacion
        firebaseAuth.addAuthStateListener(mFirebaseAuthListener);

    }

    //cerrar sesion
    private void goLogInScreen() {
        Intent intent = new Intent(MenuSporTec.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(){
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cerrar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //terminar la comunicaci[on con firebase
    @Override
    protected void onStop() {
        super.onStop();

        if (mFirebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spor_tec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_noticias) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new NoticiasFragment()).commit();
        } else if (id == R.id.nav_deportes) {

        } else if (id == R.id.nav_resultados) {

        } else if (id == R.id.nav_retos) {

        } else if (id == R.id.nav_perfil) {
            if (tipo.equals("gm")){
                mostrarInfo();
            }
            else if(tipo.equals("cp")){
                fragmentManager.beginTransaction().replace(R.id.contenedor, new PerfilFragment()).commit();
            }
        } else if (id == R.id.nav_perfil_equipo) {

        }
        else if (id == R.id.nav_cerrar_sesion) {
            logOut();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            final Uri sourceUri = data.getData();
            Log.e("foto", sourceUri.toString());

            uploadImage(sourceUri);
        }
    }


    public void uploadImage(final Uri fileUri) {
        if (firebaseAuth.getCurrentUser() == null)
            return;

        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance().getReference();
        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final StorageReference filepath = mStorage.child("Photos").child(getRandomString());/*uri.getLastPathSegment()*/
        final DatabaseReference currentUserDB = mDatabase.child(firebaseAuth.getCurrentUser().getUid());

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
                                Toast.makeText(MenuSporTec.this, "Borrado de la imagen exitoso", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MenuSporTec.this, "Borrado de la imagen fallido", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                currentUserDB.child("FotoPerfil").removeEventListener(this);

                filepath.putFile(fileUri).addOnSuccessListener(MenuSporTec.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MenuSporTec.this, "Finalizado", Toast.LENGTH_SHORT).show();
                        Glide.with(MenuSporTec.this).load(fileUri)
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(MenuSporTec.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imagePerfil);
                        //Picasso.with(MenuSporTec.this).load(fileUri).fit().centerCrop().into(imageProfile);
                        DatabaseReference currentUserDB = mDatabase.child(firebaseAuth.getCurrentUser().getUid());
                        currentUserDB.child("FotoPerfil").setValue(downloadUri.toString());
                    }
                }).addOnFailureListener(MenuSporTec.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(MenuSporTec.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void mostrarInfo(){


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        
        
        //alertDialogBuilder.setIcon(R.drawable.ic_action_perfil);
        Glide.with(this)
                .load(imageTem)
                .into(new SimpleTarget<GlideDrawable>(125, 125) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        alertDialogBuilder.setIcon(resource);
                    }
                });
        
        // set title
        alertDialogBuilder.setTitle(nomTem);
        

        // set dialog message
        alertDialogBuilder
                .setMessage(correoTem)
                .setCancelable(false)
                .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
