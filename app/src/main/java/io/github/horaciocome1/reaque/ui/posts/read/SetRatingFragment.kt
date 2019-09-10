package io.github.horaciocome1.reaque.ui.posts.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_set_rating.*
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlin.math.roundToInt

class SetRatingFragment
    : Fragment(),
    RatingBar.OnRatingBarChangeListener,
    View.OnClickListener {

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
        return inflater.inflate(R.layout.fragment_set_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appbar_title_textview.text = getString(R.string.set_rating)
        toolbar.setNavigationOnClickListener(viewModel.navigateUp)
        rating_bar.onRatingBarChangeListener = this
        done_button.setOnClickListener(this)
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

    override fun onRatingChanged(
        ratingBar: RatingBar?,
        rating: Float,
        isChangeFromUser: Boolean
    ) {
        done_button.isEnabled = if (
            rating != 0f
            && rating.roundToInt() != defaultRating
            && post.id.isNotBlank()
            && isChangeFromUser
        ) {
            this.rating = rating.roundToInt()
            true
        } else
            false
    }

    override fun onClick(view: View?) {
        if (view != null)
            viewModel.setRating(view, post, rating)
    }

}