package com.example.culinarycompass


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.culinarycompass.Viewmodels.HomeViewModel
import com.example.culinarycompass.data.Hit
import com.example.culinarycompass.data.Recipe
import com.example.culinarycompass.ui.theme.EdesOrdoTheme

class HomeActivity : ComponentActivity() {
    private val viewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getrecipes("pizza")
        setContent {

            EdesOrdoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Search()
                    displayList(viewModel = viewModel)

                    }
                }
            }
        }
        //viewModel.getrecipes()
    }


}

@Composable
fun displayList(viewModel: HomeViewModel) {
var data : State<MutableList<Hit>?> = viewModel.recepies.observeAsState()
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2), // 2 columns
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(data.value?.size?:0) { recepi ->
            // Composable item for each item in the list
            data.value?.get(recepi)?.recipe?.let { itemView(recipe = it) }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Search() {
    var text by rememberSaveable { mutableStateOf("") }
    Row (verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly){


        SearchBar(
            modifier = Modifier,
            query = text,
            onQueryChange = { text = it },
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text("search recipies") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { text = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
        ) {}
        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(25.dp))
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun itemView(recipe: Recipe) {
    Box(modifier = Modifier
        .padding(4.dp)
        .clip(RoundedCornerShape(16.dp))) {

GlideImage(model = recipe.image,
    contentDescription ="",
    loading = placeholder( R.drawable.placeholder),
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
        }){
       it

}
        Text(text = recipe.label, modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(4.dp, 0.dp, 0.dp, 20.dp), color = Color.White, fontSize = 18.sp)

    }

}