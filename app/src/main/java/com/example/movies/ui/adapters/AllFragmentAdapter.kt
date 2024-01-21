package com.example.movies.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.databinding.CardMovieBinding
import com.example.movies.models.MovieListModel
import com.example.movies.network.URL
import com.example.movies.utils.Constants
import com.example.movies.utils.MovieUtils


// Movie List Adapter

class AllFragmentAdapter(private val context: Context, val onClick: (Int) -> Unit) :
    PagingDataAdapter<MovieListModel, AllFragmentAdapter.MyViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CardMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(getItem(position), context)
    }

    inner class MyViewHolder(private val binding: CardMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: MovieListModel?, context: Context) {
            binding.apply {
                this.root.setOnClickListener {
                    if (data != null) {
                        onClick(data.id)
                    }
                }
                tvTitle.text = data?.title
                tvReleaseDate.text = MovieUtils.getFormattedDate(
                    data?.releaseDate,
                    Constants.YYYY_MM_DD,
                    Constants.DD_MM_YYYY
                )
                Glide.with(context).load("${URL.IMAGE_BASE_URL}${data?.posterPath}")
                    .error(R.drawable.default_movie_poster)
                    .placeholder(R.drawable.default_movie_poster).into(ivPoster)
            }

        }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<MovieListModel>() {
            override fun areItemsTheSame(
                oldItem: MovieListModel,
                newItem: MovieListModel
            ): Boolean {
                return oldItem.id == newItem.id
            }


            override fun areContentsTheSame(
                oldItem: MovieListModel,
                newItem: MovieListModel
            ): Boolean {
                return oldItem == newItem
            }


        }
    }


}