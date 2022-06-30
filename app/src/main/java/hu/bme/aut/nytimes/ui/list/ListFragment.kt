package hu.bme.aut.nytimes.ui.list

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import hu.bme.aut.nytimes.ui.detail.Initial
import hu.bme.aut.nytimes.util.FinishLoading
import hu.bme.aut.nytimes.util.StartLoading

@AndroidEntryPoint
class ListFragment: RainbowCakeFragment<ListViewState, ListViewModel>() {

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

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.networkAvailable()
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            viewModel.networkLost()
        }
    }

    private var articleAdapter: ArticleAdapter = ArticleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load(Period.LAST_DAY)
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.load(Period.LAST_DAY)
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun setupRecyclerView() {
        binding.listArticle.adapter = articleAdapter
        binding.listArticle.layoutManager = LinearLayoutManager(this.context)

    }

    override fun render(viewState: ListViewState) {
        articleAdapter.submitList(viewState.articles)
    }

    override fun onEvent(event: OneShotEvent) {
        when(event) {
            is StartLoading ->{
                binding.progressBarCyclic.visibility = View.VISIBLE
            }
            is FinishLoading ->{
                binding.progressBarCyclic.visibility = View.GONE
            }
        }
    }
}