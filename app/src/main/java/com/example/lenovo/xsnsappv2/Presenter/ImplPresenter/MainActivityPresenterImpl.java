package com.example.lenovo.xsnsappv2.Presenter.ImplPresenter;

import android.content.Context;

import com.example.lenovo.xsnsappv2.IView.IMainActivity;
import com.example.lenovo.xsnsappv2.Presenter.IMainActivityPresenter;
import com.example.lenovo.xsnsappv2.api.ApiManager;
import com.example.lenovo.xsnsappv2.bean.ImageProgectListBean;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 9:03
 */
public class MainActivityPresenterImpl extends BasePresenterImpl implements IMainActivityPresenter {

    private Context mContext;
    private IMainActivity mMainActivity;

    public MainActivityPresenterImpl(Context context, IMainActivity mainActivity) {
        mContext = context;
        mMainActivity = mainActivity;
    }

    @Override
    public void getImageProjects(String page_num, String page_size) {
        mMainActivity.setRefreshing(true);
        Subscription s = ApiManager.getInstance().getXsnsApiService().getImageProjectList(page_num, page_size)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ImageProgectListBean, ImageProgectListBean>() {
                    @Override
                    public ImageProgectListBean call(ImageProgectListBean imageProgectListBean) {
                        return imageProgectListBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageProgectListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainActivity.setRefreshing(false);
                        informShowErrorMessage(e, mMainActivity);
                    }

                    @Override
                    public void onNext(ImageProgectListBean imageProgectListBean) {
                        mMainActivity.setRefreshing(false);
                        if (null != imageProgectListBean) {
                            mMainActivity.saveDataAndShowImages(imageProgectListBean);
                        }
                    }
                });
        addSubscription(s);
    }
}
