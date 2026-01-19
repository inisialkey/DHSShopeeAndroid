package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
import com.fungsitama.dhsshopee.model.ListDaftarLoadingModel;

import java.util.List;

public class ListDaftarLoadingAdapter extends RecyclerView.Adapter<ListDaftarLoadingAdapter.ViewHolder> {

    private List<ListDaftarLoadingModel> listDetailNomorDOModels;
    private Context context;
    Onclick onclick,ondelete,onDetail;

    public interface Onclick {
        void onEvent(ListDaftarLoadingModel modelItem, int pos);
    }

    public ListDaftarLoadingAdapter(List<ListDaftarLoadingModel> listDetailNomorDOModels, Context context, Onclick onclick, Onclick ondelete,Onclick onDetail) {
        this.listDetailNomorDOModels = listDetailNomorDOModels;
        this.context = context;
        this.onclick = onclick;
        this.ondelete = ondelete;
        this.onDetail = onDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_daftar_loading, viewGroup, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListDaftarLoadingModel listdata = listDetailNomorDOModels.get(position);


        holder.noTransaksi.setText(listdata.getTransNumber());
        holder.status.setText(listdata.getStatus());
        holder.noTruck.setText(listdata.getCodeVehicle());
        holder.namaSupir.setText(listdata.getNameDriver());
        holder.txtTotalBarcdoe.setText(listdata.getQrcode());

        if (listdata.getLoadType().equals("null")){
            holder.txt_LoadType.setText("FCL");
        }else{
            holder.txt_LoadType.setText(listdata.getLoadType());
        }

        if (listdata.getNameOrigin().equals("null") || listdata.getNameDestination().equals("null")){
            holder.txt_asalTujuan.setText(" - ");
        }else {
            holder.txt_asalTujuan.setText(listdata.getNameOrigin() + " - " + listdata.getNameDestination());
        }

        if (listdata.getNameTransit().equals("null")){
            holder.txtTransit.setText(" - ");
        }else {
            holder.txtTransit.setText(listdata.getNameTransit());
        }

        /*if (listdata.getStatus().equals("Loading")){
            holder.btn_Detail.setText("Detail Loading");
        }else{
            holder.btn_Detail.setText("Detail Unloading");
        }*/



        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onEvent(listdata,position);
            }
        });

        holder.layoutDetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ondelete.onEvent(listdata,position);
                return false;
            }
        });

        holder.btn_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetail.onEvent(listdata,position);
            }
        });


        

    }


    @Override
    public int getItemCount() {
        return listDetailNomorDOModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView noTransaksi;
        public TextView status;
        public TextView noTruck;
        public TextView namaSupir;
        public TextView txt_asalTujuan;
        public TextView txtTransit;
        public TextView txtTotalBarcdoe;
        public TextView txt_LoadType;

        public LinearLayout layoutDetail;

        public AppCompatButton btn_Detail;

        public ImageView img_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noTransaksi  =  (TextView) itemView.findViewById(R.id.txt_noTrans);
            status  =  (TextView) itemView.findViewById(R.id.txt_Status);
            noTruck  =  (TextView) itemView.findViewById(R.id.txt_noTruck);
            namaSupir  =  (TextView) itemView.findViewById(R.id.txt_NamaSupir);
            txt_asalTujuan  =  (TextView) itemView.findViewById(R.id.txt_asalTujuan);
            txtTransit  =  (TextView) itemView.findViewById(R.id.txt_transit);
            txtTotalBarcdoe  =  (TextView) itemView.findViewById(R.id.txt_TotalBarcdoe);
            txt_LoadType  =  (TextView) itemView.findViewById(R.id.txt_LoadType);

            layoutDetail  =  (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            btn_Detail  =  (AppCompatButton) itemView.findViewById(R.id.btn_Detail);

            img_status = (ImageView) itemView.findViewById(R.id.imgView_status);

        }


    }
}