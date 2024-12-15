package com.example.quotes_uas.ui.quotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes_uas.R
import com.example.quotes_uas.data.local.QuotesEntity

class QuoteAdapter(
    private val role: String, // Menambahkan parameter role
    private val onFavoriteClick: (QuotesEntity) -> Unit, // Listener untuk favorite
    private val onDeleteClick: (QuotesEntity) -> Unit // Listener untuk delete
) : ListAdapter<QuotesEntity, QuoteAdapter.QuoteViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textQuote: TextView = itemView.findViewById(R.id.textQuote)
        private val textAuthor: TextView = itemView.findViewById(R.id.textAuthor)
        private val favoriteButton: ImageView = itemView.findViewById(R.id.favoriteButton)
        private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

        fun bind(quote: QuotesEntity) {
            textQuote.text = quote.kutipan
            textAuthor.text = "- ${quote.penulis}"

            // Menangani tombol berdasarkan role
            if (role == "admin") {
                favoriteButton.visibility = View.GONE // Admin tidak melihat tombol favorit
                deleteButton.visibility = View.VISIBLE // Admin melihat tombol delete
            } else {
                favoriteButton.visibility = View.VISIBLE // User melihat tombol favorit
                deleteButton.visibility = View.GONE // User tidak melihat tombol delete
            }

            // Menangani status favorit
            favoriteButton.setImageResource(
                if (quote.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )

            // Klik favorit
            favoriteButton.setOnClickListener {
                onFavoriteClick(quote)
            }

            // Klik delete
            deleteButton.setOnClickListener {
                onDeleteClick(quote)
            }
        }
    }

    class QuoteDiffCallback : DiffUtil.ItemCallback<QuotesEntity>() {
        override fun areItemsTheSame(oldItem: QuotesEntity, newItem: QuotesEntity): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: QuotesEntity, newItem: QuotesEntity): Boolean {
            return oldItem == newItem
        }
    }
}
