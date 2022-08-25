package com.udacity.asteroidradar.main

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.db.AsteroidModel
import com.udacity.asteroidradar.db.PictureOfDayModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() ,adapterDetails{

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val navController by lazy {
        val navHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }
    var today : String =""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        today = formatter.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 7)


        val format = formatter.format(calendar.time)
        viewModel.feedData(today, format)

        if(isOnline(requireContext())){
            viewModel.mainData()
            viewModel.feedData(today,format)
        }else{
            viewModel.getPicOfDayFromDB()
            viewModel.getAllAsteroidFromDB()
        }

            viewModel.requestDataLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                val data = it as PictureOfDayModel
                Picasso.get().load(data.url).into(binding.activityMainImageOfTheDay);
                binding.textView.text = data.title

            })

            viewModel.requestFeedDataLiveData.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    val data = it as ArrayList<AsteroidModel>
                    val adapter = AsteroidAdapter(this)
                    adapter.setList(requireContext(), data)
                    binding.asteroidRecycler.adapter = adapter
                })



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: selected item: ${item.itemId}")
        when(item.itemId){
            R.id.show_all_menu -> {
                viewModel.getAllAsteroidFromDB()
            }
            R.id.show_buy_menu -> {
                viewModel.getAllAsteroidFromDB()
            }
            R.id.show_rent_menu -> {
                viewModel.getTodayAsteroidFromDB(today)
            }
        }
        return true
    }

    override fun onClick(asteroidItem: AsteroidModel) {
        val item = Asteroid(asteroidItem.id,asteroidItem.codename,asteroidItem.closeApproachDate,asteroidItem.absoluteMagnitude,asteroidItem.estimatedDiameter,
        asteroidItem.relativeVelocity,asteroidItem.distanceFromEarth,asteroidItem.isPotentiallyHazardous)
        navController.navigate(MainFragmentDirections.actionShowDetail(item))
    }

    /**
     * this function to check if device connect to the internet
     * https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}
