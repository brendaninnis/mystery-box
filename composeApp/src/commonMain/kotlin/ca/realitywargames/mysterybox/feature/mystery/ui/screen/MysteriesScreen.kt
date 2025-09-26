package ca.realitywargames.mysterybox.feature.mystery.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.core.data.network.serverBaseUrl
import ca.realitywargames.mysterybox.shared.models.MysteryPackage
import ca.realitywargames.mysterybox.feature.mystery.ui.component.MysteryInfoRow
import ca.realitywargames.mysterybox.core.ui.component.NetworkImage
import ca.realitywargames.mysterybox.feature.mystery.navigation.MysteryDetailRoute
import ca.realitywargames.mysterybox.core.data.state.UiState
import ca.realitywargames.mysterybox.feature.mystery.presentation.viewmodel.MysteryListViewModel
import ca.realitywargames.mysterybox.preview.MockData
import ca.realitywargames.mysterybox.core.ui.theme.MysteryBoxTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MysteriesScreen(
    navController: NavHostController,
    viewModel: MysteryListViewModel
) {
    val state by viewModel.mysteries.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    MysteriesScreenContent(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshMysteries() },
        onRetry = { viewModel.loadMysteries() },
        onMysteryClick = { mysteryPackage ->
            navController.navigate(MysteryDetailRoute(mysteryPackage.id))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MysteriesScreenContent(
    state: UiState<List<MysteryPackage>>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onMysteryClick: (MysteryPackage) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        when (val s = state) {
            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> if (s.data.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No mystery packages available",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Check back later for new mysteries!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(s.data) { mysteryPackage ->
                        MysteryPackageCard(
                            mysteryPackage = mysteryPackage,
                            onClick = { onMysteryClick(mysteryPackage) }
                        )
                    }
                }
            }
            is UiState.Error -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = "Oops! Something went wrong",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    item {
                        Text(
                            text = "We couldn't load the mystery packages right now.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    item {
                        Text(
                            text = "Please check your internet connection and try again.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    item {
                        Button(
                            onClick = onRetry,
                            modifier = Modifier.padding(top = 24.dp)
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MysteryPackageCard(
    mysteryPackage: MysteryPackage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Mystery image
            NetworkImage(
                url = "$serverBaseUrl${mysteryPackage.imagePath}",
                contentDescription = "Image for ${mysteryPackage.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = mysteryPackage.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mysteryPackage.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                MysteryInfoRow(mysteryPackage = mysteryPackage)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${mysteryPackage.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Button(onClick = onClick) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MysteriesScreenLoadingPreview() {
    MysteryBoxTheme {
        MysteriesScreenContent(
            state = UiState.Loading,
            isRefreshing = false,
            onRefresh = { },
            onRetry = { },
            onMysteryClick = { }
        )
    }
}

@Preview
@Composable
private fun MysteriesScreenSuccessPreview() {
    MysteryBoxTheme {
        MysteriesScreenContent(
            state = UiState.Success(MockData.sampleMysteryPackages()),
            isRefreshing = false,
            onRefresh = { },
            onRetry = { },
            onMysteryClick = { }
        )
    }
}

@Preview
@Composable
private fun MysteriesScreenEmptyPreview() {
    MysteryBoxTheme {
        MysteriesScreenContent(
            state = UiState.Success(emptyList()),
            isRefreshing = false,
            onRefresh = { },
            onRetry = { },
            onMysteryClick = { }
        )
    }
}

@Preview
@Composable  
private fun MysteriesScreenErrorPreview() {
    MysteryBoxTheme {
        MysteriesScreenContent(
            state = UiState.Error("Failed to load mystery packages"),
            isRefreshing = false,
            onRefresh = { },
            onRetry = { },
            onMysteryClick = { }
        )
    }
}

@Preview
@Composable
private fun MysteriesScreenRefreshingPreview() {
    MysteryBoxTheme {
        MysteriesScreenContent(
            state = UiState.Success(MockData.sampleMysteryPackages()),
            isRefreshing = true,
            onRefresh = { },
            onRetry = { },
            onMysteryClick = { }
        )
    }
}

@Preview
@Composable
private fun MysteryPackageCardPreview() {
    MysteryBoxTheme {
        MysteryPackageCard(
            mysteryPackage = MockData.sampleMysteryPackage(),
            onClick = { }
        )
    }
}
