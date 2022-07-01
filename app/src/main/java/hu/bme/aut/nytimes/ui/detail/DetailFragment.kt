package hu.bme.aut.nytimes.ui.detail

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.nytimes.R
import hu.bme.aut.nytimes.databinding.FragmentDetailBinding
import hu.bme.aut.nytimes.util.NetworkCallbackHelper


@AndroidEntryPoint
class DetailFragment : RainbowCakeFragment<DetailViewState, DetailViewModel>() {

    //region sallang

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.networkAvailable()
        }
    }

    override fun onResume() {
        super.onResume()
        NetworkCallbackHelper.registerNetworkCallback(requireContext(), networkCallback)
    }

    override fun onPause() {
        super.onPause()
        NetworkCallbackHelper.unregisterNetworkCallback(requireContext(), networkCallback)
    }

    //endregion

    companion object {

        const val TAG = "DetailFragment"

        private const val KEY_ARTICLE_ID = "KEY_ARTICLE"

        fun newInstance(articleId: String): DetailFragment {
            val args = Bundle()
            args.putString(KEY_ARTICLE_ID, articleId)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }

    }



    private object Flipper {
        const val LOADING = 0
        const val LOADED = 1
        const val ERROR = 2
    }

    override fun render(viewState: DetailViewState) {
        if(viewState.isInitial){
            viewModel.loadArticle(arguments?.getString(KEY_ARTICLE_ID).toString())
        }
        else if(viewState.isError){
            binding.viewFlipper.displayedChild = Flipper.ERROR
            binding.layoutDetailError.btnRetry.setOnClickListener {
                viewModel.loadArticle(arguments?.getString(KEY_ARTICLE_ID).toString())
            }
        }
        else if(viewState.isLoading){
            binding.viewFlipper.displayedChild = Flipper.LOADING
        }
        else if(viewState.article != null){
            binding.viewFlipper.displayedChild = Flipper.LOADED
            val article = viewState.article
            Glide
                .with(requireContext())
                .load(article?.imgUrl).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.outline_feed_24)
                .into(binding.layoutDetailLoaded.ivArticle)
            binding.layoutDetailLoaded.tvTitle.text = article?.title
            binding.layoutDetailLoaded.tvByLine.text = article?.byLine
            binding.layoutDetailLoaded.layoutDate.tvDate.text = article?.dateString
            binding.layoutDetailLoaded.btnBrowser.isEnabled = !viewState.isNetworkLost
            binding.layoutDetailLoaded.btnBrowser.setOnClickListener {
                openBrowser(article?.url.toString())
            }
        }

    }

    private fun openBrowser(url: String) {
        val customIntent = CustomTabsIntent.Builder()

        customIntent.setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setNavigationBarColor(resources.getColor(R.color.red, null))
                .setToolbarColor(resources.getColor(R.color.red, null))
                .setSecondaryToolbarColor(resources.getColor(R.color.red, null))
                .build()
            )

        openCustomTab(requireActivity(), customIntent.build(), Uri.parse(url))
    }

    private fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
        val packageName = "com.android.chrome"
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(activity, uri!!)
    }
}