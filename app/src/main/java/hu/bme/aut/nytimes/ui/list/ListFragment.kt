package hu.bme.aut.nytimes.ui.list

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.*
import android.widget.Toast
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.nytimes.R
import hu.bme.aut.nytimes.databinding.FragmentListBinding
import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.util.Period
import androidx.recyclerview.widget.LinearLayoutManager
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.navigation.navigator
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.nytimes.ui.detail.DetailFragment
import hu.bme.aut.nytimes.util.FailedToLoad
import hu.bme.aut.nytimes.util.NetworkCallbackHelper.registerNetworkCallback
import hu.bme.aut.nytimes.util.NetworkCallbackHelper.unregisterNetworkCallback
import hu.bme.aut.nytimes.util.NetworkLost
import kotlin.concurrent.thread

@AndroidEntryPoint
class ListFragment: RainbowCakeFragment<ListViewState, ListViewModel>(), ArticleAdapter.Listener {

    //region sallang

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //endregion

    //region network callback

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            viewModel.networkLost()
        }
    }

    override fun onResume() {
        super.onResume()
        registerNetworkCallback(requireContext(), networkCallback)
    }

    override fun onPause() {
        super.onPause()
        unregisterNetworkCallback(requireContext(), networkCallback)
    }

    //endregion

    private var articleAdapter: ArticleAdapter = ArticleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.load()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.lastDay -> {
                    viewModel.setPeriod(Period.LAST_DAY)
                    viewModel.load()
                }
                R.id.lastWeek -> {
                    viewModel.setPeriod(Period.LAST_7_DAYS)
                    viewModel.load()
                }
                R.id.lastMonth -> {
                    viewModel.setPeriod(Period.LAST_30_DAYS)
                    viewModel.load()
                }
                else -> {}
            }
            true
        }

    }

    private fun setupRecyclerView() {
        binding.listArticle.adapter = articleAdapter
        binding.listArticle.layoutManager = LinearLayoutManager(this.context)
        articleAdapter.setListener(this)
    }

    override fun render(viewState: ListViewState) {
        if (viewState.isInitial){
            viewModel.load()
        }
        else{
            articleAdapter.submitList(viewState.articles)
            binding.progressBarCyclic.visibility = if(viewState.isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when(event) {
            is NetworkLost ->{
                Snackbar.make(
                    binding.root,
                    getString(R.string.network_lost),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.ok)) {
                }.show()

            }
            is FailedToLoad ->{
                Toast.makeText(requireContext(), getString(R.string.failed_to_refresh_articles), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onArticleClicked(article: Article) {
        navigator?.add(DetailFragment.newInstance(article.id))
    }
}