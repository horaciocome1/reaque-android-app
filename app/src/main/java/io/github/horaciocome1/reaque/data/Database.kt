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
import io.github.horaciocome1.reaque.data.configurations.ConfigurationsService
import io.github.horaciocome1.reaque.data.feed.FeedService
import io.github.horaciocome1.reaque.data.posts.PostsService
import io.github.horaciocome1.reaque.data.ratings.RatingsService
import io.github.horaciocome1.reaque.data.readings.ReadingsService
import io.github.horaciocome1.reaque.data.shares.SharesService
import io.github.horaciocome1.reaque.data.storage.StorageService
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsService
import io.github.horaciocome1.reaque.data.topics.TopicsService
import io.github.horaciocome1.reaque.data.users.UsersService

class Database private constructor() {

    val bookmarksService: BookmarksService by lazy {
        BookmarksService()
    }

    val feedsService: FeedService by lazy {
        FeedService()
    }

    val postsService: PostsService by lazy {
        PostsService()
    }

    val ratingsService: RatingsService by lazy {
        RatingsService()
    }

    val readingsService: ReadingsService by lazy {
        ReadingsService()
    }

    val sharesService: SharesService by lazy {
        SharesService()
    }

    val storageService: StorageService by lazy {
        StorageService()
    }

    val subscriptionsService: SubscriptionsService by lazy {
        SubscriptionsService()
    }

    val topicsService: TopicsService by lazy {
        TopicsService()
    }

    val usersService: UsersService by lazy {
        UsersService()
    }

    val configurationsService: ConfigurationsService by lazy {
        ConfigurationsService()
    }

    companion object {

        @Volatile
        private var instance: Database? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Database()
                .also {
                    instance = it
                }
        }

    }

}