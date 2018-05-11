package willvarela.sportec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterResultados extends BaseAdapter {
    private Context context;
    private ArrayList<EntidadResuldatos> listaResultados;

    public AdapterResultados(Context context, ArrayList<EntidadResuldatos> listaResultados) {
        this.context = context;
        this.listaResultados = listaResultados;
    }


    @Override
    public int getCount() {
        return listaResultados.size();
    }

    @Override
    public Object getItem(int position) {
        return listaResultados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntidadResuldatos entidadResuldatos = (EntidadResuldatos) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item_resultados, null);

        TextView equipo1 = (TextView) convertView.findViewById(R.id.tv_equipo1);
        TextView equipo2 = (TextView) convertView.findViewById(R.id.tv_equipo2);
        TextView resultado1 = (TextView) convertView.findViewById(R.id.tv_resultado_equipo1);
        TextView resultado2 = (TextView) convertView.findViewById(R.id.tv_resultado_equipo2);

        equipo1.setText(entidadResuldatos.getEquipo1());
        equipo2.setText(entidadResuldatos.getEquipo2());
        resultado1.setText(String.valueOf(entidadResuldatos.getResultado1()));
        resultado2.setText(String.valueOf(entidadResuldatos.getResultado2()));

        return convertView;
    }
}
