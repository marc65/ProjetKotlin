package fr.epf.mm.projetandroid
//import DetailsFilmActivity
import MovieAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.projetandroid.ApiManager
import fr.epf.mm.projetandroid.Movie
import fr.epf.mm.projetandroid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)

        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString()
            searchMovies(query)
        }
    }

    private fun searchMovies(query: String) {
        val apiKey = "0b96ae7ab4d6f3ff74468ec58e787def" // Votre clé API TMDB
        val service = ApiManager.tmdbApiService

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = service.searchMovies(apiKey, query)
                if (response.isSuccessful) {
                    val searchResult = response.body()
                    val movies = searchResult?.results
                    if (movies != null) {
                        withContext(Dispatchers.Main) {
                            showMovies(movies)
                        }
                    } else {
                        // Gérer le cas où la liste de films est null
                    }
                } else {
                    // Gérer les erreurs de la requête
                    val errorMessage = response.message()
                    // Afficher ou traiter l'erreur
                }
            } catch (e: Exception) {
                // Gérer les exceptions
                e.printStackTrace()
            }
        }
    }

    private fun showMovies(movies: List<Movie>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MovieAdapter(movies)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { movie ->
            val intent = Intent(this@MainActivity, DetailsFilmActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
    }
}
