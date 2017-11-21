package com.imgurisnotimgur

import android.os.AsyncTask
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AsyncAction<P, R>(val asyncRunnable: (Array<out P>) -> R, val resultHandler: (R) -> Unit): AsyncTask<P, Void, R>() {
    companion object {
        val pool: Executor = Executors.newCachedThreadPool()
    }

    override fun doInBackground(vararg params: P): R = asyncRunnable(params)

    override fun onPostExecute(result: R) {
        resultHandler(result)
    }

    fun exec(vararg params: P) {
        super.executeOnExecutor(pool, *params)
    }
}