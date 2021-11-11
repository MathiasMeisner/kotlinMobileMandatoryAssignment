package com.example.mobilemandatoryassignment.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilemandatoryassignment.R
import com.example.mobilemandatoryassignment.databinding.FragmentLogincreateBinding
import com.example.mobilemandatoryassignment.databinding.FragmentSinglemessageBinding
import com.example.mobilemandatoryassignment.model.Comment
import com.example.mobilemandatoryassignment.model.CommentAdapter
import com.example.mobilemandatoryassignment.model.Message
import com.example.mobilemandatoryassignment.model.MessageAdapter
import com.example.mobilemandatoryassignment.viewmodel.CommentViewModel
import com.example.mobilemandatoryassignment.viewmodel.FirebaseViewModel
import com.example.mobilemandatoryassignment.viewmodel.MessageViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SingleMessage : Fragment() {

    private var _binding: FragmentSinglemessageBinding? = null
    private var _binding1: FragmentLogincreateBinding? = null

    private val messageViewModel: MessageViewModel by activityViewModels()
    private val commentViewModel: CommentViewModel by activityViewModels()
    private val messageid: CommentViewModel by activityViewModels()
    private var firebaseViewModel: FirebaseViewModel = FirebaseViewModel()

    private val binding get() = _binding!!
    private val binding1 get() = _binding1!!

    private lateinit var mAuth: FirebaseAuth
    var messageId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        mAuth = FirebaseAuth.getInstance()
        _binding = FragmentSinglemessageBinding.inflate(inflater, container, false)
        _binding1 = FragmentLogincreateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                findNavController().navigate(R.id.action_SingleMessage_to_LoggedInFragment)
                true
            }
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    firebaseViewModel.signOut()
                    findNavController().navigate(R.id.action_SingleMessage_to_LoginCreateFragment)
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
        binding.textviewCurrentUser.text = currentUser?.email

        val bundle = requireArguments()
        val args: SingleMessageArgs
        val singleMessageArgs: SingleMessageArgs = SingleMessageArgs.fromBundle(bundle)
        val position = singleMessageArgs.position
        val message = messageViewModel[position]
        if (message == null) {
            binding.textviewMessage.text = "No message"
            return
        }

        if (message == null) {
            binding.idInput.text = "No id"
            return
        }
        binding.content.setText(message.content)
        binding.user.setText(message.user)
        binding.totalComments.setText(message.totalComments.toString())
        binding.idInput.setText(message.id.toString())

        messageId = binding.idInput.text.toString().toInt()
        messageid.messageId = messageId
        commentViewModel.commentLiveData.observe(viewLifecycleOwner) { comments ->
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (comments == null) View.GONE else View.VISIBLE
            if (comments != null) {
                val adapter = CommentAdapter(comments) { position ->
                    val action = SingleMessageDirections.actionSingleMessageToComments(position)
                    findNavController().navigate(action)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = adapter
            }
        }

        commentViewModel.errorCommentLiveData.observe(viewLifecycleOwner) { errorComment ->
            binding.textviewSecond.text = errorComment
        }

        commentViewModel.reload(commentViewModel.messageId)

        binding.swiperefresh.setOnRefreshListener {
            commentViewModel.reload(commentViewModel.messageId)
            binding.swiperefresh.isRefreshing = false
        }

        val user = binding.user.text.toString()

        binding.addCommentButton.setOnClickListener{
            val user = binding.textviewCurrentUser.text.toString()
            val comment = binding.addCommentInput.text.toString()
            if (comment.isEmpty()) {
                binding.addCommentInput.error = "Please enter a comment."
            }
            val addcomment = Comment(messageId, messageId, comment, user)

            commentViewModel.add(addcomment)
        }

        binding.deleteMessage.setOnClickListener {

            if (user != currentUser?.email) {
                Snackbar.make(binding.root, "Not allowed. Wrong user.", Snackbar.LENGTH_LONG).show()
            } else {
                messageViewModel.delete(message.id)
                Snackbar.make(binding.root, "Message deleted", Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
        }

//        binding.seeComments.setOnClickListener {



//            findNavController().navigate(R.id.action_SingleMessage_to_comments)


//        binding.addCommentButton.setOnClickListener {
//            val comment: String = binding.addCommentInput.text.toString().trim()
//            val messageId: Int = binding.messageId.text.toString().toInt()
//            if (comment.isEmpty()) {
//                binding.addCommentInput.error = "Please enter a comment."
//            }
//
//            val addcomment = Comment(comment, messageId)
//
//            commentViewModel.add(addcomment)
//        }
        }

}