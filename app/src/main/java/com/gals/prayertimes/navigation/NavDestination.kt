package com.gals.prayertimes.navigation

interface NavDestination {
    val route: String
}

object Home : NavDestination {
    override val route: String = "home"
}

object Menu : NavDestination {
    override val route: String = "settingsMenu"
}

object PrivacyPolicy: NavDestination{
    override val route: String = "privacyPolicy"
}

object Notification: NavDestination{
    override val route: String = "notificationMenu"
}