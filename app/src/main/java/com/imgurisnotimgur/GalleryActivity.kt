package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class GalleryActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_gallery)

        galleryAct.isEnabled = false
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortPreference = preferences.getString("sort", "Hot")
        val sectionPreference = preferences.getString("section", "Viral")
        val nsfwEnabled = preferences.getBoolean("nsfw_enabled", false)

        val sectionIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sectionEntries, sectionPreference)
        val sortIndex = PreferenceUtils.findIndexOfValue(PreferenceUtils.sortEntries, sortPreference)

        sectionSpinner.setSelection(sectionIndex, true)
        sortSpinner.setSelection(sortIndex, true)

        val galleryAdapter = GalleryAdapter(arrayOf(), arrayOf(), this)
        rv_gallery.adapter = galleryAdapter
        sectionSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortPosition = sortSpinner.selectedItemPosition

                AsyncAction<Triple<Int, Int, Boolean>, Array<Image>>({ params ->
                    val (section, sort, nsfwEnabled) = params[0]
                    return@AsyncAction ImgurApi.getGallery(section, sort, nsfwEnabled)
                }, { images -> galleryAdapter.items = images }).exec(Triple(position, sortPosition, nsfwEnabled))
            }
        }
            sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // We shouldn't need to do anything here as user can't select an empty entry anyways
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val sectionPosition = sectionSpinner.selectedItemPosition

                    AsyncAction<Triple<Int, Int, Boolean>, Array<Image>>({ params ->
                        val (section, sort, nsfwEnabled) = params[0]
                        return@AsyncAction ImgurApi.getGallery(section, sort, nsfwEnabled)
                    }, { images -> galleryAdapter.items = images }).exec(Triple(sectionPosition, position, nsfwEnabled))
                }
            }

            AsyncAction<Triple<Int, Int, Boolean>, Array<Image>>({ params ->
                val (section, sort, nsfwEnabled) = params[0]
                return@AsyncAction ImgurApi.getGallery(section, sort, nsfwEnabled)
        }, { images -> galleryAdapter.items = images }).exec(Triple(sectionIndex, sortIndex, nsfwEnabled))
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
