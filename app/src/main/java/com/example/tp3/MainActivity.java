package com.example.tp3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    EditText nom;
    EditText description;
    EditText prix;
    EditText qty;
    RadioButton radio_ajouter;
    RadioButton radio_modifier;
    Button btn_submit;

   String url = "http://10.0.2.2:80/index.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = (EditText) findViewById(R.id.nom);
        description = (EditText) findViewById(R.id.description);
        prix = (EditText) findViewById(R.id.prix);
        qty = (EditText) findViewById(R.id.qty);

        radio_ajouter = (RadioButton) findViewById(R.id.radio_ajouter);
        radio_modifier = (RadioButton) findViewById(R.id.radio_modifier);
        btn_submit = (Button) findViewById(R.id.btn_sumbmit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerDonnees(nom.getText().toString(), description.getText().toString(), prix.getText().toString(), qty.getText().toString());
                clearForm();
            }
        });
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
            temp = (EditText) findViewById(txtId[i]);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}



