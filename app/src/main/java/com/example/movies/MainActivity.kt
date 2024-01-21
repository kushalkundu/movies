package com.example.movies

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.models.Genre
import com.example.movies.utils.Constants.ASCENDING
import com.example.movies.utils.Constants.DARK_MODE
import com.example.movies.utils.Constants.DESCENDING
import com.example.movies.utils.Constants.LIGHT_MODE
import com.example.movies.utils.Constants.MOBILE_THEME
import com.example.movies.utils.MovieUtils.checkNetwork
import com.example.movies.utils.PreferenceUtils
import com.example.movies.viewmodels.MoviesViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

// Movie App Main Activity
@AndroidEntryPoint(AppCompatActivity::class)
class MainActivity : Hilt_MainActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<MoviesViewModel>()
    private val genreList: ArrayList<Genre> = ArrayList()
    private lateinit var navController: NavController
    private val preferenceUtils: PreferenceUtils by lazy {
        PreferenceUtils(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        viewModel.callGenreListApi(checkNetwork(this))
        viewModel.callMoviesListApi(checkNetwork(this))
        createTabItem(Genre(id = 0, name = resources.getString(com.example.movies.R.string.all)))
        callObservers()
        handleTabLayoutClick()
        setSupportActionBar(binding.toolbar)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sorting_menu, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.sortAscending -> {
                MoviesViewModel.sortBy = ASCENDING
                item.isChecked = true
                viewModel.callMoviesListApi(checkNetwork(this))
                true
            }

            R.id.sortDescending -> {
                MoviesViewModel.sortBy = DESCENDING
                item.isChecked = true
                viewModel.callMoviesListApi(checkNetwork(this))
                true
            }

            else -> super.onOptionsItemSelected(item);

        }
    }

    private fun callObservers() {
        viewModel.genreMutableLiveData.observe(this) {
            if (!it.isNullOrEmpty()) {
                genreList.clear()
                genreList.addAll(it)
                it.forEach { genre -> createTabItem(genre) }
            }
        }
    }


//    private fun toggleTheme(isDarkMode: Boolean) {
//        if (isDarkMode) {
////            setTheme(R.style.Base_Theme_Movies_Dark)
//        } else {
//            setTheme(R.style.Base_Theme_Movies)
//        }
//        onRestart()
//    }


    private fun handleTabLayoutClick() {
        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.switchCompat.text = resources.getString(R.string.dark_mode)
//                toggleTheme(true)
                preferenceUtils.saveInt(MOBILE_THEME, DARK_MODE)

            } else {
                binding.switchCompat.text = resources.getString(R.string.light_mode)
//                toggleTheme(false)
                preferenceUtils.saveInt(MOBILE_THEME, LIGHT_MODE)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.genreSelected = tab!!.id
                viewModel.callMoviesListApi(checkNetwork(this@MainActivity))
                if (navController.currentDestination?.id == R.id.detailFragment) {
                    navController.popBackStack()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do Nothing
            }

        })
    }


    private fun createTabItem(genre: Genre) {
        val tab = binding.tabLayout.newTab().apply {
            this.text = genre.name
            this.id = genre.id!!
        }
        binding.tabLayout.addTab(tab)
    }
}