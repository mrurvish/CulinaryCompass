package com.example.edesordo


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.edesordo.Viewmodels.HomeViewModel
import com.example.edesordo.data.Hit
import com.example.edesordo.data.Recipe
import com.example.edesordo.ui.theme.EdesOrdoTheme

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


                    displayList(viewModel = viewModel)
                    Button(onClick = {viewModel.getrecipes("pastas")}) {
                    }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun itemView(recipe: Recipe){
    Column {
        Text(text = recipe.label)
GlideImage(model = recipe.image,
    contentDescription ="",
    contentScale = ContentScale.FillBounds,
    modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp))

        Text(text = recipe.source.substringAfter("."))
    }
}