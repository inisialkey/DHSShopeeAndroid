package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fungsitama.dhsshopee.model.ListKendaraanModel;
import com.fungsitama.dhsshopee.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListTambahKendaraanAdapter extends RecyclerView.Adapter<ListTambahKendaraanAdapter.ViewHolder> {

    private List<ListKendaraanModel> listPengirimanModels;
    private Context context;
    Onclick onclick;

    public ListTambahKendaraanAdapter(List<ListKendaraanModel> listPengirimanModels, Context context, Onclick onclick) {
        this.listPengirimanModels = listPengirimanModels;
        this.context = context;
        this.onclick = onclick;
    }

    public interface Onclick {
        void onEvent(ListKendaraanModel modelItem, int pos);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_kendaraan, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        final ListKendaraanModel listdata = listPengirimanModels.get(position);

        String stnkExpired = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getStnkExpired());
        String kirExpired = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(listdata.getKirExpired());

        holder.txtNoKendaraan.setText(listdata.getCode());

        if (listdata.getStatusUsage().equals("false")){
            holder.txtStatus.setText("Tidak Digunakan");
        }else{
            holder.txtStatus.setText("Sedang Digunakan");
        }

        holder.txtStnkExpired.setText(stnkExpired);
        holder.txtKirExpired.setText(kirExpired);


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
        public TextView txtStatus;
        public TextView txtStnkExpired;
        public TextView txtKirExpired;
        public LinearLayout layoutDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNoKendaraan  =  (TextView) itemView.findViewById(R.id.txt_NoKendaraan);
            txtStatus =  (TextView) itemView.findViewById(R.id.txt_Status);
            txtStnkExpired =  (TextView) itemView.findViewById(R.id.txt_StnkExpired);
            txtKirExpired =  (TextView) itemView.findViewById(R.id.txt_KirExpired);
            layoutDetail = (LinearLayout) itemView.findViewById(R.id.layout_Detail);

        }
    }
}