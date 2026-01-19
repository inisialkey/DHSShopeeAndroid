package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.fungsitama.dhsshopee.model.ListDetailNomorDOModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListBarcodeLoadingAdapter extends RecyclerView.Adapter<ListBarcodeLoadingAdapter.ViewHolder> {

    private List<ListBarcodeLoadingModel> listDetailNomorDOModels;
    private Context context;
    Onclick onclick,ondelete;

    public interface Onclick {
        void onEvent(ListBarcodeLoadingModel modelItem, int pos);
    }

    public ListBarcodeLoadingAdapter(List<ListBarcodeLoadingModel> listDetailNomorDOModels, Context context, Onclick onclick, Onclick ondelete) {
        this.listDetailNomorDOModels = listDetailNomorDOModels;
        this.context = context;
        this.onclick = onclick;
        this.ondelete = ondelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_barcode_loading, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListBarcodeLoadingModel listdata = listDetailNomorDOModels.get(position);


        holder.txtQrCode.setText(listdata.getQrcode());
        holder.txtRowNumber.setText(listdata.getRowNumber());

        Log.d("TAG", "Scan true: " + listdata.getScan());



        if (listdata.getStatus().equals("Loading")){
            if (listdata.getScan().equals("true")){
                holder.ly_status.setBackgroundResource(R.drawable.button_background_dsbl);

            }else if(listdata.getManual().equals("true")) {
                holder.ly_status.setBackgroundResource(R.drawable.blue_button_background);
            }else{
                holder.ly_status.setBackgroundResource(R.drawable.button_background);
            }
        }else{
            holder.ly_status.setBackgroundResource(R.drawable.button_background_greend);
            holder.delete.setVisibility(View.GONE);
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ondelete.onEvent(listdata,position);
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
        return listDetailNomorDOModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtQrCode;
        public TextView txtRowNumber;
        public TextView delete;

        public LinearLayout layoutDetail;
        public LinearLayout ly_status;

        public ImageView img_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQrCode  =  (TextView) itemView.findViewById(R.id.txt_QrCode);
            txtRowNumber  =  (TextView) itemView.findViewById(R.id.txt_rowNumber);
            delete  =  (TextView) itemView.findViewById(R.id.delete);

            layoutDetail  =  (LinearLayout) itemView.findViewById(R.id.layout_Detail);
            ly_status  =  (LinearLayout) itemView.findViewById(R.id.ly_status);

            img_status = (ImageView) itemView.findViewById(R.id.imgView_status);

        }


    }
}