package com.example.lenovo.xsnsappv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.lenovo.xsnsappv2.IView.IMainActivity;
import com.example.lenovo.xsnsappv2.Presenter.ImplPresenter.MainActivityPresenterImpl;
import com.example.lenovo.xsnsappv2.activity.BaseActivity;
import com.example.lenovo.xsnsappv2.adapter.MainActivatyRvAdapter;
import com.example.lenovo.xsnsappv2.bean.ImageProgectListBean;
import com.example.lenovo.xsnsappv2.service.BaseService;
import com.example.lenovo.xsnsappv2.ui.ShowAllImageProjectRvItemRecoration;
import com.example.lenovo.xsnsappv2.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IMainActivity, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_show)
    RecyclerView mRvShow;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    /**
     * 获取图片的页数
     */
    private int page_num = 1;
    private int page_size = 8;
    private MainActivityPresenterImpl mMainActivityPresenter;

    private List<ImageProgectListBean.DataBean> datas = new ArrayList<>();
    private MainActivatyRvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startService(new Intent(mContext, BaseService.class));

        initData();
        initView();
        mMainActivityPresenter.getImageProjects(page_num + "", page_size + "");
    }

    private void initView() {
        setSupportActionBar(mToolbar);

        mAdapter = new MainActivatyRvAdapter(this, datas);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        mRvShow.setLayoutManager(gridLayoutManager);
        mRvShow.setHasFixedSize(true);
        mRvShow.addItemDecoration(new ShowAllImageProjectRvItemRecoration(mContext));
        mRvShow.setAdapter(mAdapter);

        mSwipeRefresh.setColorSchemeResources(R.color.mainRvItemBg2, R.color.colorPrimary);
        mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorWhite);
        mSwipeRefresh.setOnRefreshListener(this);

    }

    private void initData() {
        mMainActivityPresenter = new MainActivityPresenterImpl(this, this);
    }


    @Override
    public void saveDataAndShowImages(ImageProgectListBean imageProgectListBean) {
        datas.clear();
        datas.addAll(imageProgectListBean.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNetworkRequestErrorMessage(String message) {
        page_num--;
        mSwipeRefresh.setRefreshing(false);
        UIUtil.toastShort(this, message);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefresh.setRefreshing(refreshing);
    }

    @Override
    public void onRefresh() {
        mMainActivityPresenter.getImageProjects(++page_num + "", page_size + "");
    }

    @Override
    protected void noNetworkConnFail() {
        UIUtil.snackNewWorkErrorMessage(mRvShow, "当前网络不通");
    }

    @Override
    protected void noNetworkConnSuccess() {
        //当网络恢复时，暂时没有需要做的操作
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainActivityPresenter.unsubscribe();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.message_out_app_tip, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().finishApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
