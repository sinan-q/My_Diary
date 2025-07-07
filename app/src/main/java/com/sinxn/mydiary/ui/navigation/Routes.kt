package com.sinxn.mydiary.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("diary_list_screen")
    object Calender : Screen("calender_screen")
    object DiaryView {
        object AddDiary : Screen("diary_view_screen/add_diary")
        object ById : Screen("diary_view_screen/id={id}") {
            fun createRoute(id: Long) = "diary_view_screen/id=$id"
        }
        object ByDate : Screen("diary_view_screen/date={timestamp}") {
            fun createRoute(timestamp: Long) = "diary_view_screen/date=$timestamp"
        }
    }
    object Backup : Screen("backup_screen")
}