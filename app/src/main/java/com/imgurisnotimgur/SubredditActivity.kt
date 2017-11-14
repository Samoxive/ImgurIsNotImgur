package com.imgurisnotimgur

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_subreddit.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class SubredditActivity : AppCompatActivity() {
    val itemgibicekpanpa = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)

        subredditAct.isEnabled = false
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortPreference = preferences.getString("sort", "Hot")
        val sectionPreference = preferences.getString("section", "Viral")
        val subredditPreference = preferences.getString("subreddit", "/r/CatsStandingUp")

        val sectionIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sectionEntries, sectionPreference)
        val sortIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sortEntries, sortPreference)

        sectionSpinner.setSelection(sectionIndex, true)
        sortSpinner.setSelection(sortIndex, true)
        subredditInput.setText(subredditPreference, TextView.BufferType.EDITABLE)

        val galleryAdapter = GalleryAdapter(itemgibicekpanpa)
        rv_gallery.adapter = galleryAdapter
        sectionSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position) as String
            }
        }
        sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position) as String
            }
        }

        rv_gallery.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }
}
