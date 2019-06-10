/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
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

package io.github.horaciocome1.reaque.data

import io.github.horaciocome1.reaque.data.bookmarks.BookmarksService
import io.github.horaciocome1.reaque.data.feeds.FeedsService
import io.github.horaciocome1.reaque.data.posts.PostsService
import io.github.horaciocome1.reaque.data.ratings.RatingsService
import io.github.horaciocome1.reaque.data.readings.ReadingsService
import io.github.horaciocome1.reaque.data.shares.SharesService
import io.github.horaciocome1.reaque.data.storage.StorageService
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsService
import io.github.horaciocome1.reaque.data.topics.TopicsService
import io.github.horaciocome1.reaque.data.users.UsersService

class Database private constructor() {

    var bookmarksService = BookmarksService()
    var feedsService = FeedsService()
    var postsService = PostsService()
    var ratingsService = RatingsService()
    var readingsService = ReadingsService()
    var sharesService = SharesService()
    var storageService = StorageService()
    var subscriptionsService = SubscriptionsService()
    var topicsService = TopicsService()
    var usersService = UsersService()

    companion object {

        @Volatile
        private var instance: Database? = null

        fun getInstance() = instance
            ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }

    }

}