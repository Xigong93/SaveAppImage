package com.pokercc.saveappimage;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

public class JavaMethodTest {
    static class People {
        final String name;

        People(String name) {
            this.name = name;
        }
    }

    void a(People people) {
        final WeakReference peopleRef = new WeakReference<>(people);
        Completable.complete()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        TimeUnit.SECONDS.sleep(1);
                        assertNotNull(peopleRef.get());
                    }
                });


    }

    @Test
    public void testParamsWeakRef() throws InterruptedException {


        People people = new People("a");
        a(people);
        people = null;
        System.gc();

        TimeUnit.SECONDS.sleep(2);
    }
}
