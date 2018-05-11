package willvarela.sportec;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ActivityMain extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private TextView nameTextView;
    Button btLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTextView = (TextView) findViewById(R.id.textview_nombre);

        btLogOut = (Button) findViewById(R.id.logout);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                } else {
                    goLogInScreen();
                }
            }
        };

        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
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

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    private void goLogInScreen() {

        Intent intent = new Intent(this, ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mFirebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }
}
