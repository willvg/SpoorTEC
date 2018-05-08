package willvarela.sportec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private ProgressDialog mProgress;

    private Button mBtRegistrar;
    private EditText mEtNombre, mEtCorreo, mEtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mBtRegistrar = (Button) findViewById(R.id.bt_inicio_sesion_registrar);
        mEtNombre = (EditText) findViewById(R.id.et_nombre_registrar);
        mEtCorreo = (EditText) findViewById(R.id.et_correo_registrar);
        mEtpassword = (EditText) findViewById(R.id.et_password_registrar);


        mFirebaseAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mBtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar() {
        final String nombre = mEtNombre.getText().toString().trim();
        final String correo = mEtCorreo.getText().toString().trim();
        final String password = mEtpassword.getText().toString().trim();

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(correo) && !TextUtils.isEmpty(password)){
            mProgress.setMessage("Registrando, un momento");
            mProgress.show();

            mFirebaseAuth.createUserWithEmailAndPassword(correo,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()){
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user");
                                DatabaseReference currentUserDB = mDatabase.child(mFirebaseAuth.getCurrentUser().getUid());
                                currentUserDB.child("Nombre").setValue(nombre);
                                currentUserDB.child("FotoPerfil").setValue("default");

                                Toast.makeText(getApplicationContext(), "Registro con exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error no se pudo realizar el registro", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        }

    }
}
