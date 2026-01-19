package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListDetailPengeluaranModel;

import java.util.List;

public class ListDetailNoReferensiPengeluaranAdapter extends RecyclerView.Adapter<ListDetailNoReferensiPengeluaranAdapter.ViewHolder> {

    private List<ListDetailPengeluaranModel> listNomorModels;
    private Context context;


    public ListDetailNoReferensiPengeluaranAdapter(List<ListDetailPengeluaranModel> listNomorModels, Context context) {
        this.listNomorModels = listNomorModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_detail_nomor_pengeluaran, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListDetailPengeluaranModel listdata = listNomorModels.get(position);

       // String tanggalDibuatDO = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getTransactionDate());

        holder.txtQrCode.setText(listdata.getQrcode());
        holder.txtStatus.setText(listdata.getStatus());

    }


    @Override
    public int getItemCount() {
        return listNomorModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtQrCode;
        public TextView txtStatus;

        public Button btnUpdateStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQrCode  =  (TextView) itemView.findViewById(R.id.txt_QrCode);
            txtStatus  =  (TextView) itemView.findViewById(R.id.txt_Status);

        }


    }
}