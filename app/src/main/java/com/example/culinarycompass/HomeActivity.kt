package com.example.culinarycompass


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.culinarycompass.Viewmodels.HomeViewModel
import com.example.culinarycompass.data.ApiParams
import com.example.culinarycompass.data.Recipe
import com.example.culinarycompass.ui.theme.EdesOrdoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    // private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            EdesOrdoTheme {


                MyApp()


            }
        }
        //viewModel.getrecipes()
    }

}

@Composable
fun MyApp(viewModel: HomeViewModel = hiltViewModel()) {
    viewModel.getrecipes()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Home") {
        composable(route = "Home") {
            HomeScreen(viewModel = viewModel)
        }
        composable(route = "Notification") {

        }
        composable(route = "Help") {

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = true
        )
    )
    val scop = rememberCoroutineScope()



    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetContent = {
            BottomsheetContent(viewModel) {
                scop.launch {
                    bottomSheetState.bottomSheetState.hide()
                }
            }
        }, sheetPeekHeight = 0.dp
    ) {
        Column {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Search(viewModel = viewModel, { viewModel.getrecipes() }, onFilter = {
                    scop.launch {
                        bottomSheetState.bottomSheetState.expand()
                    }
                })
                DisplayList(viewModel = viewModel)

            }
        }
    }
    /*  LaunchedEffect(bottomSheetState) {
          snapshotFlow { bottomSheetState.bottomSheetState.isVisible }.collect { isVisible ->
              if (isVisible) {
                  // Sheet is visible
              } else {
                  viewModel.getrecipes()
              }
          }
      }*/
}


@Composable
fun BottomsheetContent(viewModel: HomeViewModel, OnDismiss: () -> Unit = {}) {
    val selecteddiet: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }
    val selectedhealth: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }
    val selectedcusintype: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }
    val selectedmealtype: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }
    val selecteddishtype: MutableState<List<String>> = remember {
        mutableStateOf(listOf())
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .size(25.dp)
                    .clickable { OnDismiss() },
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Meal :")
        ChipGroupitemview(ApiParams().meals, onChipDisselected = {

            val oldlist = selectedmealtype.value.toMutableList()
            oldlist.remove(it)
            selectedmealtype.value = oldlist
        }, onChipSelected = {

            val oldlist = selectedmealtype.value.toMutableList()
            oldlist.add(it)
            selectedmealtype.value = oldlist
        }, selectedchips = selectedmealtype)
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Diet :")
        ChipGroupitemview(ApiParams().diet, onChipDisselected = {

            val oldlist = selecteddiet.value.toMutableList()
            oldlist.remove(it)
            selecteddiet.value = oldlist
        }, onChipSelected = {

            val oldlist = selecteddiet.value.toMutableList()
            oldlist.add(it)
            selecteddiet.value = oldlist
        }, selectedchips = selecteddiet)

        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Cuisine :")
        ChipGroupitemview(ApiParams().cuisineTypes, onChipDisselected = {

            val oldlist = selectedcusintype.value.toMutableList()
            oldlist.remove(it)
            selectedcusintype.value = oldlist
        }, onChipSelected = {

            val oldlist = selectedcusintype.value.toMutableList()
            oldlist.add(it)
            selectedcusintype.value = oldlist
        }, selectedchips = selectedcusintype)
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Dish :")
        ChipGroupitemview(ApiParams().dishTypes, onChipDisselected = {

            val oldlist = selecteddishtype.value.toMutableList()
            oldlist.remove(it)
            selecteddishtype.value = oldlist
        }, onChipSelected = {

            val oldlist = selecteddishtype.value.toMutableList()
            oldlist.add(it)
            selecteddishtype.value = oldlist
        }, selectedchips = selecteddishtype)
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Health Labels :")
        ChipGroupitemview(ApiParams().health, onChipDisselected = {

            val oldlist = selectedhealth.value.toMutableList()
            oldlist.remove(it)
            selectedhealth.value = oldlist
        }, onChipSelected = {

            val oldlist = selectedhealth.value.toMutableList()
            oldlist.add(it)
            selectedhealth.value = oldlist
        }, selectedchips = selectedhealth)
        Spacer(modifier = Modifier.size(4.dp))
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                selecteddiet.value = listOf()
                selectedcusintype.value = listOf()
                selectedmealtype.value = listOf()
                selecteddishtype.value = listOf()
                selectedhealth.value = listOf()
                viewModel.diet.clear()
                viewModel.cusintype.clear()
                viewModel.mealtype.clear()
                viewModel.health.clear()
                viewModel.dishtype.clear()
                OnDismiss()

            }) {
                Text(text = "Clear")
            }
            Button(onClick = {
                viewModel.diet = selecteddiet.value.toMutableList()
                viewModel.cusintype = selectedcusintype.value.toMutableList()
                viewModel.mealtype = selectedmealtype.value.toMutableList()
                viewModel.dishtype = selecteddishtype.value.toMutableList()
                viewModel.health = selectedhealth.value.toMutableList()
                viewModel.getrecipes()
                OnDismiss()
            }) {
                Text(text = "Apply")
            }
        }

    }
}
/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipExample() {
    var selected by remember { mutableStateOf(false) }
    ElevatedFilterChip(
        selected = selected,
        onClick = { selected = !selected },
        label = { Text("Filter chip") },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipGroupitemview(
    apiparams: List<String>, onChipSelected: (String) -> Unit,
    onChipDisselected: (String) -> Unit,
    selectedchips: MutableState<List<String>>
) {

    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Adaptive(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentPadding = PaddingValues(vertical = 1.dp, horizontal = 2.dp)
    ) {
        items(apiparams) {
            ElevatedFilterChip(
                modifier = Modifier.padding(2.dp),
                selected = selectedchips.value.contains(it),
                onClick = {
                    if (!selectedchips.value.contains(it)) {
                        onChipSelected(it)

                    } else {
                        onChipDisselected(it)
                    }
                },
                label = { Text(it, maxLines = 1) },
                leadingIcon = if (selectedchips.value.contains(it)) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Localized Description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@Composable
fun DisplayList(viewModel: HomeViewModel) {

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
                    item(span = StaggeredGridItemSpan.FullLine) {
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
                    item(span = StaggeredGridItemSpan.FullLine) {
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
    val keyboardController = LocalSoftwareKeyboardController.current

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
                keyboardController?.hide()
            },
            active = false,
            onActiveChange = {},
            placeholder = { Text("search recipes") },
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
                }) { it }
        Text(
            text = recipe.label, modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(4.dp, 0.dp, 0.dp, 20.dp), color = Color.White, fontSize = 18.sp
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun drawer() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = { Drawercontent(drawerState, scope) }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(topBar = {
                TopAppBar(title = {
                    Text(text = "My App")
                }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
            }) { padding ->
                Surface(modifier = Modifier.padding(padding)) {
                    Text(text = "hello")
                }

            }
        }
    }
}

@Composable
fun Drawercontent(drawerState: DrawerState, scope: CoroutineScope) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalDrawerSheet(modifier = Modifier.padding(end = 50.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Logo()
        }
        Spacer(modifier = Modifier.height(30.dp))
        ApiParams().items.forEachIndexed { index, drawerItem ->
            NavigationDrawerItem(label = {
                Text(text = drawerItem.title)
            }, selected = index == selectedItemIndex, onClick = {
                selectedItemIndex = index
                scope.launch {
                    drawerState.close()
                }
            }, icon = {
                Icon(
                    imageVector = if (index == selectedItemIndex) {
                        drawerItem.selectedIcon
                    } else drawerItem.unselectedIcon,
                    contentDescription = drawerItem.title
                )
            },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}