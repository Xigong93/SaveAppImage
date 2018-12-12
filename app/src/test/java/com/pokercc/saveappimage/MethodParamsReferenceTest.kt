package com.pokercc.saveappimage

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Test
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class MethodParamsReferenceTest {

    data class People(val name: String)

    @Test
    fun testStrangeParamsRef() {
        fun a(string: WeakReference<People>) {
            Flowable.just(string)
                .subscribeOn(Schedulers.io())
                .doOnNext { TimeUnit.SECONDS.sleep(1) }
                .subscribe {
                    assertNull(string.get())
                }
        }


        fun b(string: People) {
            return a(WeakReference(string))
        }

        var s1: People? = People("a")
        b(s1!!)
        s1 = null
        System.gc()

        TimeUnit.SECONDS.sleep(2)
    }

    @Test
    fun testParamsWeakRef() {
        fun a(people: People) {
            val peopleRef = WeakReference(people)
            Completable.complete()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    TimeUnit.SECONDS.sleep(1)
//                    println(people.name)
                    assertNotNull(peopleRef.get())
                }
        }


        var s1: People? = People("a")
        a(s1!!)
        s1 = null
        System.gc()

        TimeUnit.SECONDS.sleep(2)
    }
}