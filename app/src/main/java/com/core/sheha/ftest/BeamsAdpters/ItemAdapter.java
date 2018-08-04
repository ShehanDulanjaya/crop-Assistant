package com.core.sheha.ftest.BeamsAdpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.sheha.ftest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Ahsan ALi.
 */
public class ItemAdapter extends ArrayAdapter<ItemBean>{
    private final Context context;
    private final int resourceID;
    ArrayList<ItemBean> Items;
    public ItemAdapter(Context context, int resource, ArrayList<ItemBean> arr1) {
        super(context, resource,arr1);

        this.context = context;
        this.resourceID = resource;
        this.Items= arr1;
    }
    static class ViewHolder {
        protected ImageView img;
        protected TextView itemname;
        protected  TextView itemdecsp;
        protected  TextView itemprice;
        protected  String url;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {


        final ViewHolder viewHolder;

        if(view==null){

            LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.layout_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemname = (TextView) view.findViewById(R.id.ItemTitle);
            viewHolder.itemdecsp = (TextView) view.findViewById(R.id.ItemDecs);
            viewHolder.img = (ImageView) view.findViewById(R.id.ItemImg);
            viewHolder.itemprice = (TextView) view.findViewById(R.id.itemPrice);


            view.setTag(viewHolder);
            view.setTag(R.id.ItemTitle, viewHolder.itemname);
            view.setTag(R.id.ItemDecs, viewHolder.itemdecsp);
            view.setTag(R.id.itemPrice, viewHolder.itemprice);
            view.setTag(R.id.ItemImg, viewHolder.img);



        } else {

            viewHolder = (ViewHolder)view.getTag();

        }

        ItemBean ob= Items.get(position);


        //UrlImageViewHelper.setUrlDrawable(viewHolder.videoimage,videos.get(position).getImageUrl(),R.drawable.no_thumbnail);

        Glide.with(context).load(ob.getUrl()).into( viewHolder.img);
      //  viewHolder.img.setImageResource(ob.getImg());
        viewHolder.itemname.setText(""+ob.getItemname());
        viewHolder.itemdecsp.setText(""+ob.getItemdesc());
        viewHolder.itemprice.setText("Price "+ob.getPrice()+ "$");
        return view;
    }




}
