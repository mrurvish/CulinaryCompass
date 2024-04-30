package com.example.culinarycompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.ViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.placeholder
import com.example.culinarycompass.Viewmodels.RecipeViewModel
import com.example.culinarycompass.data.Recipe
import com.example.culinarycompass.ui.theme.CulinaryCompassTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class RecipeActivity : ComponentActivity() {
    val viewModel by viewModels<RecipeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CulinaryCompassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.recepi = intent.getSerializableExtra("recipe") as Recipe?
                    MyRecipe(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalFoundationApi::class
)

@Composable
fun MyRecipe(viewModel: RecipeViewModel) {

    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(text = "Recipe Detail...")
            }, navigationIcon = {
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Menu"
                    )
                }
            })
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())

            ) {

                Box(
                        modifier = Modifier.height(500.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        ) {

                    GlideImage(model = viewModel.recepi?.image,
                        contentDescription = "",
                        loading = placeholder(R.drawable.placeholder),
                        failure = placeholder(R.drawable.placeholder),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(500.dp)
                            .fillMaxWidth()
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
                            }) { it }

                    viewModel.recepi?.label?.let { it1 ->
                        Text(
                            text = it1,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(4.dp, 0.dp, 0.dp, 20.dp),
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }

                    Spacer(modifier = Modifier.size(30.dp))
                val hello = listOf("asdgfSG","asdgfasdfg","sdrfg","sdfg","adfg")
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    viewModel.recepi?.ingredientLines?.forEach { item ->
                        Text(text = "* "+item, modifier = Modifier.fillMaxWidth().padding(4.dp))
                    }
                }

                val pagerState = rememberPagerState(pageCount = {viewModel.recepi?.ingredients?.size ?:0})
                Spacer(modifier = Modifier.size(30.dp))
                HorizontalPager(state = pagerState,  contentPadding = PaddingValues(horizontal = 40.dp), pageSpacing = 10.dp ) { page ->
                    Card(
                        Modifier

                            .graphicsLayer {
                                val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState
                                            .currentPageOffsetFraction
                                        ).absoluteValue
                                // We animate the alpha, between 50% and 100%
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        Box {
                            GlideImage(
                                model = viewModel.recepi?.ingredients?.get(page)?.image, contentDescription = "",
                                loading = placeholder(R.drawable.placeholder),
                                failure = placeholder(R.drawable.placeholder),
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
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
                                    }
                                
                            )
                            Text(text = viewModel.recepi?.ingredients?.get(page)?.food?:"", modifier = Modifier.align(Alignment.BottomCenter), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
