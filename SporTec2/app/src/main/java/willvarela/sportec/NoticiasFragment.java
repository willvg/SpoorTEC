package willvarela.sportec;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class NoticiasFragment extends Fragment {

    View view;
    private TextView nameTextView;
    Button btLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_noticias, container, false);

        //boton para salir
        btLogOut = (Button) view.findViewById(R.id.logout);
        nameTextView = (TextView) view.findViewById(R.id.textview_nombre);
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firebaseAuth.signOut();

            }
        });
        return view;
    }


}
