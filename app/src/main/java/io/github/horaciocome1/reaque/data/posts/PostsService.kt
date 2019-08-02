package io.github.horaciocome1.reaque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.*

class PostsService : PostsInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val increment: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("posts" to increment)
    }

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

    private val top10Posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private var userId = ""

    private var topicId = ""

    override fun create(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            post.user = user.user
            val postRef = db.collection("posts").document()
            post.id = postRef.id
            val postOnTopicRef = db.document("topics/${post.topic.id}/posts/${postRef.id}")
            val postOnUserRef = db.document("users/${post.user.id}/posts/${postRef.id}")
            val userOnTopicRef = db.document("topics/${post.topic.id}/users/${post.user.id}")
            val topicRef = db.document("topics/${post.topic.id}")
            val userRef = db.document("users/${post.user.id}")
            db.runBatch {
                it.set(postRef, post.map)
                it.set(postOnTopicRef, post.mapSimple)
                it.set(postOnUserRef, post.mapSimple)
                it.set(userOnTopicRef, user.user.map, SetOptions.merge())
                it.set(topicRef, increment, SetOptions.merge())
                it.set(userRef, increment, SetOptions.merge())
                val data = mapOf("top_topic" to post.topic.title)
                it.set(userRef, data, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun get(post: Post): LiveData<Post> {
        if (post.id != _post.id) {
            this.post.value = Post("")
            val ref = db.document("posts/${post.id}")
            ref.addSafeSnapshotListener {
                _post = it.post
                this.post.value = _post
            }
        }
        return this.post
    }

    override fun get(user: User): LiveData<List<Post>> {
        if (user.id != userId) {
            userPosts.value = mutableListOf()
            val ref = db.collection("users/${user.id}/posts")
            ref.orderBy("score", Query.Direction.DESCENDING).limit(100).safeGet {
                this.userPosts.value = it.posts
            }
            userId = user.id
        }
        return userPosts
    }

    override fun get(topic: Topic): LiveData<List<Post>> {
        if (topic.id != topicId) {
            topicPosts.value = mutableListOf()
            val ref = db.collection("topics/${topic.id}/posts")
            ref.orderBy("score", Query.Direction.DESCENDING).limit(100).safeGet {
                this.topicPosts.value = it.posts
            }
            topicId = topic.id
        }
        return topicPosts
    }

    override fun getTop10(): LiveData<List<Post>> {
        top10Posts.value?.let { list ->
            if (list.isEmpty()) {
                val ref = db.collection("posts")
                ref.orderBy("score", Query.Direction.DESCENDING)
                    .limit(10).addSimpleSnapshotListener {
                        this.top10Posts.value = it.posts
                    }
            }
        }
        return top10Posts
    }

}