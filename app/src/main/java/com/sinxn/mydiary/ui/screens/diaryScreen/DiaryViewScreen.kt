package com.sinxn.mydiary.ui.screens.diaryScreen

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sinxn.mydiary.R
import com.sinxn.mydiary.ui.components.ConfirmationDialog
import com.sinxn.mydiary.ui.components.MyTextField
import com.sinxn.mydiary.ui.components.RectangleFAB
import com.sinxn.mydiary.ui.components.rememberPressBackTwiceState
import com.sinxn.mydiary.utils.formatDate
import com.sinxn.mydiary.utils.fromMillis
import com.sinxn.mydiary.utils.toMillis
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiaryViewScreen(
    modifier: Modifier = Modifier,
    date: LocalDate? = null,
    diaryViewModel: DiaryViewModel = hiltViewModel(),
    id: Long? = null,
    onFinish: () -> Unit,
) {
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) } // State for dialog

    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    val diaryInputState by diaryViewModel.diary.collectAsState()
    var isEditing by remember { mutableStateOf(id==null) }

    val handleBackPressAttempt = rememberPressBackTwiceState(
        enabled = isEditing,
        onExit = onFinish
    )
    BackHandler(onBack = handleBackPressAttempt)

    LaunchedEffect(id) {
        if (id == null && date == null) diaryViewModel.resetDiary()
        if(id != null) diaryViewModel.fetchDiaryById(id)
        else if(date != null) diaryViewModel.setDate(date)
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
                        if (diaryInputState.content.isNotEmpty() || diaryInputState.title.isNotEmpty()) {
                            diaryViewModel.addDiary()
                            isEditing = false
                        } else {
                            diaryViewModel.toast(DiaryConstants.DIARY_EMPTY)
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
                title = {},
                navigationIcon = {
                    IconButton(onClick = handleBackPressAttempt) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!isEditing) IconButton(onClick = {
                        showDeleteConfirmationDialog = true
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = diaryInputState.title,
                onValueChange = { diaryViewModel.updateDiaryState(diaryInputState.copy(title = it))  },
                placeholder = "Title",
                readOnly = !isEditing,
                textStyle = TextStyle.Default.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                "${diaryInputState.date.formatDate()} • ${diaryInputState.date.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.FULL, Locale.getDefault())}",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 20.dp).clickable(
                    onClick = { showDatePicker = isEditing }
                )
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            MyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 8.dp),
                value = diaryInputState.content,
                onValueChange = { diaryViewModel.updateDiaryState(diaryInputState.copy( content = it ))},
                placeholder = "Description",
                readOnly = !isEditing,
            )

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = diaryInputState.date.toMillis()
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    diaryViewModel.updateDiaryState(diaryInputState.copy(
                                        date = fromMillis(it)
                                    ))
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

    ConfirmationDialog(
        showDialog = showDeleteConfirmationDialog,
        onDismiss = { showDeleteConfirmationDialog = false },
        onConfirm = {
            diaryViewModel.deleteDiary()
            showDeleteConfirmationDialog = false
            onFinish()
        },
        title = stringResource(R.string.delete_confirmation_title),
        message = stringResource(R.string.delete_item_message)
    )
}
