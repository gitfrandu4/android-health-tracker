package com.example.android_health_tracker.views.actions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.android_health_tracker.databinding.FragmentWaterBinding
import com.example.android_health_tracker.models.records.Water
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WaterFragment : Fragment() {
    private var _binding: FragmentWaterBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Agua"
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        _binding = FragmentWaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.glassPlus.setOnClickListener {
            val currentWater = binding.glassAmount.text.toString().toInt()
            binding.glassAmount.text = (currentWater + 1).toString()
        }

        binding.glassMinus.setOnClickListener {
            if (binding.glassAmount.text.toString().toInt() > 0) {
                val currentWater = binding.glassAmount.text.toString().toInt()
                binding.glassAmount.text = (currentWater - 1).toString()
            }
        }

        binding.bottlePlus.setOnClickListener {
            val currentWater = binding.bottleAmount.text.toString().toInt()
            binding.bottleAmount.text = (currentWater + 1).toString()
        }

        binding.saveButton.setOnClickListener {
            val currentWater = binding.glassAmount.text.toString()
                .toInt() * 0.250 + binding.bottleAmount.text.toString().toInt() * 1

            val user = auth.currentUser
            val userRef = db.collection("users").document(user!!.uid)

            val waterRecord: Water = Water(amount = currentWater, date = System.currentTimeMillis())

            userRef.collection("actions").document("water")
                .collection("records").add(waterRecord)
                .addOnSuccessListener {
                activity?.onBackPressed()
            }.addOnFailureListener { ex ->
                Log.e("WaterFragment", "Error adding document", ex)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}