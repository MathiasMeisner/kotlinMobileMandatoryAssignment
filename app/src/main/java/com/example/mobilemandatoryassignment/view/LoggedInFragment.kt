package com.example.mobilemandatoryassignment.view

import android.os.Bundle
import android.view.*
import com.example.mobilemandatoryassignment.model.Message
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobilemandatoryassignment.R
import com.example.mobilemandatoryassignment.databinding.FragmentLoggedinBinding
import com.example.mobilemandatoryassignment.databinding.FragmentLogincreateBinding
import com.example.mobilemandatoryassignment.viewmodel.FirebaseViewModel
import com.example.mobilemandatoryassignment.viewmodel.MessageViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoggedInFragment : Fragment() {
    private var _binding: FragmentLoggedinBinding? = null
    private var _binding1: FragmentLogincreateBinding? = null
    private var firebaseViewModel: FirebaseViewModel = FirebaseViewModel()
    private var messageViewModel: MessageViewModel = MessageViewModel()
    private val binding get() = _binding!!
    private val binding1 get() = _binding1!!
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mAuth = FirebaseAuth.getInstance()
        _binding = FragmentLoggedinBinding.inflate(inflater, container, false)
        _binding1 = FragmentLogincreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    firebaseViewModel.signOut()
                    findNavController().navigate(R.id.action_LoggedInFragment_to_LoginCreateFragment)
                } else {
                    Snackbar.make(binding.root, "Cannot sign out", Snackbar.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonViewFeed.setOnClickListener {
            findNavController().navigate(R.id.action_LoggedInFragment_to_Feed)
        }

        binding.buttonAddMessage.setOnClickListener {
            val content: String = binding.addMessageInput.text.toString().trim()
            val user: String = binding1.emailInputField.text.toString().trim()
            if (content.isEmpty()) {
                binding.addMessageInput.error = "Please enter message."
            }

            val addmessage = Message(content, user)

            messageViewModel.add(addmessage)
            findNavController().navigate(R.id.action_LoggedInFragment_to_Feed)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}