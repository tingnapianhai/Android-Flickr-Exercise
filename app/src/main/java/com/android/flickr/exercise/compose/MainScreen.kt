package com.android.flickr.exercise.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.flickr.exercise.R
import com.android.flickr.exercise.data.FlickrPhotoItem
import com.android.flickr.exercise.ui.MainViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchBarContent(
                viewModel = viewModel,
                context = context,
            )

            SearchResultContent(
                state = state,
                context = context,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarContent(
    viewModel: MainViewModel,
    context: Context,
) {
    var searchText by remember { mutableStateOf(context.getString(R.string.initial_text)) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(text = context.getString(R.string.search)) },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Button(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp),
            onClick = {
                if (searchText.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Text field is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.fetchFlickrPhotoData(searchText)
                }
            }
        ) {
            Text(text = context.getString(R.string.search))
        }
    }
}

@Composable
fun SearchResultContent(
    state: MainViewModel.State,
    context: Context,
) {
    when (state) {
        is MainViewModel.State.Result.Success -> {
            LazyColumn {
                val flickrPhotoList = state.data
                items(flickrPhotoList) { itemData ->
                    ImageItemView(itemData = itemData)
                }
            }
        }

        is MainViewModel.State.Result.Error -> {
            Toast.makeText(
                context,
                state.error,
                Toast.LENGTH_SHORT
            ).show()
        }

        is MainViewModel.State.Loading -> {
            ProgressView()
        }

        is MainViewModel.State.Empty -> {
            Text(
                text = stringResource(R.string.no_search_requests),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ProgressView(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItemView(
    itemData: FlickrPhotoItem
) {
    Row {
        GlideImage(
            modifier = Modifier
                .heightIn(min = 100.dp, max = 200.dp)
                .widthIn(min = 100.dp, max = 200.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.CenterVertically),
            model = itemData.url,
            contentDescription = itemData.title,
            contentScale = ContentScale.Fit,
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp),
            text = itemData.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) = Canvas(
    modifier
        .fillMaxWidth()
        .height(thickness)
) {
    drawLine(
        color = color,
        strokeWidth = thickness.toPx(),
        start = Offset(0f, thickness.toPx() / 2),
        end = Offset(size.width, thickness.toPx() / 2),
    )
}
