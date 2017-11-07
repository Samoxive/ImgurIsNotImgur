package com.imgurisnotimgur

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_gallery.*

class MainActivity : AppCompatActivity() {
    internal val itemgibicekpanpa = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery) //I WAS JUST TESTING THE GALLERY VIEW NAMED ACTIVITY GALLERY. DON'T MIND ME.

        val s = getSharedPreferences("secret", Context.MODE_PRIVATE).getString("refreshToken", null)

        if (s == null) {
            val intent = Intent(this@MainActivity, NoAuthActivity::class.java)
            startActivity(intent)}

        else {
            val gridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rv_gallery.layoutManager = gridLayoutManager
            val galleryAdapter = GalleryAdapter(9, itemgibicekpanpa)
            rv_gallery.adapter = galleryAdapter}

    }
}
