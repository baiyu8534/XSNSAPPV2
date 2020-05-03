package com.example.lenovo.xsnsappv2.IView;

import com.example.lenovo.xsnsappv2.bean.ImageProgectListBean;

import java.util.ArrayList;

/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 8:58
 */
public interface IMainActivity extends IBaseView{

    /**
     * 更新图片显示
     *
     * @param image_urls
     * @param referer
     */
    void saveDataAndShowImages(ImageProgectListBean imageProgectListBean);

    /**
     * 显示错误信息
     *
     * @param message
     */
    void showNetworkRequestErrorMessage(String message);

    /**
     * 控制上拉加载的显示
     *
     * @param refreshing
     */
    void setRefreshing(boolean refreshing);

}
