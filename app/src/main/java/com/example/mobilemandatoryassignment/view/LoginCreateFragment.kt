package com.example.mobilemandatoryassignment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mobilemandatoryassignment.R
import com.example.mobilemandatoryassignment.viewmodel.FirebaseViewModel
import com.example.mobilemandatoryassignment.databinding.FragmentLogincreateBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginCreateFragment : Fragment() {

    private var _binding: FragmentLogincreateBinding? = null

    private var firebaseViewModel: FirebaseViewModel = FirebaseViewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLogincreateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener{
            val email = binding.emailInputField.text.toString().trim()
            val password = binding.passwordInputField.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailInputField.error = "No email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInputField.error = "No password"
                return@setOnClickListener
            }

            firebaseViewModel.signIn(email, password)

            firebaseViewModel.user.observe(viewLifecycleOwner, Observer{
                if (firebaseViewModel.user != null) {
                    findNavController().navigate(R.id.action_LoginCreateFragment_to_loggedInFragment)
                }
            })

            firebaseViewModel.message.observe(viewLifecycleOwner, {
                if (firebaseViewModel.message != null) {
                    binding.messageView.text = firebaseViewModel.message.value
                }
            })
        }

        binding.buttonCreateUser.setOnClickListener {
            val email = binding.emailInputField.text.toString().trim()
            val password = binding.passwordInputField.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailInputField.error = "No email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInputField.error = "No password"
                return@setOnClickListener
            }

            firebaseViewModel.createUser(email, password)

            firebaseViewModel.user.observe(viewLifecycleOwner, {
                if (firebaseViewModel.user != null) {
                    binding.messageView.text = "User created, please sign in."
                }
            })

            firebaseViewModel.message.observe(viewLifecycleOwner, {
                if (firebaseViewModel.message != null) {
                    binding.messageView.text = firebaseViewModel.message.value
                }
            })
        }

        binding.testButton.setOnClickListener{
            findNavController().navigate(R.id.action_LoginCreateFragment_to_loggedInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}