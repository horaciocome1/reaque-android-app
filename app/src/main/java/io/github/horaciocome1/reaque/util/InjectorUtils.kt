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

package io.github.horaciocome1.reaque.util

import io.github.horaciocome1.reaque.data.Database
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.configurations.ConfigurationsRepository
import io.github.horaciocome1.reaque.data.feed.FeedRepository
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.ratings.RatingsRepository
import io.github.horaciocome1.reaque.data.readings.ReadingsRepository
import io.github.horaciocome1.reaque.data.shares.SharesRepository
import io.github.horaciocome1.reaque.data.storage.StorageRepository
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.ui.explore.ExploreViewModelFactory
import io.github.horaciocome1.reaque.ui.feed.FeedViewModelFactory
import io.github.horaciocome1.reaque.ui.more.MoreViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.PostsViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.create.CreatePostViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.read.ReadPostViewModelFactory
import io.github.horaciocome1.reaque.ui.sign_in.SignInViewModelFactory
import io.github.horaciocome1.reaque.ui.users.UsersViewModelFactory
import io.github.horaciocome1.reaque.ui.users.profile.UserProfileViewModelFactory
import io.github.horaciocome1.reaque.ui.users.update.UpdateUserViewModelFactory

object InjectorUtils {

    private val db: Database by lazy {
        Database.getInstance()
    }

    val feedViewModelFactory: FeedViewModelFactory by lazy {
        val repository = FeedRepository
            .getInstance(db.feedsService)
        FeedViewModelFactory(repository)
    }

    val exploreViewModelFactory: ExploreViewModelFactory by lazy {
        val topicsRepository = TopicsRepository
            .getInstance(db.topicsService)
        val postsRepository = PostsRepository
            .getInstance(db.postsService)
        ExploreViewModelFactory(topicsRepository, postsRepository)
    }

    val moreViewModelFactory: MoreViewModelFactory by lazy {
        val bookmarksRepository = BookmarksRepository
            .getInstance(db.bookmarksService)
        val configurationsRepository = ConfigurationsRepository
            .getInstance(db.configurationsService)
        MoreViewModelFactory(
            bookmarksRepository,
            configurationsRepository
        )
    }

    val postsViewModelFactory: PostsViewModelFactory by lazy {
        val postsRepository = PostsRepository
            .getInstance(db.postsService)
        val bookmarksRepository = BookmarksRepository
            .getInstance(db.bookmarksService)
        PostsViewModelFactory(
            postsRepository,
            bookmarksRepository
        )
    }

    val readPostViewModelFactory: ReadPostViewModelFactory by lazy {
        val postsRepository = PostsRepository
            .getInstance(db.postsService)
        val readingsRepository = ReadingsRepository
            .getInstance(db.readingsService)
        val sharesRepository = SharesRepository
            .getInstance(db.sharesService)
        val ratingsRepository = RatingsRepository
            .getInstance(db.ratingsService)
        val bookmarksRepository = BookmarksRepository
            .getInstance(db.bookmarksService)
        ReadPostViewModelFactory(
            postsRepository,
            readingsRepository,
            sharesRepository,
            ratingsRepository,
            bookmarksRepository
        )
    }

    val createPostViewModelFactory: CreatePostViewModelFactory by lazy {
        val topicsRepository = TopicsRepository
            .getInstance(db.topicsService)
        val postsRepository = PostsRepository
            .getInstance(db.postsService)
        val storageRepository = StorageRepository
            .getInstance(db.storageService)
        CreatePostViewModelFactory(
            postsRepository,
            topicsRepository,
            storageRepository
        )
    }

    val usersViewModelFactory: UsersViewModelFactory by lazy {
        val usersRepository = UsersRepository
            .getInstance(db.usersService)
        val subscriptionsRepository = SubscriptionsRepository
            .getInstance(db.subscriptionsService)
        UsersViewModelFactory(
            usersRepository,
            subscriptionsRepository
        )
    }

    val userProfileViewModelFactory: UserProfileViewModelFactory by lazy {
        val usersRepository = UsersRepository
            .getInstance(db.usersService)
        val subscriptionsRepository = SubscriptionsRepository
            .getInstance(db.subscriptionsService)
        UserProfileViewModelFactory(
            usersRepository,
            subscriptionsRepository
        )
    }

    val updateUserViewModelFactory: UpdateUserViewModelFactory by lazy {
        val repository = UsersRepository
            .getInstance(db.usersService)
        UpdateUserViewModelFactory(repository)
    }

    val signInViewModelFactory: SignInViewModelFactory by lazy {
        SignInViewModelFactory()
    }

}