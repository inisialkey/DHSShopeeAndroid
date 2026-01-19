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

import com.fungsitama.dhsshopee.model.ListPengirimanModel;
import com.fungsitama.dhsshopee.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListKendaraanAdapter extends RecyclerView.Adapter<ListKendaraanAdapter.ViewHolder> {

    private List<ListPengirimanModel> listPengirimanModels;
    private Context context;
    Onclick onclick,updateOtw,pindahactivity;

    public ListKendaraanAdapter(List<ListPengirimanModel> listPengirimanModels, Context context, Onclick onclick, Onclick updateOtw, Onclick pindahactivity) {
        this.listPengirimanModels = listPengirimanModels;
        this.context = context;
        this.onclick = onclick;
        this.updateOtw = updateOtw;
        this.pindahactivity = pindahactivity;
    }

    public interface Onclick {
        void onEvent(ListPengirimanModel modelItem, int pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_truck, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListPengirimanModel listdata = listPengirimanModels.get(position);

        String tanggalDibuat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getTransactionDate());

        holder.txtTanggalDbuat.setText(tanggalDibuat);
        holder.txtNoKendaraan.setText(listdata.getCodeVehicle());
        holder.txtKodeKendaraan.setText(listdata.getCodeDriver());
        holder.txtNamaSupir.setText(listdata.getNamaSupir());

        holder.txtTotalDO.setText(listdata.getQtyDO());
        holder.txtTotalBarcode.setText(listdata.getQtyBarcode());

        String status = context.getResources().getString(R.string.status_saat_ini);
        String btnUpdate = context.getResources().getString(R.string.btn_update_status);

        if (listdata.getStatus().equals("Pick-Up")){
            holder.btnUpdateStatus.setText(status + " (" + listdata.getStatus() +") - " + btnUpdate);
        }else if (listdata.getStatus().equals("Pick-Up Retur")){
            holder.btnUpdateStatus.setText(status + " (" + listdata.getStatus() +") - " + btnUpdate);
        }else if (listdata.getStatus().equals("OTW Retur")){
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
                }else if (listdata.getStatus().equals("Pick-Up Retur")){
                    //Toast.makeText(context, "Update ke OTW", Toast.LENGTH_SHORT).show();
                    updateOtw.onEvent(listdata,position);
                }else if (listdata.getStatus().equals("OTW Retur")){
                    //Toast.makeText(context, "Update ke OTW", Toast.LENGTH_SHORT).show();
                    updateOtw.onEvent(listdata,position);
                } if (listdata.getStatus().equals("OTW")){
                    onclick.onEvent(listdata,position);
                }
            }
        });

        holder.btnMapTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindahactivity.onEvent(listdata,position);
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

        public TextView txtTanggalDbuat;
        public TextView txtNoKendaraan;
        public TextView txtKodeKendaraan;

        public TextView txtTotalDO;
        public TextView txtTotalBarcode;

        public TextView txtNamaSupir;
        public TextView txtStatusKendaraan;
        public LinearLayout layoutDetail;

        public Button btnUpdateStatus,btnMapTracking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTanggalDbuat  =  (TextView) itemView.findViewById(R.id.txt_TanggalDbuat);
            txtNoKendaraan  =  (TextView) itemView.findViewById(R.id.txt_NoKendaraan);
            txtKodeKendaraan =  (TextView) itemView.findViewById(R.id.txt_KodeKendaraan);

            txtTotalDO =  (TextView) itemView.findViewById(R.id.txt_TotalDO);
            txtTotalBarcode =  (TextView) itemView.findViewById(R.id.txt_TotalBarcdoe);

            txtNamaSupir =  (TextView) itemView.findViewById(R.id.txt_NamaSupir);
            layoutDetail = (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            btnUpdateStatus = (Button) itemView.findViewById(R.id.btn_UpdateStatus);
            btnMapTracking = (Button) itemView.findViewById(R.id.btn_maping);

        }


    }
}