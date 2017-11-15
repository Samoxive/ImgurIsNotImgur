package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.navigation_bar.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileAct.isEnabled = false
        subredditAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        uploadAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        searchAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        profileAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        galleryAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))

        nickname.setText("Samoxive")
        bio.setText("Bio: Cool guy, likes kittens")
        createdAt.setText("Created at: 02 Jan 2016")
        reputation.setText("Reputation: 9001")
        url.setText("https://samoxive.imgur.com")
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
