package com.example.tp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> items;
    private final ArrayList<Integer> itemIds;

    private final DeleteItemCallback deleteItemCallback;

    public interface DeleteItemCallback {
        void onDeleteItem(int id);
    }
    public ListAdapter(Context context, ArrayList<String> items, ArrayList<Integer> itemIds, DeleteItemCallback deleteItemCallback) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.items = items;
        this.itemIds = itemIds;
        this.deleteItemCallback = deleteItemCallback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView itemName = rowView.findViewById(R.id.item_name);
        ImageView deleteIcon = rowView.findViewById(R.id.delete_icon);

        itemName.setText(items.get(position));

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(itemIds.get(position));
            }
        });

        return rowView;
    }

    private void deleteItem(int id) {
        // Call the onDeleteItem method of the DeleteItemCallback
        deleteItemCallback.onDeleteItem(id);
    }
}
