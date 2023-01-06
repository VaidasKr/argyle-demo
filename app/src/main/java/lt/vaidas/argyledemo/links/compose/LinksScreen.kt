package lt.vaidas.argyledemo.links.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lt.vaidas.argyledemo.R
import lt.vaidas.argyledemo.links.ItemResult
import lt.vaidas.argyledemo.links.LinkLoadError
import lt.vaidas.argyledemo.links.LinksState
import lt.vaidas.argyledemo.links.LinksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LinksScreen() {
    val viewModel = koinViewModel<LinksViewModel>()
    val linkState by viewModel.state.collectAsState()
    LinksContent(state = linkState, onSearch = viewModel::onQueryChange, onRetry = viewModel::onRetry)
}

@Composable
private fun LinksContent(
    state: LinksState,
    onSearch: (String) -> Unit,
    onRetry: (LinkLoadError) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LinkItemList(Modifier.fillMaxSize(), state, onSearch)
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
        ErrorSnackbar(Modifier.align(Alignment.BottomCenter), state.loadError, onRetry)
    }
}

@Composable
private fun LinkItemList(
    modifier: Modifier,
    state: LinksState,
    onSearch: (String) -> Unit,
) {
    Column(modifier = modifier) {
        var searchText by remember { mutableStateOf("") }
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = searchText,
            hint = stringResource(id = R.string.links_search_hint),
            onTextChanged = { newInput ->
                searchText = newInput
                onSearch(newInput)
            }
        )
        val listState = rememberLazyListState()
        when (state.itemResult) {
            is ItemResult.Empty -> {
                NoLinksPlaceholder(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    message = state.itemResult.message
                )
            }
            is ItemResult.Items -> {
                LinkItemList(listState = listState) {
                    items(state.itemResult.items) { linkItem -> LinkRow(link = linkItem) }
                }
            }
            is ItemResult.Placeholder -> {
                LinkItemList(listState = listState) {
                    repeat(state.itemResult.size) { item { PlaceholderItem() } }
                }
            }
        }
    }
}

@Composable
private fun LinkItemList(listState: LazyListState, content: LazyListScope.() -> Unit) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
private fun NoLinksPlaceholder(modifier: Modifier, message: String) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Icon(
            modifier = Modifier.size(48.dp),
            painter = painterResource(id = R.drawable.ic_twotone_sentiment_dissatisfied),
            contentDescription = ""
        )
        Text(modifier = Modifier.padding(top = 8.dp), text = message)
    }
}

@Composable
private fun ErrorSnackbar(
    modifier: Modifier,
    loadError: LinkLoadError?,
    onRetry: (LinkLoadError) -> Unit
) {
    val snackbarState: SnackbarHostState = remember { SnackbarHostState() }
    val actionLabel = stringResource(id = R.string.links_label_retry)
    LaunchedEffect(key1 = loadError) {
        if (loadError != null) {
            val result = snackbarState.showSnackbar(
                message = loadError.message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Indefinite
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRetry(loadError)
            }
        }
    }
    SnackbarHost(modifier = modifier, hostState = snackbarState)
}

