package io.github.horaciocome1.reaque.data.shares

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

interface SharesInterface {

    fun share(post: Post, onCompleteListener: (Task<Void?>?) -> Unit)

}