package io.github.horaciocome1.reaque.data.readings

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

interface ReadingsInterface {

    fun read(post: Post, onCompleteListener: (Task<Void?>?) -> Unit)

}