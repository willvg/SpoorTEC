package willvarela.sportec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AdapterDeportes extends BaseAdapter {

    Context context;
    ArrayList<Deportes> listaDeportes;
    public AdapterDeportes(Context context, ArrayList<Deportes> listaDeportes) {
        this.context = context;
        this.listaDeportes = listaDeportes;
    }

    @Override
    public int getCount() {
        return listaDeportes.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDeportes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Deportes deportes =  (Deportes) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_deportes, null);

        TextView tvNombreDeporte = (TextView) convertView.findViewById(R.id.tv_nombre_deporte);
        ImageView ivImagenDeporte = (ImageView) convertView.findViewById(R.id.iv_imagen_deporte);

        Glide.with(context.getApplicationContext()).load(deportes.getImagen())
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImagenDeporte);

        tvNombreDeporte.setText(deportes.getNombreDeporte());


        return convertView;
    }
}
