package com.example.movies

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.movies.databinding.ActivityMainBinding
import com.example.movies.utils.Constants.ASCENDING
import com.example.movies.utils.Constants.DESCENDING
import com.example.movies.utils.MovieUtils.checkNetwork
import com.example.movies.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

// Movie App Main Activity
@AndroidEntryPoint(AppCompatActivity::class)
class MainActivity : Hilt_MainActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel by viewModels<MoviesViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        viewModel.callGenreListApi(checkNetwork(this))
        viewModel.callMoviesListApi(checkNetwork(this))
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


}
