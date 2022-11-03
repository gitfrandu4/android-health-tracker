package com.example.android_health_tracker.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.android_health_tracker.R
import com.example.android_health_tracker.databinding.FragmentHomeBinding
import com.example.android_health_tracker.views.home.ActionsFragment
import com.example.android_health_tracker.views.home.ProfileFragment
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_actions -> {
                    childFragmentManager.beginTransaction().replace(R.id.main_container, ActionsFragment()).commit()
                    true
                }
                R.id.navigation_profile -> {
                    childFragmentManager.beginTransaction().replace(R.id.main_container, ProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.navigation_actions
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}