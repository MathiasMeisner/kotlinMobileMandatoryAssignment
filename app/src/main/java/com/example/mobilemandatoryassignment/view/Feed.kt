package com.example.mobilemandatoryassignment.view

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilemandatoryassignment.R
import com.example.mobilemandatoryassignment.databinding.FragmentFeedBinding
import com.example.mobilemandatoryassignment.viewmodel.MessageViewModel
import com.example.mobilemandatoryassignment.model.MessageAdapter
import com.example.mobilemandatoryassignment.viewmodel.FirebaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Math.abs

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class Feed : Fragment() {

    private var _binding: FragmentFeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val messageViewModel: MessageViewModel by activityViewModels()
    private var firebaseViewModel: FirebaseViewModel = FirebaseViewModel()

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        mDetector = GestureDetectorCompat(activity, MyGestureListener())
        val rootView: LinearLayout = binding.root
        rootView.setOnTouchListener { view, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
            Log.d("APPLE", "Touch: " + motionEvent.x + " " + motionEvent.y)
            true
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                findNavController().navigate(R.id.action_Feed_to_LoggedInFragment)
                true
            }
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    firebaseViewModel.signOut()
                    findNavController().navigate(R.id.action_Feed_to_LoginCreateFragment)
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

        messageViewModel.messagesLiveData.observe(viewLifecycleOwner) { messages ->
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (messages == null) View.GONE else View.VISIBLE
            if (messages != null) {
                val adapter = MessageAdapter(messages) { position ->
                    val action = FeedDirections.actionFeedToSingleMessage(position)
                    findNavController().navigate(action)
                }
                binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                binding.recyclerView.adapter = adapter
            }
        }

        messageViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textviewSecond.text = errorMessage
        }

        messageViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            messageViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onLongPress(e: MotionEvent) {
            Log.d("APPLE", "onLongPress $e")
            super.onLongPress(e)
        }

        override fun onScroll(
            ev1: MotionEvent, ev2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
            Log.d("APPLE", "onScroll: $ev1 \n $ev2")
            doIt(ev1, ev2)
            return super.onScroll(ev1, ev2, distanceX, distanceY)
        }

        override fun onFling(
            ev1: MotionEvent, ev2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            Log.d("APPLE", "onFling: $ev1 \n $ev2")
            doIt(ev1, ev2)
            return super.onFling(ev1, ev2, velocityX, velocityY)
        }

        private fun doIt(ev1: MotionEvent, ev2: MotionEvent) {
            val xDiff = ev2.x - ev1.x
            if (xDiff > 300) {
                findNavController() // inner keyword on MyGesture Listener
                    .navigate(R.id.action_Feed_to_LoggedInFragment)
            }
        }
    }
}