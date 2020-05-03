package com.example.lenovo.xsnsappv2.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.xsnsappv2.R;
import com.example.lenovo.xsnsappv2.adapter.ImageProjectInfoRvAdapter;
import com.example.lenovo.xsnsappv2.utils.ScreenUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 10:35
 */

public class ImageProjectInfoActivity extends BaseActivity {
    @BindView(R.id.rv_show_image)
    RecyclerView mRvShowImage;
//    @BindView(R.id.iv_network_error)
//    ImageView mIvNetworkError;
//    @BindView(R.id.tv_network_error)
//    TextView mTvNetworkError;
//    @BindView(R.id.tv_network_error_button)
//    TextView mTvNetworkErrorButton;
//    @BindView(R.id.rl_network_error)
//    RelativeLayout mRlNetworkError;
//    @BindView(R.id.wv_dialog)
//    WhorlView mWvDialog;
    private ArrayList<String> mImage_urls;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageProjectInfoRvAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_project_info);
        ButterKnife.bind(this);

        mImage_urls = getIntent().getStringArrayListExtra("image_urls");
        initView();
        initViewListener();
    }

    private void initView() {
        mAdapter = new ImageProjectInfoRvAdapter(mImage_urls, this);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRvShowImage.setLayoutManager(mLinearLayoutManager);
        mRvShowImage.setHasFixedSize(true);
        //刚开始隐藏，只要加载成功一次就显示，要不然第一次没成功就会显示没图的Rv
        mRvShowImage.setAdapter(mAdapter);
    }

    private void initViewListener() {
        mRvShowImage.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // TODO: 2017/9/18 0018 因为加了下拉自动加载，所以不能把背景滚动的操作放到自定义wview中，否则会被覆盖，要考虑一些怎么整合下。。
                if (dy > 0) {
                    //向下滚动
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int unvisibleItemCount = mLinearLayoutManager.findFirstVisibleItemPosition();
                }

                ImageProjectInfoRvAdapter adapter = (ImageProjectInfoRvAdapter) mRvShowImage.getAdapter();
                if (dy > 0) {
                    //手指向上滑
                    for (int i = 0; i < mRvShowImage.getChildCount(); i++) {
                        View child = mRvShowImage.getChildAt(i);
                        ImageView imageView = (ImageView) child.findViewById(R.id.iv);
                        //ScreenUtil.getScreenHeight(mContext) / 5 是滚动的最大距离
                        if (imageView.getScrollY() + (dy) / 3 < ScreenUtil.getScreenHeight(mContext) / 5) {
                            //(scrollY - proY) / 2使滑动的速度不会太快。。
                            imageView.scrollBy(0, (int) ((dy) / 2.5));
                        } else {
                        }
                        adapter.isUp = true;
                    }
                } else {
                    for (int i = 0; i < mRvShowImage.getChildCount(); i++) {
                        View child = mRvShowImage.getChildAt(i);
                        ImageView imageView = (ImageView) child.findViewById(R.id.iv);
                        if (imageView.getScrollY() + (dy) / 3 > -ScreenUtil.getScreenHeight(mContext) / 5) {
                            imageView.scrollBy(0, (int) ((dy) / 2.5));
                        } else {
//                        System.out.println("imageView.getScrollY()+dy::::"+imageView.getScrollY()+dy);
                        }
                        adapter.isUp = false;
                    }
                }
            }
        });
//        mTvNetworkErrorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mRlNetworkError.setVisibility(View.GONE);
////                mPresenter.getImages(imageUrls.size() + "", page++ + "");
//            }
//        });
    }
}
