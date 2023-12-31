package com.nhom1.oxygen.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun runSingle(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    onError: (e: Throwable) -> Unit = {
        errorLog(
            "runSingle: error: ${it.message}\n${
                it.stackTrace.joinToString(
                    "\n"
                )
            }\n$it"
        )
    },
    callback: () -> Unit,
): Disposable {
    return Single.create {
        try {
            callback.invoke()
            it.onSuccess(Unit)
        } catch (e: Throwable) {
            it.onError(e)
        }
    }.subscribeOn(subscribeOn).observeOn(observeOn).subscribe({}, onError)
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}

fun runPeriodic(
    intervalInMillis: Long,
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    callback: () -> Unit
): Disposable {
    return Observable.interval(intervalInMillis, TimeUnit.MILLISECONDS).subscribeOn(subscribeOn)
        .observeOn(observeOn).subscribe({ callback.invoke() }, {})
}

fun <T : Any> runTask(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    task: (ObservableEmitter<T>) -> Unit,
    onResult: (T) -> Unit = {},
    onError: (Throwable) -> Unit = { errorLog("runTask: error:\n${it.stackTrace.joinToString("\n")}\n$it") },
    onComplete: () -> Unit = {}
): Disposable {
    return Observable
        .create {
            task.invoke(it)
        }
        .subscribeOn(subscribeOn)
        .observeOn(observeOn)
        .subscribe(
            onResult,
            onError,
            onComplete
        )
}

fun <T : Any> Observable<T>.listen(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    onError: (Throwable) -> Unit = { errorLog(it) },
    onNext: (T) -> Unit
): Disposable {
    return this.subscribeOn(subscribeOn).observeOn(observeOn).subscribe(onNext, onError)
}

fun <T : Any> Single<T>.listen(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    onError: (Throwable) -> Unit = { errorLog(it) },
    onResult: (T) -> Unit
): Disposable {
    return this.subscribeOn(subscribeOn).observeOn(observeOn).subscribe(onResult, onError)
}

fun Completable.listen(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread(),
    onError: (Throwable) -> Unit = { errorLog(it) },
    onComplete: () -> Unit
): Disposable {
    return this.subscribeOn(subscribeOn).observeOn(observeOn).subscribe(onComplete, onError)
}

fun delay(timeMillis: Long = 500, callback: () -> Unit): Disposable {
    return Observable.timer(timeMillis, TimeUnit.MILLISECONDS).subscribe {
        callback.invoke()
    }
}