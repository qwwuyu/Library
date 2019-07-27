package com.qwwuyu.lib.mvp;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;

/**
 * Created by qiwei on 2019/6/26.
 */
public class RxBaseModel implements BaseModel{
     private CompositeDisposable compositeDisposable;

     public RxBaseModel() {
          compositeDisposable= new CompositeDisposable();
     }

     public <V extends Void> void subscribe(Flowable<V> flowable, final Callback<V> callback) {
          if (flowable == null || callback == null) {
               return;
          }
          compositeDisposable.add(flowable
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(t -> {
                       t.notify();
                       t.notify();
                  }, throwable -> {
                       throwable.notify();
                       throwable.notify();
                  }));
     }

     @Override
     public void destroy() {
          compositeDisposable.clear();
     }
}
