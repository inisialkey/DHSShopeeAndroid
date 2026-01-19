package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListPengirimanModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListKendaraanHomeAdapter extends RecyclerView.Adapter<ListKendaraanHomeAdapter.ViewHolder> {

    private List<ListPengirimanModel> listPengirimanModels;
    private Context context;
    Onclick onclick,updateOtw;

    public interface Onclick {
        void onEvent(ListPengirimanModel modelItem, int pos);
    }

    public ListKendaraanHomeAdapter(List<ListPengirimanModel> listPengirimanModels, Context context, Onclick onclick, Onclick updateOtw) {
        this.listPengirimanModels = listPengirimanModels;
        this.context = context;
        this.onclick = onclick;
        this.updateOtw = updateOtw;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_truck_home, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListPengirimanModel listdata = listPengirimanModels.get(position);

        String tanggalDibuat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getTransactionDate());


        holder.txtNoKendaraan.setText(listdata.getCodeVehicle());

        String status = context.getResources().getString(R.string.status_saat_ini);
        String btnUpdate = context.getResources().getString(R.string.btn_update_status);

        if (listdata.getStatus().equals("Pick-Up")){
            holder.btnUpdateStatus.setText(status + " (" + listdata.getStatus() +") - " + btnUpdate);
        }else {
            holder.btnUpdateStatus.setText(status + " (" + listdata.getStatus() +")");
        }



        holder.btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Update ke " + listdata.getStatus(), Toast.LENGTH_SHORT).show();

                if (listdata.getStatus().equals("Pick-Up")){
                    //Toast.makeText(context, "Update ke OTW", Toast.LENGTH_SHORT).show();
                    updateOtw.onEvent(listdata,position);
                }else if (listdata.getStatus().equals("OTW")){
                    onclick.onEvent(listdata,position);
                }
            }
        });


        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onEvent(listdata,position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listPengirimanModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtNoKendaraan;
        public TextView txtStatusKendaraan;
        public LinearLayout layoutDetail;

        public Button btnUpdateStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNoKendaraan  =  (TextView) itemView.findViewById(R.id.txt_NoKendaraan);
            layoutDetail = (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            btnUpdateStatus = (Button) itemView.findViewById(R.id.btn_UpdateStatus);

        }


    }
}