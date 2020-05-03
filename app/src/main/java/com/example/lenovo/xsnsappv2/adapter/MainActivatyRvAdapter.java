package com.example.lenovo.xsnsappv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.lenovo.xsnsappv2.R;
import com.example.lenovo.xsnsappv2.activity.ImageProjectInfoActivity;
import com.example.lenovo.xsnsappv2.bean.ImageProgectListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 10:01
 */
public class MainActivatyRvAdapter extends RecyclerView.Adapter<MainActivatyRvAdapter.MainActivatyRvAdapterViewHolder> {

    private Context mContext;
    private List<ImageProgectListBean.DataBean> datas;

    public ArrayList<Drawable> placeholderDrawables = new ArrayList<>();

    public MainActivatyRvAdapter(Context context, List<ImageProgectListBean.DataBean> datas) {
        mContext = context;
        this.datas = datas;
        for (int i = 0; i < 8; i++) {
            //要么就设置个站位图尺寸不太大的
//            placeholderDrawables.add(mContext.getResources().getDrawable(R.drawable.show_image_default));
            //第一次不设置站位图
            placeholderDrawables.add(null);
        }
    }

    @NonNull
    @Override
    public MainActivatyRvAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_main_image_project_layout, parent, false);

        return new MainActivatyRvAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivatyRvAdapterViewHolder viewHolder, final int position) {
        viewHolder.tv_item_title.setText(datas.get(position).getTitle());
        GlideUrl glideUrl = new GlideUrl(datas.get(position).getCover_img_url(), new Headers() {
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
                        .error(R.drawable.show_image_default)
                        .placeholder(placeholderDrawables.get(position))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .centerCrop())
                //加载缩略图，缩略图先加载完就显示，否则不显示
                //交叉淡入
                .transition(new DrawableTransitionOptions().crossFade())
                //加载缩略图，缩略图先加载完就显示，否则不显示
                .thumbnail(0.2f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean
                            isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource
                            dataSource, boolean isFirstResource) {
                        placeholderDrawables.remove(position);
                        placeholderDrawables.add(position, resource);
                        return false;
                    }
                })
//                .thumbnail(Glide.with(mContext).load(R.drawable.coven))
                .into(viewHolder.iv_item_image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageProjectInfoActivity.class);
                intent.putStringArrayListExtra("image_urls", (ArrayList<String>) datas.get(position).getPic_urls());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MainActivatyRvAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_title;
        public ImageView iv_item_image;

        public MainActivatyRvAdapterViewHolder(View itemView) {
            super(itemView);
            tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
            iv_item_image = (ImageView) itemView.findViewById(R.id.iv_item_image);
        }
    }
}
