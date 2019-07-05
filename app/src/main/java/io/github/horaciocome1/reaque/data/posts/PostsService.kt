package io.github.horaciocome1.reaque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.*

class PostsService : PostsServiceInterface {

    private val tag: String by lazy { "PostsService:" }

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("posts") }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var _post = Post("")

    private val post: MutableLiveData<Post> by lazy {
        MutableLiveData<Post>().apply { value = _post }
    }

    private val topicPosts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private val userPosts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private val top20Posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private var userId = ""

    private var topicId = ""

    override fun create(post: Post, onSuccessListener: (DocumentReference?) -> Unit) {
        auth.addSimpleAuthStateListener {
            post.user = it.user
            ref.add(post.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(post: Post): LiveData<Post> {
        if (post.id != _post.id)
                ref.document(post.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                    _post = snapshot.post
                    this.post.value = _post
                }
        return this.post
    }

    override fun get(user: User): LiveData<List<Post>> {
        if (user.id != userId) {
            ref.whereEqualTo("user.id", user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                this.userPosts.value = snapshot.posts
            }
            userId = user.id
        }
        return userPosts
    }

    override fun get(topic: Topic): LiveData<List<Post>> {
        if (topic.id != topicId) {
            ref.whereEqualTo("topic.id", topic.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                this.topicPosts.value = snapshot.posts
            }
            topicId = topic.id
        }
        return topicPosts
    }

    override fun getTop20(): LiveData<List<Post>> {
        ref.orderBy("score", Query.Direction.DESCENDING).limit(20)
            .addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                this.top20Posts.value = snapshot.posts
            }
        return top20Posts
    }

}