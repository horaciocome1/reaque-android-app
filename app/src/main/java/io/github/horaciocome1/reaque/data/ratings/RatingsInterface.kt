package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

interface RatingsInterface {

    fun set(post: Post, value: Int, onCompleteListener: (Task<Unit?>?) -> Unit)

    fun get(post: Post): LiveData<Int>

}