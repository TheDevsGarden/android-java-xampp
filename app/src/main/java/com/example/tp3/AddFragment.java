package com.example.tp3;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText nom;
    EditText description;
    EditText prix;
    EditText qty;
    Button btn_submit;

    String url = "http://10.0.2.2:80/index.php";

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        nom = view.findViewById(R.id.nom);
        description = view.findViewById(R.id.description);
        prix = view.findViewById(R.id.prix);
        qty = view.findViewById(R.id.qty);

        btn_submit = view.findViewById(R.id.btn_sumbmit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerDonnees(nom.getText().toString(), description.getText().toString(), prix.getText().toString(), qty.getText().toString());
                clearForm();
            }
        });

        return view;
    }

    protected void clearForm(){
        EditText temp;
        final int[] txtId = new int[] {
                R.id.nom,
                R.id.description,
                R.id.prix,
                R.id.qty,
        };

        for (int i = 0; i < txtId.length; i++) {
            temp = getView().findViewById(txtId[i]);
            temp.setText(null);
        }
    }

    public void envoyerDonnees(String nom, String description, String prix, String qty) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RESPONSE", "DONNEES RECUES");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERREUR", error.toString());
            }
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("Data is mapped", "Data is mapped");
                Map<String, String> map = new HashMap<String, String>();
                map.put("nom", nom);
                map.put("description", description);
                map.put("prix", prix);
                map.put("quantite", qty);
                return map;
            }
        };

        //requestqueue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}