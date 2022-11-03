package com.example.android_health_tracker.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.android_health_tracker.R
import com.example.android_health_tracker.databinding.FragmentProfileBinding
import com.example.android_health_tracker.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Perfil"

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUser()

        binding?.btnCloseSession?.setOnClickListener {
            auth.signOut()
            view.findNavController().navigate(R.id.action_homeFragment_to_splashFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentUser() {
        val currentUser = Firebase.auth.currentUser
        db.collection("users").document(currentUser!!.uid).get().addOnSuccessListener { document ->
            if (document != null) {
                user = document.toObject(User::class.java)

                binding?.userEmail?.text = user!!.email
                binding?.userName?.text = user!!.name
                binding?.userPhone?.text = user?.phone
            }
        }
    }
}