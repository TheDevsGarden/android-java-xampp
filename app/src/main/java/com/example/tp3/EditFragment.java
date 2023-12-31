package com.example.tp3;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment implements ListAdapter.DeleteItemCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    ArrayList<String> listeItems = new ArrayList<>();
    ArrayList<String> listeDesc = new ArrayList<>();
    ArrayList<String> listePrix = new ArrayList<>();
    ArrayList<String> listeQte = new ArrayList<>();
    ArrayList<Integer> ListeIdItems = new ArrayList<>();

    String url = "http://10.0.2.2:80/afficherItems.php";
    String deleteUrl = "http://10.0.2.2:80/deleteItem.php";
    String updateUrl = "http://10.0.2.2:80/updateItem.php";


    public EditFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        listView = view.findViewById(R.id.listView);
        fetchItems();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                montrerEditDialog(position);
            }
        });
        return view;
    }

    private void fetchItems() {
        listeItems.clear();
        ListeIdItems.clear();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String item = jsonObject.getString("nom");
                        String description = jsonObject.getString("description");
                        String prix = jsonObject.getString("prix");
                        String quantite = jsonObject.getString("quantité");
                        int id = jsonObject.getInt("idArticle");
                        listeItems.add(item);
                        listeDesc.add(description);
                        listePrix.add(prix);
                        listeQte.add(quantite);
                        ListeIdItems.add(id);
                    }
                    ListAdapter adapter = new ListAdapter(getActivity(), listeItems, listeDesc, listePrix, listeQte, ListeIdItems, EditFragment.this);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.i("EDITFRAGMENT", "EROR LINE 109");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("EDITFRAGMENT", "EROR LINE 115");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void deleteItem(int id) {
        StringRequest request = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("EDITFRAGMENT", "ONRESPNSE OK");
                fetchItems();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EDITFRAGMENT", "ONRESPNSE NOT OK, ERROR");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                Log.i("EDITFRAGMENT", "Request OK");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void montrerEditDialog(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_modifier_item, null);
        dialogBuilder.setView(dialogView);

        EditText editNom = dialogView.findViewById(R.id.edit_nom);
        EditText editDescription = dialogView.findViewById(R.id.edit_description);
        EditText editPrix = dialogView.findViewById(R.id.edit_prix);
        EditText editQuantite = dialogView.findViewById(R.id.edit_quantite);
        Button buttonUpdate = dialogView.findViewById(R.id.bouton_update);

        editNom.setText(listeItems.get(position));
        editDescription.setText(listeDesc.get(position));
        editPrix.setText(listePrix.get(position));
        editQuantite.setText(listeQte.get(position));

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem(ListeIdItems.get(position), editNom.getText().toString(), editDescription.getText().toString(), editPrix.getText().toString(), editQuantite.getText().toString());
                alertDialog.dismiss();
            }
        });
    }

    private void updateItem(int id, String nom, String description, String prix, String quantite) {

        StringRequest request = new StringRequest(Request.Method.POST, updateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchItems();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EDITFRAGMENT", "ERREUR LIGNE 204");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("nom", nom);
                params.put("description", description);
                params.put("prix", prix);
                params.put("quantite", quantite);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    @Override
    public void onDeleteItem(int id) {
        deleteItem(id);
    }
}