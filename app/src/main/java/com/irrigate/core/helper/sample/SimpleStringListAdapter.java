package com.irrigate.core.helper.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irrigate.core.util.UIUtils;

import java.util.List;

/**
 * Created by Zhengyu.Xiong on 16/9/19.
 */
public class SimpleStringListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SIZE_16 = 16;
    public static final int DPI_12 = 12;
    private int padding;

    private Context context;
    private List<String> data;
    private OnItemClickListener listener;

    public SimpleStringListAdapter(Context context, List<String> data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.padding = UIUtils.dp2px(context, DPI_12);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextSize(SIZE_16);
        return new ContentViewHolder(textView);
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView statusView;

        public ContentViewHolder(View view) {
            super(view);
            statusView = (TextView) view;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (data.isEmpty()) return;
        loadContentView((ContentViewHolder) holder, position);
    }

    private void loadContentView(ContentViewHolder holder, final int position) {
        initBaseInfoView(holder, data.get(position), position);
    }

    private void initBaseInfoView(ContentViewHolder holder, String s, final int position) {
        holder.statusView.setText(s);
        holder.statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /*--------------------------------------------------------------------------------------------*/

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}