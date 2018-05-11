package willvarela.sportec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class FragmentsDeportes extends Fragment {
    private View view;
    private GridView mGvDeportes;
    private AdapterDeportes adapterDeportes;
    private ArrayList<Deportes> deportes;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deportes, container, false);
        getActivity().setTitle("Deportes");
        mGvDeportes = (GridView) view.findViewById(R.id.gv_deportes);


        llenarLista();

        mostrar();

        mGvDeportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityGridMultiple.class);
                intent.putExtra("objeto",deportes.get(position));
                startActivity(intent);
            }
        });

        return view;
    }

    private void mostrar() {
        adapterDeportes = new AdapterDeportes(getActivity().getApplicationContext(),deportes);
        mGvDeportes.setAdapter(adapterDeportes);
        adapterDeportes.notifyDataSetChanged();
    }

    private void llenarLista() {
        deportes = new  ArrayList<>();
        Deportes fut = new Deportes("Futbol",R.drawable.fut);
        Deportes bas = new Deportes("Basketbol",R.drawable.basket);
        Deportes beis = new Deportes("Beisbol",R.drawable.beis);
        Deportes tenis = new Deportes("Tenis",R.drawable.ten);
        Deportes valei = new Deportes("Voleibol",R.drawable.voleibol);

        deportes.add(fut);
        deportes.add(bas);
        deportes.add(beis);
        deportes.add(tenis);
        deportes.add(valei);
    }
}
