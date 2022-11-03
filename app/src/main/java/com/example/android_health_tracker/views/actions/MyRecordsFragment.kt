package com.example.android_health_tracker.views.actions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.android_health_tracker.R
import com.example.android_health_tracker.adapters.MyRecordsAdapter
import com.example.android_health_tracker.models.records.Water
import com.example.android_health_tracker.views.home.CollectionNames
import com.example.android_health_tracker.views.home.DocumentNames
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "MyRecordsFragment"

class MyRecordsFragment : Fragment() {
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth

    private val myRecords = mutableListOf<Water>()
    private lateinit var adapter: MyRecordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Mis registros"
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView y Adapter
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_my_records)
        adapter = MyRecordsAdapter(myRecords as ArrayList<Water>)
        recycler.adapter = adapter

        getWaterRecords()
    }

    private fun getWaterRecords() {
        val user = auth.currentUser
        val uid = user?.uid
        uid?.let { db.collection(CollectionNames.USERS).document(it)
            .collection(CollectionNames.ACTIONS).document(DocumentNames.WATER)
            .collection(CollectionNames.RECORDS)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (doc in document) {
                        val water = doc.toObject(Water::class.java)
                        myRecords.add(water)
                    }
                    adapter.notifyItemChanged(myRecords.size - 1)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
        }
    }
}