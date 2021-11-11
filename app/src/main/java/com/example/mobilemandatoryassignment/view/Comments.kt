package com.example.mobilemandatoryassignment.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilemandatoryassignment.R
import com.example.mobilemandatoryassignment.databinding.FragmentCommentsBinding
import com.example.mobilemandatoryassignment.databinding.FragmentLogincreateBinding
import com.example.mobilemandatoryassignment.databinding.FragmentSinglemessageBinding
import com.example.mobilemandatoryassignment.model.CommentAdapter
import com.example.mobilemandatoryassignment.viewmodel.CommentViewModel
import com.example.mobilemandatoryassignment.viewmodel.FirebaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Comments : Fragment() {

    private var _binding: FragmentCommentsBinding? = null

    private val commentViewModel: CommentViewModel by activityViewModels()
    private var firebaseViewModel: FirebaseViewModel = FirebaseViewModel()

    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mAuth = FirebaseAuth.getInstance()
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                findNavController().navigate(R.id.action_comments_to_LoggedInFragment)
                true
            }
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    firebaseViewModel.signOut()
                    findNavController().navigate(R.id.action_comments_to_LoginCreateFragment)
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

        val currentUser = mAuth.currentUser
        binding.textviewCurrentUser.text = "Current user: " + currentUser?.email

        val bundle = requireArguments()
        val args: CommentsArgs
        val commentsArgs: CommentsArgs = CommentsArgs.fromBundle(bundle)
        val position = commentsArgs.position
        val comment = commentViewModel[position]

        if (comment == null) {
            binding.textviewMessage.text = "No comment."
            return
        }

        binding.content.setText(comment.content)
        binding.user.setText(comment.user)
        binding.idInput.setText(comment.id.toString())

        val user = binding.user.text.toString()

        binding.buttonDeleteComment.setOnClickListener {
            val messageId = commentViewModel.messageId
            val commentId = binding.idInput.text.toString().toInt()
            if (user != currentUser?.email) {
                Snackbar.make(binding.root, "Not allowed. Wrong user.", Snackbar.LENGTH_LONG).show()
            } else {
                commentViewModel.delete(messageId, commentId)
                Snackbar.make(binding.root, "Comment deleted", Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}