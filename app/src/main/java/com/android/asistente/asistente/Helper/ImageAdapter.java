package com.android.asistente.asistente.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.asistente.asistente.Entities.Phone;
import com.android.asistente.asistente.R;


import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int icons[];
    private List<Phone> listContacts;
    private LayoutInflater inflater;
    private int icons2[];
    private String letters[];
    private int Cant[];
    private boolean bStock=false;
    View gridView= null;

    public ImageAdapter(Context context, int icons[], List<Phone> list) {
        this.context = context;
        this.icons = icons;
        if(list != null) {
          //  icons2 = new int[categorias.size()];
            letters = new String[list.size()];
            Cant =  new int[list.size()];
            int i=0;
            this.listContacts = list;
            for (Phone item: list) {
               // icons2[i] = Integer.valueOf(item.Imagen);
                letters[i] = item.ID;
               // Cant[i] = item.Cant;

                i++;
            }
        }

    }
    public ImageAdapter(Context context, List<Phone> listContacts) {
        this.context = context;
        this.icons = icons;
        this.listContacts = listContacts;

    }

    public int getCount() {
        return this.listContacts.size();
    }

    public Object getItem(int position) {
        return listContacts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        try {
            gridView = convertView;
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                gridView = inflater.inflate(R.layout.activity_grid_image, null);
            }
            if (listContacts == null) {
                //ImageView icon = (ImageView) gridView.findViewById(R.id.icons);
               // icon.setImageResource(icons[i]);
            } else {
                 //   ImageView icon = (ImageView) gridView.findViewById(R.id.icons);
                   TextView letter = (TextView) gridView.findViewById(R.id.letters);
                   //  TextView stock = (TextView) gridView.findViewById(R.id.Stock);

                //if(i < icons2.length) {
                    //icon.setImageResource(icons[i]);
                    letter.setText(letters[i]);
                    //stock.setText(String.valueOf(bStock==true?Cant[i]:""));
              //  }


            }
            return gridView;
        }catch(Exception ex) {
         throw ex;
        }
    }


}