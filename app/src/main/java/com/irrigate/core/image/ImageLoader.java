package com.irrigate.core.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.lang.ref.WeakReference;

/**
 * Created by xinyuanzhong on 2017/7/13.
 */

public final class ImageLoader {
    private ImageLoader() {
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }
        WeakReference<ImageView> viewWeakReference = new WeakReference<>(imageView);
        Glide.with(context.getApplicationContext())
                .load(url)
                .into(viewWeakReference.get());
    }

    public static void loadImage(Context context, String url,
                                 ImageView imageView, int defaultImage) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .dontAnimate()
                .placeholder(defaultImage)
                .into(imageView);
    }

    public static void loadImage(Context context, int res,
                                 ImageView imageView, int defaultImage) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(res)
                .centerCrop()
                .dontAnimate()
                .placeholder(defaultImage)
                .into(imageView);
    }

    public static void loadImageWithTargetListener(Context context, String url,
                                                   GlideDrawableImageViewTarget target) {
        if (context == null) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .into(target);
    }
}
