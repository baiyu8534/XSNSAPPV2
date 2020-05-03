package com.example.lenovo.xsnsappv2.Presenter;

/**
 * @author BaiYu
 * @description:
 * @date :2020/5/3 9:02
 */
public interface IMainActivityPresenter extends BasePresenter {
    /**
     * 获取图片
     *
     * @param count 数量
     * @param page  页
     */
    void getImageProjects(String page_num, String page_size);
}
