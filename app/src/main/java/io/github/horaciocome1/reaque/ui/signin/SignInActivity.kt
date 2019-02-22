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

package io.github.horaciocome1.reaque.ui.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.R
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_sign_in.*

fun Context.getSignInActivityIntent() = Intent(this, SignInActivity::class.java)

class SignInActivity : AppCompatActivity() {

    private val backgroundUrl =
        "http://assets.worldwildlife.org/photos/15262/images/story_full_width/Medium_WW146784.jpg?1520455432"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        activity_sign_in_button.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this)
            .load(backgroundUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions.bitmapTransform(BlurTransformation(3, 7)))
            .into(activity_sign_in_imageview)
    }

}