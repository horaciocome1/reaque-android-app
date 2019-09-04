package io.github.horaciocome1.reaque.ui.posts.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentSetRatingBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_set_rating.*
import kotlin.math.roundToInt

class SetRatingFragment : Fragment() {

    private lateinit var binding: FragmentSetRatingBinding

    private val viewModel: ReadPostViewModel by lazy {
        val factory = InjectorUtils.readPostViewModelFactory
        ViewModelProviders.of(this, factory)[ReadPostViewModel::class.java]
    }

    private var rating = 0

    private var defaultRating = 0

    private var post = Post("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener(viewModel.navigateUp)
        rating_bar.setOnRatingBarChangeListener { _, rating, fromUser ->
            done_button.isEnabled = if (rating != 0f
                && rating.roundToInt() != defaultRating
                && post.id.isNotBlank()
                && fromUser
            ) {
                this.rating = rating.roundToInt()
                true
            } else
                false
        }
        done_button.setOnClickListener {
            viewModel.setRating(it, post, rating)
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val args = SetRatingFragmentArgs.fromBundle(bundle)
            this.post = Post(args.postId)
            defaultRating = args.rating
            rating_bar.rating = defaultRating.toFloat()
        }
    }

}