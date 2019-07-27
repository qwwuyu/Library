package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2018/4/19 17:39
 * Description .
 */
public abstract class BasePresenter<V extends BaseView,M extends BaseModel> {
    protected V view;
    protected M model;

    public BasePresenter(V view,M model) {
        this.view = view;
        this.model = model;
    }

    public void destroy() {
        model.destroy();
    }
}