package hu.bme.aut.nytimes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.hilt.getViewModelFromFactory
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.nytimes.R
import hu.bme.aut.nytimes.databinding.FragmentListBinding
import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.ui.theme.SecondaryText
import hu.bme.aut.nytimes.util.Period
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.skydoves.landscapist.glide.GlideImage
import hu.bme.aut.nytimes.ui.theme.NYTimesTheme

@AndroidEntryPoint
class ListFragment: RainbowCakeFragment<ListViewState, ListViewModel>() {

    //region sallang

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_list

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFromNetwork(Period.LAST_DAY)
    }

    override fun render(viewState: ListViewState) {
        binding.composeView.setContent {
            when(viewState){
                is Loading -> {
                    ListScreen(isLoading = true, articles = mutableListOf(), viewModel = viewModel)
                }
                is ListLoadedFromNetwork ->{
                    ListScreen(isLoading = false, articles = viewState.articles, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ListScreen(isLoading: Boolean, articles: List<Article>, viewModel: ListViewModel){
    NYTimesTheme(darkTheme = false) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(R.string.ny_times_most_popular))
                        },
                        actions = {
                            PeriodSelectAppBarDropdownMenu(viewModel = viewModel)
                        })
                }) {
                LazyColumn{
                    items(articles){ article ->
                        ArticleItem(article)
                    }
                }
            }

        }
    }

}




@Preview(showBackground = true)
@Composable
fun PreviewArticleItem(){
    ArticleItem(Article(
        id = "asdadsadasdsadadasdas",
        title = "asdakkkkkkkkkkkkkkkklna as da nsd assssssssssnpas nd nasnk as das as a",
        byLine = "by meee",
        dateString = "1999-12-01",
        smallImgUrl = "",
        mediumImgUrl = "",
        largeImgUrl = ""
    ))
}

@Composable
fun ArticleItem(article: Article) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight()
    ) {
        GlideImage(
            imageModel  = article.smallImgUrl,
            contentDescription = "",
            modifier =
            Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align(alignment = CenterVertically)
                .padding(4.dp)

        )
        Column(
            modifier =
            Modifier
                .wrapContentHeight()
        ) {

            Text(
                article.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier =
                Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                article.byLine,
                color = SecondaryText,
            )

            DateWithIcon(dateString = article.dateString)
            
        }
    }
}

@Composable
fun DateWithIcon(dateString: String){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(dateString, color = SecondaryText)
        Icon(Icons.Filled.Event,
            "date",
            tint = Color.Gray
        )

    }
}

@Composable
fun PeriodSelectAppBarDropdownMenu(viewModel: ListViewModel) {
    val expanded = remember { mutableStateOf(false) } // 1

    Box(
        Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = {
            expanded.value = true
        }) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "More Menu"
            )
        }
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        DropdownMenuItem(onClick = {
            expanded.value = false
            viewModel.loadFromNetwork(Period.LAST_DAY)
        }) {
            Text("Last day")
        }

        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            viewModel.loadFromNetwork(Period.LAST_7_DAYS)
        }) {
            Text("Last week")
        }

        Divider()

        DropdownMenuItem(onClick = {
            expanded.value = false
            viewModel.loadFromNetwork(Period.LAST_30_DAYS)
        }) {
            Text("Last month")
        }

    }
}