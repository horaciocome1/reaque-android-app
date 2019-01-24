/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.ui.favorites

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.favorites.FavoriteRepository
import io.github.horaciocome1.reaque.utilities.InjectorUtils

fun Fragment.getFavoritesViewModel(): FavoritesViewModel {
    val factory = InjectorUtils.provideFavoritesViewModelFactory()
    return ViewModelProviders.of(this, factory).get(FavoritesViewModel::class.java)
}

class FavoritesViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    fun getPosts() = favoriteRepository.getPosts()

    fun getTopics() = favoriteRepository.getTopics()

}