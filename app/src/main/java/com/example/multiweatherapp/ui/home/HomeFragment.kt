package com.example.multiweatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.multiweatherapp.R
import com.example.multiweatherapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textViewLocation = root.findViewById<TextView>(R.id.textview_location)

        homeViewModel.location.observe(viewLifecycleOwner, Observer {
            textViewLocation.text = it
        })

        val textViewTemperature = root.findViewById<TextView>(R.id.textview_temperature)

        homeViewModel.temperature.observe(viewLifecycleOwner, Observer {

            textViewTemperature.text = it

        })

        val textViewDescription = root.findViewById<TextView>(R.id.textview_description)

        homeViewModel.description.observe(viewLifecycleOwner, Observer {

            textViewDescription.text = it

        })

        val textViewProvider = root.findViewById<TextView>(R.id.textview_provider)

        homeViewModel.provider.observe(viewLifecycleOwner, Observer {

            textViewProvider.text = it


        })

        val imageView = root.findViewById<ImageView>(R.id.imageview_weathericon)
        homeViewModel.iconBitmap.observe(viewLifecycleOwner) {
            imageView.setImageBitmap(it)
        }
        homeViewModel.retrieveWeatherData()
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}