package ca.realitywargames.mysterybox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.realitywargames.mysterybox.ui.navigation.NavRoutes
import ca.realitywargames.mysterybox.ui.screens.mysteries.MysteriesScreen
import ca.realitywargames.mysterybox.ui.screens.parties.PartiesScreen
import ca.realitywargames.mysterybox.ui.screens.profile.ProfileScreen
import ca.realitywargames.mysterybox.ui.theme.MysteryGradient
import ca.realitywargames.mysterybox.ui.viewmodel.MysteryListViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.PartyViewModel
import ca.realitywargames.mysterybox.ui.viewmodel.UserViewModel

data class BottomNavItem(
    val route: String,
    val icon: @Composable () -> Unit,
    val label: String
)

enum class Tab {
    MYSTERIES, PARTIES, PROFILE
}

val TabSaver = Saver<Tab, String>(
    save = { it.name },
    restore = { Tab.valueOf(it) }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    var selectedTab by rememberSaveable(stateSaver = TabSaver) { mutableStateOf(Tab.MYSTERIES) }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "mysteries",
            icon = { Icon(Icons.Default.Search, contentDescription = "Mysteries") },
            label = "Mysteries"
        ),
        BottomNavItem(
            route = "parties",
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Parties") },
            label = "Parties"
        ),
        BottomNavItem(
            route = "profile",
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = "Profile"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mystery Box",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavRoutes.SETTINGS)
                    }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (isLoggedIn) {
                        IconButton(onClick = {
                            userViewModel.logout()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            navController.navigate(NavRoutes.LOGIN)
                        }) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Login",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    val tab = when (index) {
                        0 -> Tab.MYSTERIES
                        1 -> Tab.PARTIES
                        2 -> Tab.PROFILE
                        else -> Tab.MYSTERIES
                    }

                    NavigationBarItem(
                        icon = {
                            item.icon()
                        },
                        label = {
                            Text(
                                item.label,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        selected = selectedTab == tab,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = {
                            selectedTab = tab
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MysteryGradient)
        ) {
            when (selectedTab) {
                Tab.MYSTERIES -> MysteriesScreen(
                    navController = navController,
                    viewModel = viewModel { MysteryListViewModel() }
                )
                Tab.PARTIES -> PartiesScreen(
                    navController = navController,
                    viewModel = viewModel { PartyViewModel() }
                )
                Tab.PROFILE -> ProfileScreen(
                    userViewModel = userViewModel,
                    navController = navController
                )
            }
        }

    }
}
