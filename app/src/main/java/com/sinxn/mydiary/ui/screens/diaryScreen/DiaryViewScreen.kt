package com.sinxn.mydiary.ui.screens.diaryScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinxn.mydiary.data.local.entities.Diary
import com.sinxn.mydiary.ui.components.MyTextField
import com.sinxn.mydiary.ui.components.RectangleFAB
import com.sinxn.mydiary.utils.formatDate
import com.sinxn.mydiary.utils.fromMillis
import com.sinxn.mydiary.utils.toMillis
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryViewScreen(
    modifier: Modifier = Modifier,
    timestamp: LocalDate = LocalDate.now(),
    diaryViewModel: DiaryViewModel,
    isNew: Boolean,
    onFinish: () -> Unit,
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var diaryInputState by remember { mutableStateOf(Diary(timestamp = timestamp)) }
    var isEditing by remember { mutableStateOf(isNew) }

    LaunchedEffect(isNew) {
        if(!isNew) diaryInputState = diaryViewModel.fetchDiaryByTimestamp(timestamp) ?: Diary()
    }

    LaunchedEffect(Unit) {
        diaryViewModel.toastMessage.collectLatest { message -> // or .collect {
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }
    }
    Scaffold(
        floatingActionButton = {
            RectangleFAB(
                onClick = {
                    if (isEditing) {
                        if (diaryInputState.content.isNotEmpty()) {
                            diaryViewModel.addDiary(diaryInputState)
                        } else {
                            diaryViewModel.toast("Note cannot be empty")
                        }
                    } else {
                        isEditing = true
                    }


                }
            ) {
                Icon(
                    if (!isEditing) Icons.Default.Edit else Icons.Default.Check,
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(diaryInputState.timestamp.formatDate(), modifier = Modifier.clickable {
                    showDatePicker = isEditing
                })  },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!isEditing) IconButton(onClick = {
                        diaryInputState.let { diaryViewModel.deleteDiary(it) }
                        onFinish()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }

                })
        },
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MyTextField(
                value = diaryInputState.title,
                onValueChange = { diaryInputState = diaryInputState.copy(title = it) },
                placeholder = "Title",
                readOnly = !isEditing,
                textStyle = TextStyle.Default.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                )

            HorizontalDivider(modifier = Modifier.height(8.dp))
            MyTextField(
                value = diaryInputState.content,
                onValueChange = {diaryInputState = diaryInputState.copy( content = it )},
                placeholder = "Description",
                readOnly = !isEditing,
            )

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = diaryInputState.timestamp.toMillis()
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    diaryInputState = diaryInputState.copy(
                                        timestamp = fromMillis(it)
                                    )
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

        }
    }
}
