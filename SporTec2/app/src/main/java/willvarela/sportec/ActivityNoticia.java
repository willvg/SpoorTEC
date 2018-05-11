package willvarela.sportec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ActivityNoticia extends AppCompatActivity {

    private Entidad entidad;

    private TextView titulo, fecha, descripcion;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        image = (ImageView) findViewById(R.id.iv_imagen_n);
        titulo = (TextView) findViewById(R.id.tv_titulo_n);
        fecha = (TextView) findViewById(R.id.tv_fecha_n);
        descripcion = (TextView) findViewById(R.id.tv_description_n);

        entidad = (Entidad) getIntent().getSerializableExtra("objeto");

        Glide.with(this).load(entidad.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        titulo.setText(entidad.getTitulo());
        descripcion.setText(entidad.getDescripcion());
        fecha.setText(entidad.getFecha());
    }
}
