package com.qwwuyu.example.mvp;

import com.qwwuyu.lib.mvp.BaseModel;
import com.qwwuyu.lib.mvp.BasePresenter;
import com.qwwuyu.lib.mvp.BaseView;

/**
 * Created by qiwei on 2018/6/14 14:02
 * Description .
 */
public interface MvpContract {
    interface View extends BaseView {
        void test();
    }

    interface IPresenter {
        void test();
    }

    abstract class Presenter<M extends BaseModel> extends BasePresenter<View,M> implements IPresenter {
        public Presenter(View view,M model) {
            super(view,model);
        }
    }
}
