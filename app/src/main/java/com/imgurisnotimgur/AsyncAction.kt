package com.imgurisnotimgur

import android.os.AsyncTask

class AsyncAction<P, R>(val asyncRunnable: (P) -> R, val resultHandler: (R) -> Unit): AsyncTask<P, Void, R>() {
    override fun doInBackground(vararg params: P): R {
        return asyncRunnable(params.first())
    }

    override fun onPostExecute(result: R) {
        resultHandler(result)
    }
}