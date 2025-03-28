package smartrecycleradapter

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import io.github.zero8.smartrecycleradapter.sample.databinding.ActivityMovieCategoryDetailsBinding
import smartadapter.Position
import smartadapter.SmartEndlessScrollRecyclerAdapter
import smartadapter.SmartRecyclerAdapter
import smartadapter.viewevent.listener.OnClickEventListener
import smartrecycleradapter.extension.GridAutoLayoutManager
import smartrecycleradapter.models.MovieData
import smartrecycleradapter.models.MovieModel
import smartrecycleradapter.utils.AssetsUtils
import smartrecycleradapter.utils.showToast
import smartrecycleradapter.viewholder.HeaderViewHolder
import smartrecycleradapter.viewholder.ThumbViewHolder

enum class MovieType(val title: String) {
    COMING_SOON("Coming soon"),
    MY_WATCH_LIST("My watch list"),
    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATED("Animated"),
    SCI_FI("Sci-fi")
}

class MovieCategoryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCategoryDetailsBinding

    private val movieData: MovieData by lazy {
        AssetsUtils.loadStyleFromAssets<MovieData>(this, "main-movie-data.json")
    }

    private lateinit var movieType: MovieType
    private var endlessScrollCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieCategoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.getIntExtra("MovieType", MovieType.ANIMATED.ordinal)?.let {
            movieType = MovieType.values()[it]
        }

        initSmartRecyclerAdapter()
    }

    private fun initSmartRecyclerAdapter() {
        val movieItems: List<Any> = when (movieType) {
            MovieType.COMING_SOON -> movieData.categories.find { it.id == "coming-soon" }!!.items
            MovieType.MY_WATCH_LIST -> movieData.categories.find { it.id == "watch-list" }!!.items
            MovieType.ACTION -> movieData.categories.find { it.id == "action" }!!.items
            MovieType.ADVENTURE -> movieData.categories.find { it.id == "adventure" }!!.items
            MovieType.ANIMATED -> movieData.categories.find { it.id == "anim" }!!.items
            MovieType.SCI_FI -> movieData.categories.find { it.id == "sci-fi" }!!.items
        }

        val adapterItems = mutableListOf(
            movieType.title,
            *movieItems.toTypedArray()
        )

        val gridAutoLayoutManager = GridAutoLayoutManager(this, 100)
        gridAutoLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Position): Int {
                return if (position == 0) gridAutoLayoutManager.spanCount else 1
            }
        }

        when (movieType) {
            MovieType.COMING_SOON, MovieType.MY_WATCH_LIST -> {
                SmartRecyclerAdapter.items(adapterItems)
                    .map(String::class, HeaderViewHolder::class)
                    .map(MovieModel::class, ThumbViewHolder::class)
                    .setLayoutManager(gridAutoLayoutManager)
                    .add(OnClickEventListener(ThumbViewHolder::class) {
                        showToast("Movie ${it.position}")
                    })
                    .into<SmartRecyclerAdapter>(binding.recyclerView)
            }
            MovieType.ACTION, MovieType.ADVENTURE, MovieType.ANIMATED, MovieType.SCI_FI -> {
                SmartEndlessScrollRecyclerAdapter.items(adapterItems)
                    .setAutoLoadMoreEnabled(true)
                    .setOnLoadMoreListener { adapter, loadMoreViewHolder ->
                        if (!adapter.isLoading) {
                            adapter.isLoading = true

                            Handler().postDelayed(
                                {
                                    adapter.addItems(movieItems)
                                    if (endlessScrollCount++ == 3) {
                                        adapter.isEndlessScrollEnabled = false
                                    }
                                    adapter.isLoading = false
                                },
                                3000
                            )
                        }
                    }
                    .map(String::class, HeaderViewHolder::class)
                    .map(MovieModel::class, ThumbViewHolder::class)
                    .setLayoutManager(gridAutoLayoutManager)
                    .add(OnClickEventListener(ThumbViewHolder::class) {
                        showToast("Movie ${it.position}")
                    })
                    .into<SmartEndlessScrollRecyclerAdapter>(binding.recyclerView)
            }
        }
    }
}
