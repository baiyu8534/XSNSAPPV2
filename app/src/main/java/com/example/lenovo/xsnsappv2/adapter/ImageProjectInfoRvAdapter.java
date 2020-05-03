package com.example.lenovo.xsnsappv2.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.xsnsappv2.R;
import com.example.lenovo.xsnsappv2.activity.ImageProjectInfoActivity;
import com.example.lenovo.xsnsappv2.activity.ShowImageActivity;
import com.example.lenovo.xsnsappv2.ui.GlidePlaceholderDrawable;
import com.example.lenovo.xsnsappv2.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baiyu on 2017/4/24.
 */
public class ImageProjectInfoRvAdapter extends RecyclerView.Adapter<ImageProjectInfoRvAdapter.ViewHolder> {


    public boolean isUp = true;
    private ArrayList<String> images;

    private Context mContext;

    public ImageProjectInfoRvAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bg_roll_rv_layout, parent, false);

        //设置itemVIew高度
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.height = layoutParams.height/2;
//        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GlideUrl glideUrl = new GlideUrl(images.get(position), new Headers() {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                //不一定都要添加，具体看原站的请求信息
                header.put("Referer", "https://www.xsnvshen.com/album");
                return header;
            }
        });
        Glide.with(mContext).load(glideUrl)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(new GlidePlaceholderDrawable(mContext.getResources(),R.drawable.show_image_default_1))
                        //重新设置图片的宽高
                        .override(ScreenUtil.getScreenWidth(mContext), ScreenUtil.getScreenHeight(mContext))
                        .centerCrop()
                )
                .transition(new DrawableTransitionOptions().crossFade())
                // TODO: 2017/9/21 0021 加载缩略图，缩略图先加载完就显示，否则不显示 没卵用图片填不满，很小。。可以研究想
//                .thumbnail(0.5f)
                // 设置gif为loading时的图片但是会缩放，不好看
//                .thumbnail(Glide.with(mContext).load(R.drawable.loading_1))
                //这个加载不出gif
//                .thumbnail(Glide.with(mContext).load(new GlidePlaceholderDrawable(mContext.getResources(),R.drawable.coven)))
                .into(holder.iv_item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowImageActivity.class);
                intent.putExtra("imageUrl", images.get(position));
                List<Pair<View, String>> pairs = new ArrayList<>();
                pairs.add(Pair.create((View) holder.iv_item, "iv_item"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((ImageProjectInfoActivity) mContext, pairs.toArray
                            (new Pair[]{}))
                            .toBundle();
                    mContext.startActivity(intent, bundle);
                } else {
                    mContext.startActivity(intent);
                }
            }
        });

        // 判断是什么方向划出图片  提前滑动预定地点
        //ScreenUtil.getScreenHeight(mContext) / 5 是滚动的最大距离
        // FIXME: 2017/9/19 坑爹的，开头3张图没移动，有的时候移动了，有的时候没移动，一定是加载和移动的顺序问题
        if (isUp)
            holder.iv_item.scrollTo(0, (-1 * ScreenUtil.getScreenHeight(mContext) / 5));
        else
            holder.iv_item.scrollTo(0, ScreenUtil.getScreenHeight(mContext) / 5);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_item;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_item = (ImageView) itemView.findViewById(R.id.iv);
        }

    }
}
