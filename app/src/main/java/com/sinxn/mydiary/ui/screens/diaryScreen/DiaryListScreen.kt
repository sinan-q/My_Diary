package com.sinxn.mydiary.ui.screens.diaryScreen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sinxn.mydiary.ui.components.BottomBar
import com.sinxn.mydiary.ui.components.MyTextField
import com.sinxn.mydiary.ui.components.MyTopAppBar
import com.sinxn.mydiary.ui.components.RectangleCard
import com.sinxn.mydiary.ui.components.RectangleFAB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    diaryViewModel: DiaryViewModel,
    onAddDiaryClick: () -> Unit,
    onDiaryClick: (id: Long) -> Unit,
    navController: NavController,
) {
    val diaries by diaryViewModel.diaries.collectAsState()
    var search by remember { mutableStateOf("") }

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = {
            RectangleFAB(onClick = { onAddDiaryClick() }) {
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
                        modifier = Modifier,
                        value = search,
                        onValueChange = { search = it },
                        placeholder = "Search"
                    )
                    IconButton(
                        modifier = Modifier,
                        onClick = { diaryViewModel.searchDiaries(search) }
                    ) {
                        Icon(Icons.Default.Search, "Search button")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            if(diaries.isEmpty()) Text("No Diaries Found", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                items(diaries) { diary ->
                    DiaryItem(
                        diary = diary,
                        onClick = { onDiaryClick(diary.id) }
                    )
                }
            }
        }

    }
}