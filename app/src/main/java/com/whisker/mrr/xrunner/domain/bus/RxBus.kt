package com.whisker.mrr.xrunner.domain.bus

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

object RxBus {
    private val subjectMap = HashMap<String, PublishSubject<Any>>()
    private val subscriptionMap = HashMap<Any, CompositeDisposable>()

    private fun getSubject(subjectKey: String): PublishSubject<Any> {
        var subject: PublishSubject<Any>? = subjectMap[subjectKey]
        if (subject == null) {
            subject = PublishSubject.create()
            subjectMap[subjectKey] = subject
        }

        return subject
    }

    private fun getCompositeDisposable(obj: Any): CompositeDisposable {
        var compositeDisposable: CompositeDisposable? = subscriptionMap[obj]
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
            subscriptionMap[obj] = compositeDisposable
        }

        return compositeDisposable
    }

    fun subscribe(subject: String, lifecycle: Any, action: Consumer<Any>) {
        val disposable: Disposable = getSubject(subject).subscribe(action)
        getCompositeDisposable(lifecycle).add(disposable)
    }

    fun unsubscribe(lifecycle: Any) {
        val compositeDisposable: CompositeDisposable? = subscriptionMap.remove(lifecycle)
        compositeDisposable?.dispose()
    }

    fun publish(event: Any) {
        getSubject(event.javaClass.name).onNext(event)
    }
}