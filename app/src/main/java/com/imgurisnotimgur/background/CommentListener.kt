package com.imgurisnotimgur.background

import android.content.Context
import com.firebase.jobdispatcher.*
import com.imgurisnotimgur.AsyncAction
import com.imgurisnotimgur.Imgur
import com.imgurisnotimgur.Notification
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.data.BookmarkRecords
import com.imgurisnotimgur.data.CommentRecord
import com.imgurisnotimgur.entities.Image

class CommentListener: JobService() {
    companion object {
        fun initJob(context: Context) {
            val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

            val myJob = dispatcher.newJobBuilder()
                    .setService(CommentListener::class.java)
                    .setTag("comment-listener")
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(120, 135))
                    .build()

            dispatcher.mustSchedule(myJob)
        }
    }

    var action: AsyncAction<*>? = null

    override fun onStopJob(job: JobParameters?): Boolean {
        action?.cancel(false)
        return false
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        action = AsyncAction({
            val bookmarks = BookmarkRecords.getInstancesFromCursor(
                    contentResolver.query(BookmarkRecords.CONTENT_URI, BookmarkRecords.COLUMNS, null, null, null)
            )

            bookmarks.map {
                // for the love of god, make this abomination of a line prettier before submission
                val cursor = contentResolver.query(CommentRecord.buildImageCommentsUri(CommentRecord.getIdThingy(it)), CommentRecord.COLUMNS, null, null, null)
                Triple(it, Imgur.getComments(contentResolver, cursor, it), ImgurApi.getComments(it))
            }.filter {
                val (_, oldComments, newComments) = it
                oldComments.size != newComments.size
            }.forEach { sendNewCommentsNotification(it.first) }
        }, {})
        return true
    }

    private fun sendNewCommentsNotification(image: Image) {
        Notification.newCommentNotification(applicationContext, image)
    }
}
