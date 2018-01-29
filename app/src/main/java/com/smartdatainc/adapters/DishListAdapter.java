package com.smartdatainc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdatainc.dataobject.HotelMenuModal;
import com.smartdatainc.dataobject.HotelTableModal;
import com.smartdatainc.fudo.R;

import java.util.ArrayList;

/**
 * Created by aniketraut on 23/1/18.
 */

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<HotelMenuModal> dishModels;
    private ArrayList<HotelMenuModal> orderdMenuList = new ArrayList<>();

    public DishListAdapter(Context context, ArrayList<HotelMenuModal> dishModels) {
        this.context = context;
        this.dishModels = dishModels;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_dish, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HotelMenuModal dishModel = dishModels.get(position);

        holder.dishName.setText(dishModel.getMenuName());
        holder.dishDescription.setText(dishModel.getMenuDescription());
        holder.dishPrice.setText("Rs " + dishModel.getPrice());

        if (position % 2 == 0) {
                holder.mImageView.setBackgroundResource(R.drawable.restaurant_first);
        } else {
            holder.mImageView.setBackgroundResource(R.drawable.restaurant_second);
        }

        if (dishModel.getIsVeg() == 1) {
            holder.dishType.setText(context.getResources().getString(R.string.veg));
            holder.dishType.setTextColor(Color.GREEN);
        } else {
            holder.dishType.setText(context.getResources().getString(R.string.non_veg));
            holder.dishType.setTextColor(Color.RED);
        }

        holder.addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=holder.addDish.getText().toString();
                if(str.equalsIgnoreCase("Add")){
                    holder.addDish.setText("Remove");
                    orderdMenuList.add(position,dishModel);
                }else{
                    holder.addDish.setText("Add");
                    orderdMenuList.remove(position);
                }

            }
        });
    }

    public void filterList(ArrayList<HotelMenuModal> dishModels) {
        this.dishModels = dishModels;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dishModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dishName, dishDescription, dishPrice, addDish, dishType;
        private ImageView mImageView;

        public MyViewHolder(View view) {
            super(view);
            dishName = (TextView) view.findViewById(R.id.dish_name);
            dishDescription = (TextView) view.findViewById(R.id.dish_desc);
            dishPrice = (TextView) view.findViewById(R.id.dish_price);
            addDish = (TextView) view.findViewById(R.id.add_dish);
            dishType = (TextView) view.findViewById(R.id.menu_type);
            mImageView = (ImageView) view.findViewById(R.id.dish_image);

        }
    }

    public ArrayList<HotelMenuModal> getOrderdMenuList() {
        return orderdMenuList;
    }
}
