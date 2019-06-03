package io.github.horaciocome1.reaque.data.favorites

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener

class FavoritesWebService {

    private val addRequestRef = FirebaseFirestore.getInstance().collection("add_to_favorites_requests")
    private val removeRequestRef = FirebaseFirestore.getInstance().collection("remove_from_favorites_requests")
    private val auth = FirebaseAuth.getInstance()

    fun addToFavorites(user: User) {
        auth.addSimpleAuthStateListener {
            val data = mapOf(
                "user" to it.uid,
                "favorite" to user.id,
                "is_user" to true
            )
            addRequestRef.add(data)
        }
    }

    fun addToFavorites(post: Post) {
        auth.addSimpleAuthStateListener {
            val data = mapOf(
                "user" to it.uid,
                "favorite" to post.id,
                "is_post" to true
            )
            addRequestRef.add(data)
        }
    }

    fun removeFromFavorites(user: User) {
        auth.addSimpleAuthStateListener {
            val data = mapOf(
                "user" to it.uid,
                "favorite" to user.id,
                "is_user" to true
            )
            removeRequestRef.add(data)
        }
    }

    fun removeFromFavorites(post: Post) {
        auth.addSimpleAuthStateListener {
            val data = mapOf(
                "user" to it.uid,
                "favorite" to post.id,
                "is_post" to true
            )
            removeRequestRef.add(data)
        }
    }

}