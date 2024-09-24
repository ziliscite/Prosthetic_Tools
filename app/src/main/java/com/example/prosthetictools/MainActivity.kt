package com.example.prosthetictools

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prosthetictools.DetailActivity.Companion.CURSTATE
import com.example.prosthetictools.DetailActivity.Companion.PROSTHETIC
import com.example.prosthetictools.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val LAYOUT = "layout"
    }

    private var layoutStateStr: String = "list"
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Prosthetic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Force dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            // Load data from state
            val layoutState: String? = savedInstanceState.getString(LAYOUT)

            if (layoutState != null) {
                layoutStateStr = layoutState
            }
        } else {
            if (intent != null) {
                val mainLayoutNul = intent.getStringExtra(MainActivity.LAYOUT)
                mainLayoutNul?.let {
                    layoutStateStr = mainLayoutNul
                }
            }
        }

        binding.rvProsthetic.setHasFixedSize(true)
        list.addAll(getProstheticList())

        showRecyclerList()
    }

    @SuppressLint("Recycle")
    private fun getProstheticList(): ArrayList<Prosthetic> {
        val dataName = resources.getStringArray(R.array.tool_name)
        val dataDescription = resources.getStringArray(R.array.tool_flavor_text)
        val dataPhoto = resources.obtainTypedArray(R.array.tool_photo)

        val dataId = dataName.map {
            it.lowercase().replace(" ", "_")
        }

        val listProsthetic = ArrayList<Prosthetic>()
        for (i in dataName.indices) {
            val prosthetic = Prosthetic(dataId[i], dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listProsthetic.add(prosthetic)
        }

        return listProsthetic
    }

    private fun showRecyclerList() {
        val prostheticAdapter = if (layoutStateStr == "list") {
            binding.rvProsthetic.layoutManager = LinearLayoutManager(this@MainActivity)
            ProstheticListAdapter(list, onClick = {
                onAdapterClick(it)
            })
        } else {
            binding.rvProsthetic.layoutManager = GridLayoutManager(this@MainActivity, 2)
            ProstheticGridAdapter(list, onClick = {
                onAdapterClick(it)
            })
        }

        binding.rvProsthetic.adapter = prostheticAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAYOUT, layoutStateStr)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.layout_list -> {
                layoutStateStr = "list"
                showRecyclerList()
            }

            R.id.layout_grid -> {
                layoutStateStr = "grid"
                showRecyclerList()
            }

            R.id.about_page -> {
                val intentToDetail = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(intentToDetail)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onAdapterClick(prosthetic: Prosthetic) {
        val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.PROSTHETIC, prosthetic)
        intentToDetail.putExtra(LAYOUT, layoutStateStr)
        startActivity(intentToDetail)
    }
}
