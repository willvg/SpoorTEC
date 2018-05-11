package willvarela.sportec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterGanados extends BaseAdapter {
    Context context;
    ArrayList<EntidadEquipo> listaEquipos;

    public AdapterGanados(Context context, ArrayList<EntidadEquipo> listaEquipos) {
        this.context = context;
        this.listaEquipos = listaEquipos;
    }

    @Override
    public int getCount() {
        return listaEquipos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaEquipos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EntidadEquipo equipo = (EntidadEquipo) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_ganados, null);

        TextView nombre = (TextView) convertView.findViewById(R.id.tv_ganados_equipo);
        TextView valor = (TextView) convertView.findViewById(R.id.tv_ganados_resultado);


        Button retar = (Button) convertView.findViewById(R.id.bt_ganados_retar);

        nombre.setText(equipo.getNombre());
        valor.setText(String.valueOf(equipo.getGanados()));

        //falta lo del boton

        retar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final CharSequence[] items = new CharSequence[3];


                items[0] = "Soltero/a";
                items[1] = "Casado/a";
                items[2] = "Divorciado/a";

                builder.setTitle("Estado Civil")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(
                                        context,
                                        "Seleccionaste: " + items[which],
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();*/
            }
        });



        return convertView;
    }

    private void llenarIntem(){

    }
}
