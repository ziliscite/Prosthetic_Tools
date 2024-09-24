package com.example.prosthetictools

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prosthetictools.MainActivity.Companion.LAYOUT
import com.example.prosthetictools.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val PROSTHETIC = "prosthetic"

        const val CURSTATE = "prostheticUpgrades"
    }

    private lateinit var mainLayoutStateStr: String

    private lateinit var curState: ProstheticUpgrades

    private lateinit var prosthetic: Prosthetic

    private lateinit var binding: ActivityDetailBinding
    private val upgradesList = ArrayList<ProstheticUpgrades>()

    private lateinit var dataName: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataPhoto: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Remove title and add "back button" on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rvProstheticUpgrades.setHasFixedSize(true)

        if (savedInstanceState != null) {
            // Load data from state
            val prostheticState: Prosthetic? = if (Build.VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable(PROSTHETIC, Prosthetic::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable(PROSTHETIC)
            }
            if (prostheticState != null) {
                prosthetic = prostheticState
            }

            val upgradeState: ProstheticUpgrades? = if (Build.VERSION.SDK_INT >= 33) {
                savedInstanceState.getParcelable(CURSTATE, ProstheticUpgrades::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable(CURSTATE)
            }
            if (upgradeState != null) {
                curState = upgradeState
            }

            val mainLayoutNul = savedInstanceState.getString(MainActivity.LAYOUT)
            mainLayoutNul?.let {
                mainLayoutStateStr = mainLayoutNul
            }

            bindViews(curState)
        } else {
            // Load the prosthetic intent from main if no state is saved
            getProsthetic()
        }

        // Load the upgrades data of a main prosthetic
        val upgrades = getProstheticUpgradesList()
        upgradesList.addAll(upgrades)
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intentToMain = Intent(this@DetailActivity, MainActivity::class.java)
                intentToMain.putExtra(LAYOUT, mainLayoutStateStr)
                startActivity(intentToMain)
                true
            }

            // Share prosthetic upgrades descriptions
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, curState.description)
                }

                startActivity(
                    Intent.createChooser(shareIntent, "Share ${curState.name}")
                )

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(PROSTHETIC, prosthetic)
        outState.putParcelable(CURSTATE, curState)

        outState.putString(MainActivity.LAYOUT, mainLayoutStateStr)
    }

    private fun getProsthetic() {
        val prostheticNul = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(PROSTHETIC, Prosthetic::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(PROSTHETIC)
        }
        prostheticNul?.let {
            prosthetic = prostheticNul
        }

        val mainLayoutNul = intent.getStringExtra(MainActivity.LAYOUT)
        mainLayoutNul?.let {
            mainLayoutStateStr = mainLayoutNul
        }

        curState = ProstheticUpgrades(prosthetic.name, prosthetic.description, prosthetic.photo)
        bindViews(curState)
    }

    @SuppressLint("Recycle")
    private fun getProstheticUpgradesList(): ArrayList<ProstheticUpgrades> {
        initializeDetail(prosthetic.id)

        val listUpgrades = ArrayList<ProstheticUpgrades>()

        // Main prosthetic also gets added to the variant
        listUpgrades.add(ProstheticUpgrades(prosthetic.name, prosthetic.description, prosthetic.photo))

        for (i in dataName.indices) {
            val upgrade = ProstheticUpgrades(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listUpgrades.add(upgrade)
        }

        return listUpgrades
    }

    private fun bindViews(upgrades: ProstheticUpgrades) {
        binding.ivUpgrades.setImageResource(upgrades.photo)
        binding.tvName.text = upgrades.name
        binding.tvDescription.text = upgrades.description
    }

    private fun showRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rvProstheticUpgrades.layoutManager = layoutManager

        val prostheticListAdapter = ProstheticUpgradesListAdapter(upgradesList, onClick = {
            // Save current state
            curState = it.copy()

            bindViews(it)
        })

        binding.rvProstheticUpgrades.adapter = prostheticListAdapter
    }

    private fun initializeDetail(prostheticId: String) {
        // Get array of data to load based off the main prosthetic id
        when(prostheticId) {
            "loaded_axe" -> setData(
                resources.getStringArray(R.array.loaded_axe_upgrades),
                resources.getStringArray(R.array.loaded_axe_upgrades_description),
                resources.obtainTypedArray(R.array.loaded_axe_upgrades_photo)
            )
            "loaded_shuriken" -> setData(
                resources.getStringArray(R.array.loaded_shuriken_upgrades),
                resources.getStringArray(R.array.loaded_shuriken_upgrades_description),
                resources.obtainTypedArray(R.array.loaded_shuriken_upgrades_photo)
            )
            "loaded_spear" -> setData(
                resources.getStringArray(R.array.loaded_spear_upgrades),
                resources.getStringArray(R.array.loaded_spear_upgrades_description),
                resources.obtainTypedArray(R.array.loaded_spear_upgrades_photo)
            )
            "loaded_umbrella" -> setData(
                resources.getStringArray(R.array.loaded_umbrella_upgrades),
                resources.getStringArray(R.array.loaded_umbrella_upgrades_description),
                resources.obtainTypedArray(R.array.loaded_umbrella_upgrades_photo)
            )
            "flame_vent" -> setData(
                resources.getStringArray(R.array.flame_vent_upgrades),
                resources.getStringArray(R.array.flame_vent_upgrades_description),
                resources.obtainTypedArray(R.array.flame_vent_upgrades_photo)
            )
            "shinobi_firecracker" -> setData(
                resources.getStringArray(R.array.shinobi_firecracker_upgrades),
                resources.getStringArray(R.array.shinobi_firecracker_upgrades_description),
                resources.obtainTypedArray(R.array.shinobi_firecracker_upgrades_photo)
            )
            "sabimaru" -> setData(
                resources.getStringArray(R.array.sabimaru_upgrades),
                resources.getStringArray(R.array.sabimaru_upgrades_description),
                resources.obtainTypedArray(R.array.sabimaru_upgrades_photo)
            )
            "mist_raven" -> setData(
                resources.getStringArray(R.array.mist_raven_upgrades),
                resources.getStringArray(R.array.mist_raven_upgrades_description),
                resources.obtainTypedArray(R.array.mist_raven_upgrades_photo)
            )
            "divine_abduction" -> setData(
                resources.getStringArray(R.array.divine_abduction_upgrades),
                resources.getStringArray(R.array.divine_abduction_upgrades_description),
                resources.obtainTypedArray(R.array.divine_abduction_upgrades_photo)
            )
            "finger_whistle" -> setData(
                resources.getStringArray(R.array.finger_whistle_upgrades),
                resources.getStringArray(R.array.finger_whistle_upgrades_description),
                resources.obtainTypedArray(R.array.finger_whistle_upgrades_photo)
            )
            else -> setData(
                resources.getStringArray(R.array.loaded_axe_upgrades),
                resources.getStringArray(R.array.loaded_axe_upgrades_description),
                resources.obtainTypedArray(R.array.loaded_axe_upgrades_photo)
            )
        }
    }

    private fun setData(name: Array<String>, description: Array<String>, photo: TypedArray) {
        dataName = name
        dataDescription = description
        dataPhoto = photo
    }
}