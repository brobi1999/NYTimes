package hu.bme.aut.nytimes.ui.list
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.bme.aut.nytimes.R
import hu.bme.aut.nytimes.databinding.RowArticleBinding
import hu.bme.aut.nytimes.model.ui.Article

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(
    ArticleComparator
) {

    private var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ArticleViewHolder {
        val binding = RowArticleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        Glide
            .with(holder.itemView.context)
            .load(article.imgUrl).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.outline_feed_24)
            .into(holder.binding.ivArticle)
        holder.binding.tvTitle.text = article.title
        holder.binding.tvByLine.text = article.byLine
        holder.binding.tvDate.text = article.dateString
        holder.binding.root.setOnClickListener {
            listener?.onArticleClicked(article)
        }
    }

    inner class ArticleViewHolder(val binding: RowArticleBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    interface Listener {
        fun onArticleClicked(article: Article)
    }

}

object ArticleComparator : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}