package io.github.horaciocome1.reaque.data.favorites

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User

class FavoritesRepository private constructor(private val service: FavoritesWebService) {

    fun addToFavorites(post: Post) = service.addToFavorites(post)

    fun addToFavorites(user: User) = service.addToFavorites(user)

    fun removeFromFavorites(post: Post) = service.removeFromFavorites(post)

    fun removeFromFavorites(user: User) = service.removeFromFavorites(user)

    companion object {

        @Volatile
        private var instance: FavoritesRepository? = null

        fun getInstance(service: FavoritesWebService) = instance ?: synchronized(this) {
            instance ?: FavoritesRepository(service).also {
                instance = it
            }
        }

    }

}