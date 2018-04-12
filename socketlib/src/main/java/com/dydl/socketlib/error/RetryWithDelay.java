package com.dydl.socketlib.error;

import com.dydl.socketlib.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by lwx on 2018/4/10.
 */

public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    /* retry次数*/

    private int count = 1;

    /*延迟*/

    private long delay = 100;

    /*叠加延迟*/

    private long increaseDelay = 100;


    public RetryWithDelay() {


    }


    public RetryWithDelay(int count, long delay) {

        this.count = count;

        this.delay = delay;

    }


    public RetryWithDelay(int count, long delay, long increaseDelay) {

        this.count = count;

        this.delay = delay;

        this.increaseDelay = increaseDelay;

    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable

                .zipWith(Observable.range(1, count + 1),
                        (BiFunction<Throwable, Integer, Wrapper>) (throwable, integer) ->
                                new Wrapper(throwable, integer))
                .flatMap((Function<Wrapper, Observable<?>>) wrapper -> {
                    if ((wrapper.throwable instanceof ConnectException

                            || wrapper.throwable instanceof SocketTimeoutException

                            || wrapper.throwable instanceof TimeoutException)

                            && wrapper.index < count + 1) { //如果超出重试次数也抛出错误，否则默认是会进入onCompleted

                        return Observable.timer(delay + (wrapper.index - 1) * increaseDelay, TimeUnit.MILLISECONDS);
                    }

                    return Observable.error(wrapper.throwable);

                });
    }


    private class Wrapper {

        private int index;

        private Throwable throwable;


        public Wrapper(Throwable throwable, int index) {

            this.index = index;

            this.throwable = throwable;

        }

    }
}
