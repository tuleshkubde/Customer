package com.smartdatainc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartdatainc.activities.TableActivity;
import com.smartdatainc.dataobject.HotelModal;
import com.smartdatainc.dataobject.HotelTableModal;
import com.smartdatainc.fudo.R;

import java.util.ArrayList;

/**
 * Created by aniketraut on 23/1/18.
 */

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<HotelTableModal> hotelTableModalList;

    public TableListAdapter(Context context, ArrayList<HotelTableModal> hotelTableModalList) {
        this.context = context;
        this.hotelTableModalList = hotelTableModalList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_table_list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HotelTableModal hotelTableModal = hotelTableModalList.get(position);
        holder.hotelName.setText(hotelTableModal.getHotelName());
        holder.totalCapacity.setText("Table Remaining :"+String.valueOf(hotelTableModal.getTableAvailable()));


    /*    holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TableActivity
                        .class).putExtra("hotel",hotelModel.getId()));
            }
        });
*/
    }

    public void filterList(ArrayList<HotelTableModal> hotelTableModalList) {
        this.hotelTableModalList = hotelTableModalList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return hotelTableModalList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView hotelName,totalTable,totalCapacity;


        public MyViewHolder(View view) {
            super(view);
            hotelName = (TextView) view.findViewById(R.id.txtHotelName);
            totalTable = (TextView) view.findViewById(R.id.totalTable);
            totalCapacity = (TextView) view.findViewById(R.id.tableCapacity);


        }
    }
}
