package io.github.horaciocome1.reaque.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.ItemUserBinding

class UsersAdapter(private val list: List<User>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private lateinit var binding: ItemUserBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.user = list[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}