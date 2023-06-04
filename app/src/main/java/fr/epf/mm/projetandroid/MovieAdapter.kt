import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.mm.projetandroid.ApiManager
import fr.epf.mm.projetandroid.Movie
import fr.epf.mm.projetandroid.MovieDetails
import fr.epf.mm.projetandroid.R

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var onItemClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val movie = movies[position]
                    onItemClickListener?.invoke(movie)
                }
            }
        }

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
            val voteAverage = movie.voteAverage ?: 0f
            ratingBar.rating = voteAverage / 2
            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            Glide.with(itemView)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Image de remplacement en cas de chargement ou d'erreur
                .into(posterImageView)
        }
    }
    private suspend fun getMovieDetails(movieId: Int): MovieDetails? {
        val apiKey = "0b96ae7ab4d6f3ff74468ec58e787def"
        val service = ApiManager.tmdbApiService

        return try {
            val response = service.getMovieDetails(movieId, apiKey)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Gérer les erreurs de la requête
                val errorMessage = response.message()
                null
            }
        } catch (e: Exception) {
            // Gérer les exceptions
            e.printStackTrace()
            null
        }
    }

}
