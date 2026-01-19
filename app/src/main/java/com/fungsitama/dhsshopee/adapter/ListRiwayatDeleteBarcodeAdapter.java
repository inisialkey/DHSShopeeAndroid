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
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
import com.fungsitama.dhsshopee.model.ListDeleteBarcodeLoadingModel;

import java.util.List;

public class ListRiwayatDeleteBarcodeAdapter extends RecyclerView.Adapter<ListRiwayatDeleteBarcodeAdapter.ViewHolder> {

    private List<ListDeleteBarcodeLoadingModel> listDetailNomorDOModels;
    private Context context;
    Onclick onclick;

    public interface Onclick {
        void onEvent(ListDeleteBarcodeLoadingModel modelItem, int pos);
    }

    public ListRiwayatDeleteBarcodeAdapter(List<ListDeleteBarcodeLoadingModel> listDetailNomorDOModels, Context context, Onclick onclick) {
        this.listDetailNomorDOModels = listDetailNomorDOModels;
        this.context = context;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_riwayat_delete_barcode, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListDeleteBarcodeLoadingModel listdata = listDetailNomorDOModels.get(position);


        holder.txtQrCode.setText(listdata.getQrcode());
        holder.txtRowNumber.setText(listdata.getRowNumber());


        /*holder.layoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onEvent(listdata,position);
            }
        });*/


        

    }


    @Override
    public int getItemCount() {
        return listDetailNomorDOModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtQrCode;
        public TextView txtRowNumber;

        public LinearLayout layoutDetail;

        public ImageView img_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQrCode  =  (TextView) itemView.findViewById(R.id.txt_QrCode);
            txtRowNumber  =  (TextView) itemView.findViewById(R.id.txt_rowNumber);

            layoutDetail  =  (LinearLayout) itemView.findViewById(R.id.layout_Detail);

            img_status = (ImageView) itemView.findViewById(R.id.imgView_status);

        }


    }
}