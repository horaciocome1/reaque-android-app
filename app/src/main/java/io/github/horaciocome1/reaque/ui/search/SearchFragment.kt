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

package io.github.horaciocome1.reaque.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onStart() {
        super.onStart()
        View.GONE.run {
            fragment_search_progressbar.visibility = this
            fragment_search_recyclerview.visibility = this
        }
        fragment_search_back_imageview.visibility = View.INVISIBLE
        View.VISIBLE.run {
            fragment_search_icon_imageview.visibility = this
            fragment_search_back_imageview.visibility = this
        }

        fragment_search_topics_button.setOnClickListener {
            showTopicsList(topics())
        }

        fragment_search_posts_button.setOnClickListener {
            showPostsList(posts())
        }

        fragment_search_users_button.setOnClickListener {
            showUsersList(users())
        }
    }

    private fun prepareView() {
        fragment_search_cardview1.visibility = View.GONE
        fragment_search_icon_imageview.visibility = View.INVISIBLE
        fragment_search_back_imageview.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                fragment_search_recyclerview.visibility = View.GONE
                fragment_search_cardview1.visibility = View.VISIBLE
                visibility = View.INVISIBLE
                fragment_search_icon_imageview.visibility = View.VISIBLE
            }
        }
    }

    private fun showTopicsList(list: List<Topic>) = fragment_search_recyclerview.apply {
        layoutManager = StaggeredGridLayoutManager(4, RecyclerView.VERTICAL)
        adapter = TopicsAdapter(list)
        prepareView()
        visibility = View.VISIBLE
    }

    private fun showPostsList(list: List<Post>) = fragment_search_recyclerview.apply {
        layoutManager = StaggeredGridLayoutManager(4, RecyclerView.VERTICAL)
        adapter = PostsAdapter(list)
        prepareView()
        visibility = View.VISIBLE
    }

    private fun showUsersList(list: List<User>) = fragment_search_recyclerview.apply {
        layoutManager = StaggeredGridLayoutManager(4, RecyclerView.VERTICAL)
        adapter = UsersAdapter(list)
        prepareView()
        visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
        fragment_search_recyclerview.visibility = View.GONE
        fragment_search_back_imageview.visibility = View.INVISIBLE
        View.VISIBLE.run {
            fragment_search_cardview1.visibility = this
            fragment_search_icon_imageview.visibility = this
        }
    }

}


fun topics() = mutableListOf(
    Topic("").apply {
        title = "Aventura"
    },
    Topic("").apply {
        title = "Casamento"
    }, Topic("").apply {
        title = "Vida a dois"
    }, Topic("").apply {
        title = "Nostalgia"
    },
    Topic("").apply {
        title = "Reflexão"
    },
    Topic("").apply {
        title = "Aventura"
    },
    Topic("").apply {
        title = "Casamento"
    }, Topic("").apply {
        title = "Vida a dois"
    }, Topic("").apply {
        title = "Nostalgia"
    },
    Topic("").apply {
        title = "Reflexão"
    },
    Topic("").apply {
        title = "Aventura"
    },
    Topic("").apply {
        title = "Casamento"
    }, Topic("").apply {
        title = "Vida a dois"
    }, Topic("").apply {
        title = "Nostalgia"
    },
    Topic("").apply {
        title = "Reflexão"
    },
    Topic("").apply {
        title = "Aventura"
    },
    Topic("").apply {
        title = "Casamento"
    }, Topic("").apply {
        title = "Vida a dois"
    }, Topic("").apply {
        title = "Nostalgia"
    },
    Topic("").apply {
        title = "Reflexão"
    },
    Topic("").apply {
        title = "Aventura"
    },
    Topic("").apply {
        title = "Casamento"
    }, Topic("").apply {
        title = "Vida a dois"
    }, Topic("").apply {
        title = "Nostalgia"
    },
    Topic("").apply {
        title = "Reflexão"
    }
)

fun posts() = mutableListOf(
    Post("").apply {
        title = "AUTOPSICOGRAFIA"
        cover = "https://cdn.culturagenial.com/imagens/fernando-pessoa1-800x445-cke.jpg"
    },
    Post("").apply {
        title = "SIMULTANEIDADE"
        cover = "https://farm6.static.flickr.com/5833/30628049752_4259b406a7_b.jpg"
    },
    Post("").apply {
        title = "Carta familiar"
        cover = "https://cdn.culturagenial.com/imagens/fernando-pessoa1-800x445-cke.jpg"
    },
    Post("").apply {
        title = "Forte é quem os seus maus pensamentos domina"
        cover = "https://timendi.files.wordpress.com/2012/02/forte.jpg"
    },
    Post("").apply {
        title = "Se não soubermos esquecer, nunca estaremos livres de tristeza."
        cover =
            "https://img-s3.onedio.com/id-57ed1004b196dbaf0c2dc9cd/rev-0/raw/s-002a9da11813f5ebefe82adcb21b8c0d9f6c28d4.jpg"
    }
)

fun users() = mutableListOf(
    User("").apply {
        name = "Angela Zerbab"
        pic =
            "https://www.alvinailey.org/sites/default/files/styles/slideshow_image/public/melanie-person.jpg?itok=ocw3xkx_"
    },
    User("").apply {
        name = "Fernando Joaquim"
        pic =
            "https://image.al.com/home/bama-media/width600/img/sports_impact/photo/chuck-personjpg-28f27a6d761b6a09.jpg"
    }
)