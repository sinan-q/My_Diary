package com.sinxn.mydiary.ui.screens.homeScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sinxn.mydiary.ui.components.BottomBar
import com.sinxn.mydiary.ui.components.MyTextField
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.components.RectangleCard
import com.sinxn.mydiary.ui.components.RectangleFAB
import com.sinxn.mydiary.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val diaries by homeViewModel.diaries.collectAsState()
    var search by remember { mutableStateOf("") }
    val searchResults by homeViewModel.searchResults.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val toastMessage by homeViewModel.toastMessage.collectAsState(initial = "")
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = {
            RectangleFAB(onClick = { navController.navigate(Screen.DiaryView.AddDiary.route) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Diary")
            }
        },
        topBar = {
            MyTopAppBar(navController)
        },
    ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        ) {
            RectangleCard(shape = RoundedCornerShape(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MyTextField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = "Search",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions =
                            KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    homeViewModel.searchDiaries(search)
                                }
                            )
                    )
                    IconButton(
                        onClick = { homeViewModel.searchDiaries(search) }
                    ) {
                        Icon(Icons.Default.Search, "Search button")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            if(diaries.isEmpty()) Text("No Diaries Found", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
            LazyColumn {
                items(items = diaries , key = { it.id }) { diary ->
                    AnimatedVisibility(
                        visible = searchResults.isEmpty() || searchResults.any { it.id == diary.id },
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        DiaryItem(
                            diary = diary,
                            onClick = { navController.navigate(Screen.DiaryView.ById.createRoute(diary.id)) },
                        )
                    }
                }
            }
        }

    }
}