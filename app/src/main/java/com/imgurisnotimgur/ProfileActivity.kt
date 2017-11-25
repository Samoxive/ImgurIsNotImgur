package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Account
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.navigation_bar.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileAct.isEnabled = false
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))

        val bioString = getString(R.string.bio)
        val createdAtString = getString(R.string.created_at)
        val reputationString = getString(R.string.reputation)

        nickname.text = "Samoxive"
        bio.text = "$bioString Cool guy, likes kittens"
        createdAt.text = "$createdAtString 02 Jan 2016"
        reputation.text = "$reputationString 9001"
        url.text = "https://samoxive.imgur.com"

        val accessTok = SecretUtils.getSecrets(this).second.accessToken
        AsyncAction({ ImgurApi.getSelfAccount(accessTok) }, { account ->
            val creationDate = Date(account.createdAt * 1000)
            val dateFormat = DateFormat.getLongDateFormat(this)
            val timeString = dateFormat.format(creationDate)
            nickname.text = account.username
            bio.text = "$bioString ${account.bio}"
            createdAt.text = "$createdAtString $timeString"
            reputation.text = "$reputationString ${account.reputation}"
            url.text = "https://${account.username}.imgur.com"
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.settingsButton -> {
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
            }
            R.id.quitButton -> System.exit(0)
        }

        return super.onOptionsItemSelected(item)
    }
}
