package com.imgurisnotimgur

import android.os.AsyncTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AsyncAction<R>(val asyncRunnable: () -> R, val resultHandler: (R) -> Unit): AsyncTask<Unit, Void, R>() {
    companion object {
        val pool: ExecutorService = Executors.newCachedThreadPool()
    }

    init {
        exec()
    }

    override fun doInBackground(vararg params: Unit): R = asyncRunnable()
    override fun onPostExecute(result: R) = resultHandler(result)
    private fun exec() = super.executeOnExecutor(pool)
}