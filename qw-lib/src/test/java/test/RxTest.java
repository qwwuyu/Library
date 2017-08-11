package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxTest {
    /** 模拟Android主线程Scheduler */
    private Scheduler main;
    /** 模拟ioScheduler */
    private Scheduler io;
    /** 模拟dbScheduler */
    private Scheduler db;
    private long wait = 1000;

    @Before
    public void setup() {
        Thread.currentThread().setName("java-main");
        main = Schedulers.from(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NameThreadFactory("main"), new ThreadPoolExecutor.AbortPolicy()));
        io = Schedulers.from(new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new NameThreadFactory("  io"), new ThreadPoolExecutor.AbortPolicy()));
        db = Schedulers.from(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NameThreadFactory("  db"), new ThreadPoolExecutor.AbortPolicy()));
    }

    @After
    public void end() {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException ignored) {
        }
    }

    @Test
    public void api() throws Exception {
        Observable.zip(null, null, null)
                .subscribeOn(null)
                .observeOn(null)

                .doOnSubscribe(null)
                .doOnNext(null)
                .doOnError(null)
                .doOnComplete(null)

                .compose(null)
                .map(null)
                .flatMap(null)
                .concatMap(null)

                .filter(null)
                .distinct()
                .take(0)

                .sample(null)

        ;
    }

    @Test
    public void observable() throws Exception {
        Observable.empty().subscribe(new SObserver<>("empty"));
        Observable.just(1, 2, 3).subscribe(new SObserver<>("just"));
        Observable.fromArray(new Integer[]{1, 2, 3}).subscribe(new SObserver<>("fromArray"));
        Observable.create(new SObservableOnSubscribe()).subscribe(new SObserver<>("create"));
        Observable.create(new SObservableOnSubscribe(true)).subscribe(new SObserver<>("createError"));
        Observable.interval(400, TimeUnit.MILLISECONDS).subscribe(new SObserver<>("interval"));
    }

    @Test
    public void observer() throws Exception {
        Observable.just(1, 2, 3).subscribe(new SObserver<>("SObserver"));
        Observable.just(1, 2, 3).subscribe(data -> tag("订阅1个参数", "onNext:" + data));
        Observable.just(1, 2, 3).subscribe(data -> tag("订阅2个参数", "onNext:" + data), error -> tag("订阅2个参数", "onError:" + error));
        Observable.just(1, 2, 3).subscribe(data -> tag("订阅3个参数", "onNext:" + data), error -> tag("订阅3个参数", "onError:" + error),
                () -> tag("订阅3个参数", "onComplete"));
        Observable.just(1, 2, 3).subscribe(data -> tag("订阅4个参数", "onNext:" + data), error -> tag("订阅4个参数", "onError:" + error),
                () -> tag("订阅4个参数", "onComplete"), disposable -> tag("订阅4个参数", "onSubscribe"));
    }

    @Test
    public void disposable() throws Exception {
        final Disposable[] mDisposable = new Disposable[1];
        Observable.just(1, 2, 3).subscribe(data -> {
                    s("onNext:" + data);
                    if (data == 2) {
                        s("取消观察");
                        mDisposable[0].dispose();
                    }
                }, error -> s("onError:" + error), () -> s("onComplete"),
                disposable -> {
                    s("存储Disposable");
                    mDisposable[0] = disposable;
                });
    }

    @Test
    public void scheduler() throws Exception {
        Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    s("subscribe");
                    e.onNext(1);
                    e.onComplete();
                })
                .subscribeOn(io)
                .observeOn(main)
                .subscribe(data -> s("onNext:" + data), error -> s("onError"), () -> s("onComplete"), disposable -> s("onSubscribe"));
    }

    @Test
    public void map() throws Exception {
        Observable.just(1, 2, 3)
                .map((obj) -> String.valueOf(obj + 1))
                .subscribe(new SObserver<>("map"));
    }

    @Test
    public void flatMap() throws Exception {
        Observable.fromArray(new Integer[][]{{1, 2}, {3, 4}, {5, 6}})
                .flatMap(new Function<Integer[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer[] is) throws Exception {
                        return Observable.fromArray(is).map(String::valueOf);
                    }
                }).subscribe(new SObserver<>("flatMap"));
    }

    @Test
    public void concatMap() throws Exception {
        Observable.fromArray(new Integer[][]{{1, 2}, {3, 4}, {5, 6}})
                .concatMap(new Function<Integer[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer[] is) throws Exception {
                        return Observable.fromArray(is).map(String::valueOf);
                    }
                }).subscribe(new SObserver<>("concatMap"));
    }

    @Test
    public void zip() throws Exception {
        Observable.zip(Observable.just(1, 2), Observable.just(4, 5, 6), (i1, i2) -> i1 * 10 + i2).subscribe(new SObserver<>("zip"));
    }

    @Test
    public void filter() throws Exception {
        Observable.just(1, 2, 3, 4, 5)
                .filter((obj) -> obj % 2 != 0)
                .subscribe(new SObserver<>("filter"));
    }

    @Test
    public void sample() throws Exception {
        Observable
                .create((ObservableOnSubscribe<Integer>) e -> {
                    for (int i = 0; i < 100; i++) {
                        e.onNext(i);
                        sleep(100);
                    }
                    e.onComplete();
                })
                .sample(1000, TimeUnit.MILLISECONDS)
                .observeOn(io)
                .subscribe(new SObserver<>("sample"));
    }

    @Test
    public void flowable() throws Exception {
        Flowable
                .create((FlowableOnSubscribe<Integer>) e -> {
                    s("onNext1:" + e.requested());
                    e.onNext(1);
                    s("onNext2:" + e.requested());
                    e.onNext(2);
                    s("onNext3:" + e.requested());
                    e.onNext(3);
                    e.onComplete();
                }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(2);
                    }

                    @Override
                    public void onNext(@NonNull Integer t) {
                        s("onNext:" + t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        s("onError:" + e.getClass());
                    }

                    @Override
                    public void onComplete() {
                        s("onComplete");
                    }
                });
    }

    @Test
    public void flowable2() throws Exception {
        Subscription[] sb = new Subscription[1];
        Flowable
                .interval(1, TimeUnit.MILLISECONDS)
                .subscribeOn(io)
                .onBackpressureDrop()
                .observeOn(io)
                .subscribe(aLong -> {
                            s("onNext:" + aLong);
                            sleep(100);
                            sb[0].request(1);
                        }, throwable -> s("onError:" + throwable.getClass())
                        , () -> s("onComplete"), subscription -> {
                            sb[0] = subscription;
                            subscription.request(1);
                        });
    }

    @Test
    public void test() throws Exception {

    }

    private static void s(String s) {
        System.out.println("thread=" + Thread.currentThread().getName() + " : " + s);
    }

    private static void tag(String tag, String s) {
        System.out.println("tag=" + tag + " thread=" + Thread.currentThread().getName() + " : " + s);
    }

    private static class SObserver<T> implements Observer<T> {
        private String tag;

        public SObserver(String tag) {
            this.tag = tag;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            tag(tag, "onSubscribe");
        }

        @Override
        public void onNext(@NonNull T t) {
            tag(tag, "onNext:" + t);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            tag(tag, "onError:" + e.getMessage());
        }

        @Override
        public void onComplete() {
            tag(tag, "onComplete");
        }
    }

    private static class SObservableOnSubscribe implements ObservableOnSubscribe<Integer> {
        private boolean error;
        private long sleep;

        public SObservableOnSubscribe() {
        }

        public SObservableOnSubscribe(boolean error) {
            this.error = error;
        }

        public SObservableOnSubscribe(boolean error, long sleep) {
            this.error = error;
            this.sleep = sleep;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
            if (sleep != 0) sleep(sleep);
            e.onNext(1);
            if (sleep != 0) sleep(sleep);
            e.onNext(2);
            if (error) e.onError(new RuntimeException("customize error"));
            else e.onComplete();
            if (sleep != 0) sleep(sleep);
            e.onNext(3);
        }
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

    /** j8特性 */
    public void java8() throws Exception {
        Observable.empty().subscribe(System.out::println);
        Observable.empty().subscribe(i -> System.out.println(String.valueOf(i)));
    }

    /** 提供多种Scheduler */
    private void schedulers() throws Exception {
        /** Android主线程 */
        Scheduler mainScheduler = AndroidSchedulers.mainThread();
        /** 多线程,无上线,可复用 */
        Scheduler ioScheduler = Schedulers.io();
        /** 新的线程,不建议使用 */
        Scheduler newScheduler = Schedulers.newThread();
        /** 根据CPU的核数量Scheduler(密集型操作,不要用等待操作) */
        Scheduler computationScheduler = Schedulers.computation();
        /** 单线程 */
        Scheduler singleScheduler = Schedulers.single();
        /** 根据线程池创建Scheduler */
        Scheduler customize = Schedulers.from(Executors.newSingleThreadExecutor());
    }

    /** 更改线程名字 */
    private static class NameThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NameThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix + "-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}