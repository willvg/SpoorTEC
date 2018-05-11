package willvarela.sportec;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class ActivitySelectSport extends AppCompatActivity {

    private Button btSeguir;
    private CheckBox mCbFutbol, mChbasket, mCbTenis, mChBeisbol, mCbVolleybol;
    private String allSport = "";
    private String tipo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tipo = getIntent().getExtras().getString("tipo");
        setContentView(R.layout.activity_select_sport);
        btSeguir = (Button) findViewById(R.id.bt_next);
        mCbFutbol = (CheckBox) findViewById(R.id.cb_futbol);
        mChbasket = (CheckBox) findViewById(R.id.cb_basketbol);
        mChBeisbol = (CheckBox) findViewById(R.id.cb_beisbol);
        mCbTenis = (CheckBox) findViewById(R.id.cb_tenis);
        mCbVolleybol = (CheckBox) findViewById(R.id.cb_volleybol);


        btSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(
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
                Intent intent = new Intent(ActivitySelectSport.this,MenuSporTec.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tipo", tipo);
                startActivity(intent);
            }
        });
    }
}
