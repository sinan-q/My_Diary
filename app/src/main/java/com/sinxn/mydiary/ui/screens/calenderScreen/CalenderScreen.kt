package com.sinxn.mydiary.ui.screens.calenderScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sinxn.mydiary.ui.components.BottomBar
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.screens.homeScreen.HomeViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class) // Required for Pager
@Composable
fun CalenderScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit
) {
    val diaries by homeViewModel.diaries.collectAsState()

    // Define a very large range for "infinite" swiping.
    // Pager works with indices. We'll map these indices to YearMonth.
    val initialMonth = YearMonth.now()
    val pageCount = Int.MAX_VALUE // Effectively "infinite"
    val initialPage = pageCount / 2 // Start in the middle
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { pageCount }
    )

    // State to hold the YearMonth derived from the current pager page
    var currentDisplayMonth by remember { mutableStateOf(initialMonth) }

    // Update currentDisplayMonth when the pager's currentPage changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .map { pageIndex ->
                // Calculate month based on page index relative to the initial month
                val monthOffset = pageIndex - initialPage
                initialMonth.plusMonths(monthOffset.toLong())
            }
            .distinctUntilChanged()
            .collect { month ->
                currentDisplayMonth = month
                // Optional: You might want to trigger data loading for this new month here
                // homeViewModel.loadDiariesForMonth(month)
            }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        bottomBar = { BottomBar(navController = navController) },
        topBar = {
            MyTopAppBar(navController = navController)
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Optional: Month/Year Header that reacts to pager changes or controls pager
            MonthYearHeader(
                currentMonth = currentDisplayMonth,
                onPreviousMonth = {
                    // Animate to previous page
                    // This requires a coroutine scope
                    // rememberCoroutineScope().launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    // For simplicity now, direct change, but animation is better
                    currentDisplayMonth = currentDisplayMonth.minusMonths(1)
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                    // You would need to calculate the target page and scroll the pager
                },
                onNextMonth = {
                    currentDisplayMonth = currentDisplayMonth.plusMonths(1)
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    // You would need to calculate the target page and scroll the pager
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f) // Ensure Pager takes available space
            ) { pageIndex ->
                // Calculate the YearMonth for the current page
                val monthOffset = pageIndex - initialPage
                val pageMonth = initialMonth.plusMonths(monthOffset.toLong())

                // Pass this specific month to your CalendarGrid
                // CalendarGrid will now be responsible for rendering only ONE month
                CalendarGrid(
                    diaries = diaries.filter {
                        // Filter diaries for the specific month being displayed by this pager page
                        val diaryDate = it.date
                        YearMonth.from(diaryDate) == pageMonth
                    },
                    displayMonth = pageMonth, // Pass the month this grid should display
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun MonthYearHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround // Or Center
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous Month")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = currentMonth.month.toString(), lineHeight = 10.sp)
            Text(text = currentMonth.year.toString(), fontSize = 10.sp, lineHeight = 10.sp)
        }
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next Month")
        }
    }
}