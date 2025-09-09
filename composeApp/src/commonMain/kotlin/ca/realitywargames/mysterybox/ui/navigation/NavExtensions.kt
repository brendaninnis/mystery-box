package ca.realitywargames.mysterybox.ui.navigation

import androidx.navigation.NavController

/**
 * Pops the back stack only if there is a previous entry.
 * Returns true if a destination was popped, false otherwise.
 */
fun NavController.safePopBackStack(): Boolean {
    return if (previousBackStackEntry != null) {
        popBackStack()
    } else {
        false
    }
}


