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

object Constants {

    const val DEFAULT = 123456789

    // StartActivityForResult
    const val PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 1207
    const val GOOGLE_SIGN_IN_REQUEST_CODE = 1208
    const val STORAGE_PERMISSION_CODE = 1209

    // Glide
    const val CIRCLE = 2307
    const val BLUR = 2308

    // list type
    const val LISTING_TOPICS = 4401
    const val LISTING_POSTS = 4403
    const val LISTING_USERS = 4405

    // hosts
    const val FEED_FRAGMENT = 5501
    const val EXPLORE_FRAGMENT = 5502
    const val POSTS_FRAGMENT = 5503
    const val CREATE_POST_FRAGMENT = 5504
    const val USERS_FRAGMENT = 5505

    // strings
    const val LANDING_PAGE = "https://reaque.firebaseapp.com"
    const val BOOKMARKS_REQUEST = "bookmarks"
    const val SUBSCRIPTIONS_REQUEST = "subscriptions"
    const val SUBSCRIBERS_REQUEST = "subscribers"
    const val TOPIC_POSTS_REQUEST = "topic_posts"
    const val TOPIC_USERS_REQUEST = "topic_users"
    const val USER_POSTS_REQUEST = "user_posts"
    const val USER_ID = "USER_ID"
    const val MAIN_ACTIVITY = "MainActivity"

    object SharedPreferences {

        const val NAME = "SharedPreferencesPostDraft"
        const val POST_TITLE = "SharedPreferencesPostDraftPostTitle"
        const val POST_MESSAGE = "SharedPreferencesPostDraftPostMessage"
        const val TOPIC_ID = "SharedPreferencesPostDraftTopicId"
        const val TOPIC_TITLE = "SharedPreferencesPostDraftTopicTitle"

    }

}