package com.android.flickr.exercise.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.flickr.exercise.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { FlickrPhotoListAdapter() }

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.search_btn)
        searchEditText = findViewById(R.id.search_editText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayout.VERTICAL)
        )

        searchButton.setOnClickListener {
            if (searchEditText.text.isNotEmpty()) {
                val searchQuery = searchEditText.text.toString()
                viewModel.fetchFlickrPhotoData(searchQuery)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                handleUiState(state)
            }
        }
    }

    private fun handleUiState(state: MainViewModel.State) {
        when (state) {
            is MainViewModel.State.Empty -> onEmpty()
            is MainViewModel.State.Result.Success -> onSuccess(state)
            is MainViewModel.State.Result.Error -> onError(state)
        }
    }

    private fun onEmpty() {
        // Do nothing
    }

    private fun onSuccess(success: MainViewModel.State.Result.Success) {
        adapter.submitList(success.data)
    }

    private fun onError(error: MainViewModel.State.Result.Error) {
        Toast.makeText(this, error.error, Toast.LENGTH_SHORT).show()
    }
}
