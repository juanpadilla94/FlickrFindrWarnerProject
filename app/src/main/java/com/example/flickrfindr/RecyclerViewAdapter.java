package com.example.flickrfindr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ResultsActivity.Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private String type;
    private int page;
    private String query;
    private HashMap<Integer, Bitmap> bitMap;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<ResultsActivity.Item> data, String type, int page, String query) {
        this.context = context;
        this.type = type;
        this.page = page;
        this.query = query;
        this.mInflater = LayoutInflater.from(context);
        bitMap = new HashMap<>();
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = mData.get(position).title;
        Bitmap photoBitMap = null;
        if(bitMap.containsKey(position)) {
            photoBitMap = bitMap.get(position);
        }
        else {
            try {
                photoBitMap = new BitMapFactory().execute(mData.get(position).url).get();
                bitMap.put(position, photoBitMap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        holder.rowTextView.setText(title);
        holder.rowImageView.setImageBitmap(photoBitMap);
        holder.rowImageView.setMinimumHeight(200);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rowTextView;
        ImageView rowImageView;

        ViewHolder(View itemView) {
            super(itemView);
            rowTextView = itemView.findViewById(R.id.recyclerViewTextView);
            rowImageView = itemView.findViewById(R.id.recyclerViewImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("mClickListener: " + mClickListener);
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                System.out.println("on click s");
                final Intent photoIntent =
                        new Intent(context, PhotoActivity.class);
                ResultsActivity.Item item = mData.get(getAdapterPosition());
                photoIntent.putExtra("type", type);
                photoIntent.putExtra("page", page);
                photoIntent.putExtra("url",item.url);
                photoIntent.putExtra("title", item.title);
                photoIntent.putExtra("query", query);
                Bitmap photoBitMap = null;
                try {
                    photoBitMap = new BitMapFactory().execute(item.url).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                photoIntent.putExtra("photo", photoBitMap);
                context.startActivity(photoIntent);
            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).title;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}