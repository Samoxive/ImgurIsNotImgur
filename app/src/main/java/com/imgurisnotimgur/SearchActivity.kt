package com.imgurisnotimgur

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class SearchActivity : AppCompatActivity() {
    val itemgibicekpanpa = intArrayOf(
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
            R.drawable.cat5,
            R.drawable.cat6,
            R.drawable.cat7
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchAct.isEnabled = false
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortPreference = preferences.getString("sort", "Hot")
        val sectionPreference = preferences.getString("section", "Viral")

        val sectionIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sectionEntries, sectionPreference)
        val sortIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sortEntries, sortPreference)

        sectionSpinner.setSelection(sectionIndex, true)
        sortSpinner.setSelection(sortIndex, true)

        val galleryAdapter = GalleryAdapter(intArrayOf(), this)
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

        searchInput.setOnClickListener { searchInput.isIconified = false }

        searchInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                galleryAdapter.items = itemgibicekpanpa
                rv_gallery.requestFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
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