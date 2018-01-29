package com.smartdatainc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartdatainc.adapters.DishListAdapter;
import com.smartdatainc.dataobject.AppInstance;
import com.smartdatainc.dataobject.HotelMenuModal;
import com.smartdatainc.dataobject.HotelModal;
import com.smartdatainc.dataobject.HotelTableModal;
import com.smartdatainc.dataobject.OrderDetailsEntities;
import com.smartdatainc.fudo.R;
import com.smartdatainc.interfaces.ServiceRedirection;
import com.smartdatainc.managers.HotelMenuManager;
import com.smartdatainc.managers.OrderManager;
import com.smartdatainc.utils.Constants;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DishListActivity extends BaseActivity implements ServiceRedirection,View.OnClickListener {

    private RecyclerView hotelList;
    private ArrayList<HotelMenuModal> dishModels;
    private HotelMenuModal dishModel;
    private DishListAdapter dishListAdapter;
    private Button btnConfirmOrder;
    private HotelModal hotelModal;
    private HotelTableModal hotelTableModal;
    private OrderManager orderManager;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnConfirmOrder =(Button)findViewById(R.id.btnConfirmOrder);
        btnConfirmOrder.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        hotelList = (RecyclerView) findViewById(R.id.hotel_list);
        dishModels = new ArrayList<>();
        dishListAdapter = new DishListAdapter(DishListActivity.this, dishModels);
        hotelList.setHasFixedSize(true);

        hotelModal=getIntent().getParcelableExtra("hotel");
        hotelTableModal=getIntent().getParcelableExtra("table");

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(DishListActivity.this);
        hotelList.setLayoutManager(recyclerViewLayoutManager);
        hotelList.setItemAnimator(new DefaultItemAnimator());
        hotelList.setAdapter(dishListAdapter);

        orderManager = new OrderManager(this, this);
//        hotelList.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//
//                        startActivity(new Intent(DishListActivity.this,CartItemListActivity.class));
//                    }
//                })
//        );


        etSearch=(EditText)findViewById(R.id.etSearch);
        etSearch.setSingleLine(true);
        etSearch.setHint("Search Menu Item by Name");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });


    }



    private void filter(String text) {

        if(dishModels!=null && dishModels.size()!=0){
            ArrayList<HotelMenuModal> filterdNames = new ArrayList<>();

            //looping through existing elements
            for (int i=0;i<dishModels.size();i++) {
                String s=dishModels.get(i).getMenuName();
                //if the existing elements contains the search input
                if (s.toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(dishModels.get(i));
                }
            }

            //calling a method of the adapter class and passing the filtered list
            dishListAdapter.filterList(filterdNames);
        }
        //new array list that will hold the filtered data

    }


    @Override
    protected void onResume() {
        super.onResume();
        dishModels.clear();
        loadMenuList();
    }

    private void loadMenuList(){
        HotelMenuManager hotelListManager = new HotelMenuManager(DishListActivity.this, DishListActivity.this);
        hotelListManager.getHotelMenuList(hotelModal);
    }

    @Override
    public void onSuccessRedirection(int taskID) {
        if (taskID == Constants.TaskID.GET_HOTEL_MENU_LIST_TASK_ID) {
            //AppInstance.hotelModal = (HotelModal) dataReceived;
            dishModels.addAll(AppInstance.hotelMenuModal);
            dishListAdapter.notifyDataSetChanged();



        }else if(taskID == Constants.TaskID.POST_CONFIRM_ORDER){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Order ID "+AppInstance.orderSearchModelList.get(0).getOrderID()+" has been proccessed successfully")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            //do things
                            Intent intent = new Intent(DishListActivity.this, HotelListActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // call this to finish the current activity
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onFailureRedirection(String errorMessage) {

        Toast.makeText(this, "Failue Error : "+errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirmOrder:
                    openConfirmDialog();
                break;

            default:
                break;
        }
    }

    public void openConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Order Confirmation");

        final View dialogView = inflater.inflate(R.layout.dialog_confirm_order, null);

        final TextInputLayout textInputLayout = (TextInputLayout) dialogView.findViewById(R.id.emailLayout);
        final EditText edt = (EditText) dialogView.findViewById(R.id.etEmail);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the use  r ...

                        String email = edt.getText().toString().trim();
                        OrderDetailsEntities orderDetailsEntities = new OrderDetailsEntities();
                        orderDetailsEntities.setHotelId(hotelModal.getId());
                        orderDetailsEntities.setTableID(hotelTableModal.getId());
                        orderDetailsEntities.setTotalQuantity(0);
                        orderDetailsEntities.setEmailID("");
                        //orderDetailsEntities.setEmailID(email);
                        orderDetailsEntities.setHotelMenuModal(dishListAdapter.getOrderdMenuList());
                        orderDetailsEntities.setTotalAmount(getTotalPrice(orderDetailsEntities.getHotelMenuModal()));



                        orderManager.ConfirmOrder(orderDetailsEntities);
                        /*if(!TextUtils.isEmpty(email)){
                            textInputLayout.setError("Email ID Requied");
                        }else if(isValidEmaillId(email)){
                            textInputLayout.setError("Please provide valid email ID Address");

                        }else if(dishListAdapter.getOrderdMenuList().size()==0){
                            Toast.makeText(DishListActivity.this, "Please add atleast one menu item", Toast.LENGTH_SHORT).show();
                        }else {

                        }*/

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }


    private boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public double getTotalPrice(ArrayList<HotelMenuModal> menuList){
        double total_sum=0;

        for(int i=0;i<menuList.size();i++){
            total_sum=total_sum+menuList.get(i).getPrice();
        }

        return total_sum;

    }
}
