package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.gallery_preferences.*

class MainActivity : AppCompatActivity() {
    val itemgibicekpanpa = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO(sam): Revert this back to a main activity, ideally we should launch the gallery activity if there isn't a problem with the tokens
        setContentView(R.layout.activity_gallery) //I WAS JUST TESTING THE GALLERY VIEW NAMED ACTIVITY GALLERY. DON'T MIND ME.

        val preferences = getSharedPreferences("secret", Context.MODE_PRIVATE)
        val accessToken = preferences.getString("accessToken", null)

        if (accessToken == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)
        }  else {
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
        }
    }
}
