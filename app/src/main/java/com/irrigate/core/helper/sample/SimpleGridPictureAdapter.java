package com.irrigate.core.helper.sample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.irrigate.core.image.ImageLoader;
import com.irrigate.core.util.UIUtils;
import com.irrigate.core.widget.ViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-04-06.
 */

public class SimpleGridPictureAdapter extends BaseAdapter {

    private List<String> pictureUrls = new ArrayList<>();
    private PictureClickListener pictureClickListener;
    private Context context;
    private int imageHeight;

    public SimpleGridPictureAdapter(Context context, List<String> pictureUrls, int imageHeight,
                                    @Nullable PictureClickListener pictureClickListener) {
        this.context = context;
        this.pictureUrls = pictureUrls;
        this.pictureClickListener = pictureClickListener;
        this.imageHeight = imageHeight;
    }

    @Override
    public int getCount() {
        return pictureUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return pictureUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dp2px(context, imageHeight)));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.loadImage(context, pictureUrls.get(position), imageView);
        if (pictureClickListener != null) {
            imageView.setOnClickListener(new ViewClickListener() {
                @Override
                protected void onViewClick(View v) {
                    pictureClickListener.onClick(position);
                }
            });
        }

        return imageView;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    /*--------------------------------------------------------------------------------------------*/

    public interface PictureClickListener {
        void onClick(int position);
    }

}