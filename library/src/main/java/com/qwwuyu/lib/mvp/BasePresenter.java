package com.qwwuyu.lib.mvp;

/**
 *
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {
    protected V view;
    protected M model;

    public BasePresenter(V view, M model) {
        this.view = view;
        this.model = model;
    }

    public void destroy() {
        model.destroy();
    }
}