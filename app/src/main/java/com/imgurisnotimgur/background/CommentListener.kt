package com.imgurisnotimgur.background

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.imgurisnotimgur.AsyncAction
import com.imgurisnotimgur.Imgur
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.data.BookmarkRecords
import com.imgurisnotimgur.data.CommentRecord


class CommentListener: JobService() {
    var action: AsyncAction<*>? = null
    override fun onStopJob(job: JobParameters?): Boolean {
        action = AsyncAction({
            val bookmarks = BookmarkRecords.getInstancesFromCursor(
                    contentResolver.query(BookmarkRecords.CONTENT_URI, BookmarkRecords.COLUMNS, null, null, null)
            )

            bookmarks.map {
                // for the love of god, make this abomination of a line prettier before submission
                val cursor = contentResolver.query(CommentRecord.buildImageCommentsUri(CommentRecord.getIdThingy(it)), CommentRecord.COLUMNS, null, null, "${CommentRecord.COLUMN_POINTS} DESC")
                Imgur.getComments(contentResolver, cursor, it) to ImgurApi.getComments(it)
            }.filter {
                val (oldComments, newComments) = it
                oldComments.size != newComments.size
            }
        }, {})
        return true
    }

    override fun onStartJob(job: JobParameters?): Boolean {
        action?.cancel(false)
        return false
    }
}
