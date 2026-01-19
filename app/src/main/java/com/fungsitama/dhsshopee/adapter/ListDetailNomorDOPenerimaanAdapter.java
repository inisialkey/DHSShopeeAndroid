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

public class ListDetailNomorDOPenerimaanAdapter extends RecyclerView.Adapter<ListDetailNomorDOPenerimaanAdapter.ViewHolder> {

    private List<ListDetailNomorDOModel> listDetailNomorDOModels;
    private Context context;
    Onclick onclick;

    public interface Onclick {
        void onEvent(ListDetailNomorDOModel modelItem, int pos);
    }

    public ListDetailNomorDOPenerimaanAdapter(List<ListDetailNomorDOModel> listDetailNomorDOModels, Context context, Onclick onclick) {
        this.listDetailNomorDOModels = listDetailNomorDOModels;
        this.context = context;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_detail_nomor_d_o_penerimaan, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListDetailNomorDOModel listdata = listDetailNomorDOModels.get(position);

        String tanggalDibuat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getCreatedAt());

        holder.txtNoDO.setText(listdata.getDoNumber());
        holder.txtTujuan.setText(listdata.getTujuan());
        holder.txtQrCode.setText(listdata.getQrcode());
        holder.txtStatusKendaraan.setText(listdata.getStatus());

        if (listdata.getStatus().equals("Delivered")){
            holder.status_Penerimaan.setImageResource(R.drawable.un_check_list);
        }else if (listdata.getStatus().equals("In-DC")) {
            holder.status_Penerimaan.setImageResource(R.drawable.check_list);
        }else if (listdata.getStatus().equals("Out-DC")) {
            holder.status_Penerimaan.setImageResource(R.drawable.out_chek_list);
        }else{
            holder.status_Penerimaan.setImageResource(R.drawable.retur_chek_list);
        }

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
        public TextView txtTujuan;
        public TextView txtQrCode;
        public TextView txtStatusKendaraan;
        public LinearLayout layoutDetail;

        public ImageView status_Penerimaan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNoDO  =  (TextView) itemView.findViewById(R.id.txt_noDO);
            txtTujuan =  (TextView) itemView.findViewById(R.id.txt_Tujuan);
            txtQrCode =  (TextView) itemView.findViewById(R.id.txt_QrCode);
            txtStatusKendaraan =  (TextView) itemView.findViewById(R.id.txt_StatusKendaraan);
            layoutDetail = (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            status_Penerimaan = (ImageView) itemView.findViewById(R.id.imgView_status_penerimaan);
        }

    }
}