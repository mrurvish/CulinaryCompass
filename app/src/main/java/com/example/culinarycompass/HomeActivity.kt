package com.example.culinarycompass


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.culinarycompass.Viewmodels.HomeViewModel
import com.example.culinarycompass.data.Recipe
import com.example.culinarycompass.ui.theme.EdesOrdoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getrecipes()
        setContent {

            EdesOrdoTheme {


                    Bottomsheet(viewModel)


            }
        }
        //viewModel.getrecipes()
    }

}

@Composable
fun MyApp(viewModel: HomeViewModel,onFilter: () -> Unit) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Search(viewModel = viewModel, { viewModel.getrecipes() }, onFilter = { onFilter() })
            displayList(viewModel = viewModel)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bottomsheet(viewModel: HomeViewModel) {
    val bottomSheetState = rememberBottomSheetScaffoldState(bottomSheetState = SheetState(initialValue = SheetValue.Hidden, skipPartiallyExpanded = true))
    val scop = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetContent = {
        BottomsheetContent {
            scop.launch {
                bottomSheetState.bottomSheetState.hide()
            }
        }
    }, sheetPeekHeight = 0.dp) {
        MyApp(viewModel, onFilter = {
            scop.launch {
                bottomSheetState.bottomSheetState.expand()
            }
        })
    }

}

@Composable
fun BottomsheetContent(OnDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        Icon(
            Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .size(25.dp)
                .clickable { OnDismiss() }
                .align(Alignment.TopEnd))
        Text(text = "hello bottom sheet", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun displayList(viewModel: HomeViewModel) {

    val data = viewModel.recepiResponse.collectAsLazyPagingItems()
//    Log.d("size", data?.size.toString())
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // 2 columns
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(data?.itemCount ?: 0) { recepi ->
            // Composable item for each item in the list
//            data?.getOrNull(recepi)?.recipe?.let { itemView(recipe = it) }
            data[recepi]?.recipe?.let { itemView(recipe = it) }
        }

        data.apply {
            when {
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )

                        }
                    }
                }

                loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                    item {
                        Text(text = "Error")
                    }
                }

                loadState.refresh is LoadState.NotLoading -> {
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(viewModel: HomeViewModel, onSearch: () -> Unit, onFilter: () -> Unit) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        SearchBar(
            modifier = Modifier,
            query = viewModel.searchtext,
            onQueryChange = {
                viewModel.searchtext = it
            },
            onSearch = {
                onSearch()
            },
            active = false,
            onActiveChange = {},
            placeholder = { Text("search recipies") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (viewModel.searchtext.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchtext = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
        ) {}
        Icon(
            Icons.Default.FilterList,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .clickable { onFilter() })
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun itemView(recipe: Recipe) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {

        GlideImage(model = recipe.image,
            contentDescription = "",
            loading = placeholder(R.drawable.placeholder),
            failure = placeholder(R.drawable.placeholder),
            contentScale = ContentScale.FillBounds,

            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = size.height / 3,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.Multiply)
                    }
                }) {
            it

        }
        Text(
            text = recipe.label, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp, 0.dp, 0.dp, 20.dp), color = Color.White, fontSize = 18.sp
        )

    }

}
