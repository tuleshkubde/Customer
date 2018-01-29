package com.smartdatainc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartdatainc.dataobject.HotelTableModal;
import com.smartdatainc.dataobject.OrderSearchModel;
import com.smartdatainc.fudo.R;

import java.util.ArrayList;

/**
 * Created by aniketraut on 23/1/18.
 */

public class SearchOrderListAdapter extends RecyclerView.Adapter<SearchOrderListAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<OrderSearchModel> orderSearchModelList;

    public SearchOrderListAdapter(Context context, ArrayList<OrderSearchModel> orderSearchModelList) {
        this.context = context;
        this.orderSearchModelList = orderSearchModelList;
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
        final OrderSearchModel orderSearchModel = orderSearchModelList.get(position);
        holder.hotelName.setText("Order ID :"+String.valueOf(orderSearchModel.getOrderID()));
        holder.totalTable.setText("Name :"+orderSearchModel.getApprovalName());

        String status = "NA";
        int color=Color.RED;

        if(orderSearchModel.getIsApproveStatus()==1){
            status="Pending";
        }else if(orderSearchModel.getIsApproveStatus()==0){
            status="Confirmed";
        }else {
            status="Rejected";
            color=Color.GREEN;
        }
        holder.totalCapacity.setText("Order Status :"+status);
        holder.totalCapacity.setTextColor(color);


    /*    holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TableActivity
                        .class).putExtra("hotel",hotelModel.getId()));
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return orderSearchModelList.size();
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
