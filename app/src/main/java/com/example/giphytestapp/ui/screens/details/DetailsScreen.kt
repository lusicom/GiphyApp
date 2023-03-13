package com.example.giphytestapp.ui.screens.details

import android.content.Context
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.giphytestapp.R
import com.example.giphytestapp.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
@Composable
fun DetailsScreen(
    context: Context,
    navController: NavController,
    images: List<Any>,
    page: Int,
    onContentChange: (String, String) -> Unit,
    onShortChange: (String, String) -> Unit
) {
    val pagerState = rememberPagerState()

    HorizontalPager(count = images.size, state = pagerState) { page ->
        val image = images[page]


        val imageLoader = ImageLoader.Builder(context)
            .components(ComponentRegistry.Builder().apply {
                if (Build.VERSION.SDK_INT >= 28) {
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

    LaunchedEffect(page) {
        pagerState.scrollToPage(page)
    }
}
