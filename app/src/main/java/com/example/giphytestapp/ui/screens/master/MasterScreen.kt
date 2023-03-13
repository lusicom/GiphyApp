package com.example.giphytestapp.ui.screens.master

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.*
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.giphytestapp.R
import com.example.giphytestapp.navigation.Screen
import com.example.giphytestapp.ui.ScreenState
import com.example.giphytestapp.ui.SharedViewModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun MasterScreen(
    context: Context,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {

    val scrollState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val state = sharedViewModel.state
    val searchWidgetState by sharedViewModel.searchWidgetState
    val searchTextState by sharedViewModel.searchTextState

    Scaffold(
        topBar = {
            MainAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    sharedViewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    sharedViewModel.updateSearchTextState(newValue = "")
                    sharedViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    sharedViewModel.searchGifs(it)
                    sharedViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    coroutineScope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                onSearchTriggered = {
                    sharedViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        },
        content = {
            if (searchTextState == "") {
                ShowText()
            } else if (state.items.isEmpty() && searchWidgetState == SearchWidgetState.CLOSED) {
                ShowErrorText()
            } else {
                GifsGrid(scrollState, sharedViewModel, state = state, context = context, navController)
            }
        }
    )
}


@Composable
fun Pulsating(pulseFraction: Float = 1.2f, content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .padding(horizontal = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ShowText() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Pulsating {
            Text(
                text = "Use SearchBar to look for the gif",
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ShowErrorText() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Pulsating {
            Text(
                text = "There are no results available for your request. \n Try again",
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif,
                maxLines = 2,
            )
        }
    }
}


@Composable
fun GifsGrid(
    scrollState: LazyGridState,
    sharedViewModel: SharedViewModel,
    state: ScreenState,
    context: Context,
    navController: NavController
) {
    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Adaptive(180.dp),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(state.items.size) { i ->
            val item = state.items[i]
            if (i >= state.items.size - 2 && !state.isLoading) {
                sharedViewModel.loadNextItems()
            }
            GifCard(item.images.downsized.url, context, navController)
        }
    }
}

@Composable
fun GifCard(
    gifUrl: String,
    context: Context,
    navController: NavController
) {
    val imageLoader = ImageLoader.Builder(context)
        .components(ComponentRegistry.Builder().apply {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build())
        .build()

    Card(shape = MaterialTheme.shapes.medium) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .wrapContentSize(Alignment.Center)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(gifUrl)
                    .apply(block = {
                        size(Size.ORIGINAL)
                    })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                imageLoader = imageLoader,
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                               navController.navigate(route = Screen.Details.route)
                    },
                placeholder = rememberAsyncImagePainter(
                    model = R.drawable.square_placeholder,
                    imageLoader = imageLoader,
                    filterQuality = FilterQuality.Low,
                ),
            )
        }
    }
}
