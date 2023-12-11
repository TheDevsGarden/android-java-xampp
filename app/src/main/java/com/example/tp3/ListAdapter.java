package com.example.tp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> items;
    private final ArrayList<String> listeDesc;
    private final ArrayList<String> listePrix;
    private final ArrayList<String> listeQte;
    private final ArrayList<Integer> itemIds;
    private final DeleteItemCallback deleteItemCallback;
    public interface DeleteItemCallback {
        void onDeleteItem(int id);
    }
    public ListAdapter(Context context, ArrayList<String> items,ArrayList<String> listeDesc,ArrayList<String> listePrix,ArrayList<String> listeQte, ArrayList<Integer> itemIds, DeleteItemCallback deleteItemCallback) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.items = items;
        this.listeDesc = listeDesc;
        this.listePrix = listePrix;
        this.listeQte = listeQte;
        this.itemIds = itemIds;
        this.deleteItemCallback = deleteItemCallback;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView itemName = rowView.findViewById(R.id.nom_item);
        TextView itemDescription = rowView.findViewById(R.id.item_description);
        TextView itemPrice = rowView.findViewById(R.id.item_price);
        TextView itemQuantity = rowView.findViewById(R.id.item_quantity);
        ImageView deleteIcon = rowView.findViewById(R.id.delete_icon);
        itemName.setText(items.get(position));
        itemDescription.setText(listeDesc.get(position));
        itemPrice.setText(listePrix.get(position));
        itemQuantity.setText(listeQte.get(position));
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(itemIds.get(position));
            }
        });
        return rowView;
    }

    private void deleteItem(int id) {
        deleteItemCallback.onDeleteItem(id);
    }
}
