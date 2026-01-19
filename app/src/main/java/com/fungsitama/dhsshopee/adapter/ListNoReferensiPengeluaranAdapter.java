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
import com.fungsitama.dhsshopee.model.ListNomorReferensiPengeluaranModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListNoReferensiPengeluaranAdapter extends RecyclerView.Adapter<ListNoReferensiPengeluaranAdapter.ViewHolder> {

    private List<ListNomorReferensiPengeluaranModel> listNomorModels;
    private Context context;
    Onclick scanListDetail,detail;


    public interface Onclick {
        void onEvent(ListNomorReferensiPengeluaranModel modelItem, int pos);
    }

    public ListNoReferensiPengeluaranAdapter(List<ListNomorReferensiPengeluaranModel> listNomorModels, Context context, Onclick scanListDetail, Onclick detail) {
        this.listNomorModels = listNomorModels;
        this.context = context;
        this.scanListDetail = scanListDetail;
        this.detail = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_no_referensi_pengeluaran, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListNomorReferensiPengeluaranModel listdata = listNomorModels.get(position);

        String tanggalDibuatDO = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getTransactionDate());

        holder.txtNoReferensi.setText(listdata.getReferenceNumber());
        holder.txtAgen.setText(listdata.getNamaAgen());
        holder.txtKoli.setText(listdata.getKoli() + " Koli");
        holder.txtBerat.setText(listdata.getKg() + " Kg");
        holder.txtKuantitas.setText(listdata.getQty());
        holder.txtTotalQrCode.setText(listdata.getTotalQrCode());

        holder.btnScanDetailDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanListDetail.onEvent(listdata,position);
            }
        });

        holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail.onEvent(listdata,position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listNomorModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNoReferensi;
        public TextView txtAgen;
        public TextView txtKoli;
        public TextView txtBerat;
        public TextView txtKuantitas;
        public TextView txtTotalQrCode;

        public Button btnScanDetailDo;
        public LinearLayout layoutDetail;

        public Button btnUpdateStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNoReferensi  =  (TextView) itemView.findViewById(R.id.txt_NoReferensi);
            txtAgen  =  (TextView) itemView.findViewById(R.id.txt_Agen);
            txtKoli =  (TextView) itemView.findViewById(R.id.txt_Koli);
            txtBerat =  (TextView) itemView.findViewById(R.id.txt_Berat);
            txtKuantitas =  (TextView) itemView.findViewById(R.id.txt_Kuantitas);
            txtTotalQrCode =  (TextView) itemView.findViewById(R.id.txt_TotalQrCode);

            btnScanDetailDo =  (Button) itemView.findViewById(R.id.btn_ScanDetailDo);
            layoutDetail =  (LinearLayout) itemView.findViewById(R.id.layout_Detail);

        }


    }
}