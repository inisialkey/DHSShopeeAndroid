package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListDetailNomorDOModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListDetailNomorDOAdapter extends RecyclerView.Adapter<ListDetailNomorDOAdapter.ViewHolder> {

    private List<ListDetailNomorDOModel> listDetailNomorDOModels;
    private Context context;
    Onclick onclick;

    public interface Onclick {
        void onEvent(ListDetailNomorDOModel modelItem, int pos);
    }

    public ListDetailNomorDOAdapter(List<ListDetailNomorDOModel> listDetailNomorDOModels, Context context, Onclick onclick) {
        this.listDetailNomorDOModels = listDetailNomorDOModels;
        this.context = context;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_detail_nomor_d_o, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListDetailNomorDOModel listdata = listDetailNomorDOModels.get(position);

        String tanggalDibuat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getCreatedAt());

        holder.txtNoDO.setText(listdata.getDoNumber());

       /* if (listdata.getStatus().equals("Pick-Up") || listdata.getStatus().equals("OTW") || listdata.getStatus().equals("Delivered")){
            holder.txtTitleTujuan.setVisibility(View.VISIBLE);
            holder.txtTujuan.setVisibility(View.VISIBLE);
            holder.txtTujuan.setText(listdata.getTujuan());
        }else{
            holder.txtTitleAsal.setVisibility(View.VISIBLE);
            holder.txtAsal.setVisibility(View.VISIBLE);
            holder.txtAsal.setText(listdata.getAsal());
        }*/




        holder.txtQrCode.setText(listdata.getQrcode());

        if (listdata.getStatus().equals("Pick-Up") || listdata.getStatus().equals("Pick-Up Retur")){
            holder.img_status.setImageResource(R.drawable.pickup_chek_list);
        }else if (listdata.getStatus().equals("OTW")) {
            holder.img_status.setImageResource(R.drawable.otw_chek_list);
        }else if (listdata.getStatus().equals("OTW Retur")) {
            holder.img_status.setImageResource(R.drawable.otwretur_chek_list);
        }else{
            holder.img_status.setImageResource(R.drawable.delivered_chek_list);
        }

        holder.txtStatusKendaraan.setText(listdata.getStatus());

        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onEvent(listdata,position);
            }
        });


        

    }


    @Override
    public int getItemCount() {
        return listDetailNomorDOModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNoDO;
        public TextView txtTitleAsal;
        public TextView txtAsal;
        public TextView txtTitleTujuan;
        public TextView txtTujuan;
        public TextView txtQrCode;
        public TextView txtStatusKendaraan;
        public LinearLayout layoutDetail;

        public ImageView img_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNoDO  =  (TextView) itemView.findViewById(R.id.txt_noDO);
            txtTitleAsal =  (TextView) itemView.findViewById(R.id.title_asal);
            txtAsal =  (TextView) itemView.findViewById(R.id.txt_Asal);
            txtTitleTujuan =  (TextView) itemView.findViewById(R.id.title_tujuan);
            txtTujuan =  (TextView) itemView.findViewById(R.id.txt_Tujuan);
            txtQrCode =  (TextView) itemView.findViewById(R.id.txt_QrCode);
            txtStatusKendaraan =  (TextView) itemView.findViewById(R.id.txt_StatusKendaraan);
            layoutDetail = (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            img_status = (ImageView) itemView.findViewById(R.id.imgView_status);

        }


    }
}