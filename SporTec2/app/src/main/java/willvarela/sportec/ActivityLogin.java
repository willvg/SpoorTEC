package willvarela.sportec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ActivityLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    public static final int SIGN_IN_CODE = 777;

    private ProgressBar mProgressBar;
    private Button mBtRegistrar;

    private Button mBtInio;
    private EditText mEtCorreo, mEtPassword;

    private ProgressDialog mProgress;

    private String tipo= "cp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Context context = this;
        mBtRegistrar = (Button) findViewById(R.id.bt_registrar);
        mBtInio = (Button) findViewById(R.id.bt_inicio_sesion);
        mEtCorreo = (EditText) findViewById(R.id.et_correo);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mProgress = new ProgressDialog(this);

        mBtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegistrar.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //login con gmail
        mSignInButton = (SignInButton) findViewById(R.id.bt_login_Gmail);
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();

        mBtInio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            //Este metodo se ejecuta cuando cambia el estado de la utentificaci[on
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    readData();
                }
            }
        };

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void inicio() {
        String correo = mEtCorreo.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(password)){
            mProgress.setMessage("Iniciando, un momento");
            mProgress.show();
            mFirebaseAuth.signInWithEmailAndPassword(correo,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()){
                                //readData();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error no se pudo realizar el inicio", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        String linea;

        String[] archivos = fileList();

        if (existe(archivos, "deportes.txt")) {
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
            //File file = new File(getFilesDir(), "deportes.txt");
            //file.delete();
            Intent intent = new Intent(ActivityLogin.this, MenuSporTec.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tipo", tipo);
            startActivity(intent);
        }else{
            try {
                OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
                        "deportes.txt", Context.MODE_PRIVATE));
                //archivo.write("Deportes");
                archivo.flush();
                archivo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(ActivityLogin.this,ActivitySelectSport.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tipo", tipo);
            startActivity(intent);
        }
    }

    private boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            //tipo = "gm";
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            firebaseAuthWithGoogle(result.getSignInAccount());
            tipo = "gm";
        }
        else{
            Toast.makeText(this, R.string.not_init_sign, Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        mProgress.setMessage("Iniciando, un momento");
        mProgress.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgress.dismiss();

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_fire_auth, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }
}
