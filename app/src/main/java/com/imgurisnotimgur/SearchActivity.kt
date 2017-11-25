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
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.navigation_bar.*
import kotlinx.android.synthetic.main.search_preferences.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchAct.isEnabled = false
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortPreferenceDefault = resources.getString(R.string.sort_default)
        val sortPreference = preferences.getString("sort", sortPreferenceDefault)

        val sortEntries = resources.getStringArray(R.array.sort)
        val sortIndex = PreferenceUtils.findIndexOfValue(sortEntries, sortPreference)

        sortSpinner.setSelection(sortIndex, true)

        val galleryAdapter = GalleryAdapter(arrayOf(), arrayOf(), this)
        rv_gallery.adapter = galleryAdapter

        sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                AsyncAction<Unit, Array<Image>>({ ImgurApi.getSearch(searchInput.query.toString(), position) },
                        { images -> galleryAdapter.items = images }).exec()
            }
        }

        searchInput.setOnClickListener { searchInput.isIconified = false }

        searchInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val sortIndex = sortSpinner.selectedItemPosition
                AsyncAction<Unit, Array<Image>>({ ImgurApi.getSearch(query, sortIndex) }, { images ->
                    galleryAdapter.items = images
                    rv_gallery.requestFocus()
                }).exec()
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