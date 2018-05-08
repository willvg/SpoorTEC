package willvarela.sportec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdapterItems extends BaseAdapter {
    private Context context;
    private ArrayList<Entidad> listaItems;


    public AdapterItems(Context context, ArrayList<Entidad> listaItems) {
        this.context = context;
        this.listaItems = listaItems;
    }

    @Override
    public int getCount() {
        return listaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entidad item = (Entidad) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_noticias, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.iv_imagen_noticia);
        TextView titulo = (TextView) convertView.findViewById(R.id.tv_titulo_noticia);
        TextView fecha = (TextView) convertView.findViewById(R.id.tv_fecha_noticia);
        TextView descripcion  = (TextView) convertView.findViewById(R.id.tv_description_noticia);

        //Uri uri =  Uri.parse("https://firebasesto...-8334-c8c3f409d310");
        //image.setImageURI(uri);
        Glide.with(context.getApplicationContext()).load(item.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        /*Glide.with(context)
                .load()
                .into(image);*/
        titulo.setText(item.getTitulo());
        fecha.setText(item.getFecha());

        return convertView;
    }
}
