package com.fungsitama.dhsshopee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.model.ListBarcodeLoadingModel;
import com.fungsitama.dhsshopee.model.ListGambarModel;

import java.util.List;

public class ListGambarAdapter extends RecyclerView.Adapter<ListGambarAdapter.ViewHolder> {

    private List<ListGambarModel> images;
    private Context mContext;

    public interface ClickListener {
        void onClick(View view, int i);

        void onLongClick(View view, int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);

        }


    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clickListener;
        private GestureDetector gestureDetector;

        public void onRequestDisallowInterceptTouchEvent(boolean z) {
        }

        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        }

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener2) {
            this.clickListener = clickListener2;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

                public void onLongPress(MotionEvent motionEvent) {
                    View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (findChildViewUnder != null && clickListener2 != null) {
                        clickListener2.onLongClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
                    }
                }
            });
        }

        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (!(findChildViewUnder == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(motionEvent))) {
                this.clickListener.onClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
            }
            return false;
        }
    }

    public ListGambarAdapter(Context mContext,List<ListGambarModel> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_thumbnail, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {

        ListGambarModel ft = (ListGambarModel) this.images.get(i);
        Glide.with(this.mContext)
                .load(ft.getUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(0.1f)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder((int) R.drawable.ic_gallery)
                        .error((int) R.drawable.transparant)
                        .centerCrop()).into(holder.thumbnail);
        // myViewHolder.title.setText(ft.getName());

    }


    public int getItemCount() {
        return this.images.size();
    }


}