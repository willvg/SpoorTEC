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
        TextView descripcion = (TextView) convertView.findViewById(R.id.tv_descripcion_noticia);
        TextView fecha = (TextView) convertView.findViewById(R.id.tv_fecha_noticia);


        Glide.with(context).load(item.getImage())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        descripcion.setText(item.getDescripcion());
        fecha.setText(item.getFecha());

        return convertView;
    }
}
