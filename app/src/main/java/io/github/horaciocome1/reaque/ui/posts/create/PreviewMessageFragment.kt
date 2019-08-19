package io.github.horaciocome1.reaque.ui.posts.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.horaciocome1.reaque.R
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.fragment_preview_message.*

class PreviewMessageFragment : Fragment() {

    private val markwon: Markwon? by lazy {
        if (context != null)
            Markwon.create(context!!)
        else
            null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview_message, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            val message = PreviewMessageFragmentArgs.fromBundle(it).message
            markwon?.setMarkdown(message_textview, message)
        }
    }

}