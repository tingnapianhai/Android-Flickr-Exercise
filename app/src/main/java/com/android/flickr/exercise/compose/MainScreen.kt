package com.android.flickr.exercise.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.flickr.exercise.R
import com.android.flickr.exercise.data.FlickrPhotoItem
import com.android.flickr.exercise.ui.MainViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("plane") }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(text = context.getString(R.string.search)) },
                    // align text field to the start of the Button
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
                Button(
                    // align button to the end of the row
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(0.dp),
                    onClick = { viewModel.fetchFlickrPhotoData(searchText) }
                ) {
                    Text(text = context.getString(R.string.search))
                }
            }
            LazyColumn {
                when (state) {
                    is MainViewModel.State.Result.Success -> {
                        val flickrPhotoList = (state as MainViewModel.State.Result.Success).data
                        items(flickrPhotoList) { itemData ->
                            PhotoItem(itemData = itemData)
                            // show a divider with 1dp height
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            ) { }
                        }
                    }

                    is MainViewModel.State.Result.Error -> {
                        //show Toast with error message
                        Toast.makeText(
                            context,
                            (state as MainViewModel.State.Result.Error).error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    MainViewModel.State.Empty -> {
                        // do nothing
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItem(
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
                .padding(start = 0.dp, end = 16.dp),
            text = itemData.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}