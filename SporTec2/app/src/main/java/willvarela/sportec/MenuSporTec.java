package willvarela.sportec;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
    private int CAMERA_REQUEST_CODE = 0;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_spor_tec);
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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                    setUserData2();

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

    private void setUserData(FirebaseUser user) {


        nombre.setText(user.getDisplayName());


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
        Glide.with(MenuSporTec.this).load(R.drawable.ic_action_perfil)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePerfil);

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
                /*if (URLUtil.isValidUrl(imageUrl))
                    Glide.with(MenuSporTec.this).load(user.getPhotoUrl())
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(this))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imagePerfil);
                    Picasso.with(MenuSporTec.this).load(Uri.parse(imageUrl)).into(imageProfile);*/
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

        } else if (id == R.id.nav_perfil_equipo) {

        }
        else if (id == R.id.nav_cerrar_sesion) {
            logOut();
            //firebaseAuth.signOut();
            //Intent intent = new Intent(MenuSporTec.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivity(intent);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new CerrarSesionFragment()).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
