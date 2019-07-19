package io.github.horaciocome1.reaque.ui.posts.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentCreatePostBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.reaque.util.OnFocusChangeListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_create_post.*

class CreatePostFragment : Fragment() {

    lateinit var binding: FragmentCreatePostBinding

    private val viewModel: CreatePostViewModel by lazy {
        val factory = InjectorUtils.createPostViewModelFactory
        ViewModelProviders.of(this, factory)[CreatePostViewModel::class.java]
    }

    private lateinit var selectTopicBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var selectPicBehavior: BottomSheetBehavior<MaterialCardView>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        select_pic_from_gallery_button.setOnClickListener {
            pickImageFromGallery()
        }
        OnFocusChangeListener(context).let {
            title_edittext?.onFocusChangeListener = it
            message_edittext?.onFocusChangeListener = it
        }
        selectTopicBehavior = BottomSheetBehavior.from(select_topics_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        selectPicBehavior = BottomSheetBehavior.from(select_pic_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        topics_recyclerview.addOnItemClickListener { _, position ->
            binding.topics?.let {
                if (it.isNotEmpty()) {
                    selectTopicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.post.topic = it[position]
                    binding.viewmodel = viewModel
                    create_button.isEnabled = viewModel.isPostReady
                }
            }
        }
        select_topic_button.setOnClickListener {
            selectTopicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        select_pic_button.setOnClickListener {
            selectPicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        toolbar?.setNavigationOnClickListener {
            viewModel.navigateUp(it)
        }
        create_button.setOnClickListener { binding.viewmodel = viewModel.create(it) }
    }

    override fun onStart() {
        super.onStart()
        viewModel.title.observe(this, Observer {
            viewModel.post.title = it
            create_button.isEnabled = viewModel.isPostReady
        })
        viewModel.message.observe(this, Observer {
            viewModel.post.message = it
            create_button.isEnabled = viewModel.isPostReady
        })
        viewModel.topics.observe(this, Observer {
            binding.topics = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE -> {
                        viewModel.imageUri = data?.data!!
                        binding.viewmodel = viewModel
                    }
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val mimeTypes = arrayOf("image/jpeg", "image/png")
                    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                }
            }
            startActivityForResult(intent, Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
        } else
            requestStoragePermission()

    }

    private fun requestStoragePermission() {
        if (activity is MainActivity)
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity as MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(activity as MainActivity)
                    .setTitle(resources.getString(R.string.permission_needed))
                    .setMessage(resources.getString(R.string.permission_needed_explanation))
                    .setPositiveButton(resources.getString(R.string.confirm)) { _, _ ->
                        ActivityCompat.requestPermissions(
                            activity as MainActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.STORAGE_PERMISSION_CODE
                        )
                    }
                    .setNegativeButton(resources.getString(R.string.reject)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            } else
                ActivityCompat.requestPermissions(
                    activity as MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.STORAGE_PERMISSION_CODE
                )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                pickImageFromGallery()
            else
                Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_LONG).show()
        }
    }
}