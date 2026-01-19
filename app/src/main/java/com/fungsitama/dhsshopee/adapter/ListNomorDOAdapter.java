package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListNomorDOModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListNomorDOAdapter extends RecyclerView.Adapter<ListNomorDOAdapter.ViewHolder> {

    private List<ListNomorDOModel> listNomorDOModels;
    private Context context;
    Onclick scanListDetailDo,detailDO;

    public interface Onclick {
        void onEvent(ListNomorDOModel modelItem, int pos);
    }

    public ListNomorDOAdapter(List<ListNomorDOModel> listNomorDOModels, Context context, Onclick scanListDetailDo, Onclick detailDO) {
        this.listNomorDOModels = listNomorDOModels;
        this.context = context;
        this.scanListDetailDo = scanListDetailDo;
        this.detailDO = detailDO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_nomor_d_o, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListNomorDOModel listdata = listNomorDOModels.get(position);

        String tanggalDibuatDO = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getCreatedAt());

        holder.txtNoDo.setText(listdata.getDoNumber());

        if (listdata.getStatus().equals("Pick-Up") || listdata.getStatus().equals("OTW") || listdata.getStatus().equals("Delivered")){
            holder.txtTujuan.setText(listdata.getNameDestination());
            holder.txtTitleTujuan.setVisibility(View.VISIBLE);
            holder.fLayoutTujuan.setVisibility(View.VISIBLE);

        }else{
            holder.txtAsal.setText(listdata.getNameOrigin());
            holder.txtTitleAsal.setVisibility(View.VISIBLE);
            holder.fLayoutAsal.setVisibility(View.VISIBLE);
        }

        holder.txtStatus.setText(listdata.getStatus());
        holder.txtKoli.setText(listdata.getKoli() + " Koli");
        holder.txtBerat.setText(listdata.getKg() + " Kg");
        holder.txtKuantitas.setText(listdata.getKty());
        holder.txtTotalQrCode.setText(listdata.getTotalQrCode());

        holder.btnScanDetailDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanListDetailDo.onEvent(listdata,position);
            }
        });

        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDO.onEvent(listdata,position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return listNomorDOModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNoDo;

        public TextView txtTitleAsal;
        public FrameLayout fLayoutAsal;
        public TextView txtTitleTujuan;
        public FrameLayout fLayoutTujuan;

        public TextView txtAsal;
        public TextView txtTujuan;
        public TextView txtStatus;
        public TextView txtKoli;
        public TextView txtBerat;
        public TextView txtKuantitas;
        public TextView txtTotalQrCode;

        public Button btnScanDetailDo;
        public LinearLayout layoutDetail;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNoDo  =  (TextView) itemView.findViewById(R.id.txt_noDO);

            txtTitleAsal  =  (TextView) itemView.findViewById(R.id.title_asal);
            fLayoutAsal  =  (FrameLayout) itemView.findViewById(R.id.fl_Asal);
            txtTitleTujuan  =  (TextView) itemView.findViewById(R.id.title_tujuan);
            fLayoutTujuan  =  (FrameLayout) itemView.findViewById(R.id.fl_Tujuan);

            txtAsal  =  (TextView) itemView.findViewById(R.id.txt_Asal);
            txtTujuan  =  (TextView) itemView.findViewById(R.id.txt_Tujuan);
            txtStatus  =  (TextView) itemView.findViewById(R.id.txt_StatusKendaraan);
            txtKoli =  (TextView) itemView.findViewById(R.id.txt_Koli);
            txtBerat =  (TextView) itemView.findViewById(R.id.txt_Berat);
            txtKuantitas =  (TextView) itemView.findViewById(R.id.txt_Kuantitas);
            txtTotalQrCode =  (TextView) itemView.findViewById(R.id.txt_TotalQrCode);

            btnScanDetailDo =  (Button) itemView.findViewById(R.id.btn_ScanDetailDo);
            layoutDetail =  (LinearLayout) itemView.findViewById(R.id.layout_Detail);


        }


    }
}