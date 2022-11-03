package com.example.android_health_tracker.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.android_health_tracker.MainActivity
import com.example.android_health_tracker.R
import com.example.android_health_tracker.databinding.FragmentRegisterBinding
import com.example.android_health_tracker.hideKeyboard
import com.example.android_health_tracker.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Registrarte"

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = binding.registerNameInput
        val phoneInput = binding.registerPhoneInput
        val emailInput = binding.registerEmailInput
        val passwordInput = binding.registerPasswordInput
        val passwordConfirmInput = binding.registerConfirmPasswordInput

        binding.registerButton.setOnClickListener {
            hideKeyboard()

            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val phone = phoneInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = passwordConfirmInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password.length < 6) {
                    passwordInput.error = "La contraseña debe tener al menos 6 caracteres"
                } else {
                    if (!validateEmail(email)) {
                        emailInput.error = "El email no es válido"
                    } else if (!validatePassword(password, confirmPassword)) {
                        passwordConfirmInput.error = "Las contraseñas no coinciden"
                    } else {
                        createUserWithEmailAndPassword(email, password)
                    }
                }
            } else {
                if (name.isEmpty()) {
                    nameInput.error = "El nombre es obligatorio"
                }
                if (email.isEmpty()) {
                    emailInput.error = "El email es obligatorio"
                }
                if (password.isEmpty()) {
                    passwordInput.error = "La contraseña es obligatoria"
                }
                if (confirmPassword.isEmpty()) {
                    passwordConfirmInput.error = "La confirmación de la contraseña es obligatoria"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                if (task.user != null) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val firebaseUser = auth.currentUser
                    val newUser = User(
                        uid = firebaseUser!!.uid,
                        name = binding.registerNameInput.text.toString(),
                        phone = binding.registerPhoneInput.text.toString(),
                        email = binding.registerEmailInput.text.toString()
                    )
                    (activity as MainActivity).saveUser(newUser)

                    view?.findNavController()
                        ?.navigate(R.id.action_registerFragment_to_homeFragment)
                }
            }.addOnFailureListener { ex ->
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", ex)
                Toast.makeText(
                    context, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        return emailRegex.matches(email)
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        if (password.length < 6 || password != confirmPassword) {
            return false
        }
        return true
    }
}