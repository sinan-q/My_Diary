package com.sinxn.mydiary.ui.screens.aboutScreen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sinxn.mydiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val context = LocalContext.current

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = link.toUri()
        context.startActivity(intent)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.about_title),  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onSurface) })
        }
    ) {
        Column(modifier = Modifier.padding(it), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.fillMaxWidth(0.3f),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "app_logo"
            )
            Spacer(modifier = Modifier.height(20.dp))
            CardLayout(
                imageId = R.drawable.about_version,
                text = "Version: 1.0 beta",
                subText = "com.sinxn.mydiary"
            ) {}
            CardLayout(
                imageId = R.drawable.about_github,
                text = "Source Code",
                subText = "https://github.com/sinan-q/My_Diary"
            ) {
                openLink("https://github.com/sinan-q/My_Diary")
            }
            CardLayout(
                imageId = R.drawable.about_issues,
                text = "Issues",
                subText = "Report any issues"
            ) {
                openLink("https://github.com/sinan-q/My_Diary/issues")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Made with ❤️ Sinan", modifier = Modifier.clickable {
                openLink("https://github.com/sinan-q/")
            })
        }
    }
}

@Composable
fun CardLayout(
    imageId: Int,
    imageDesc: String? = null,
    text: String,
    subText: String = "",
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 5.dp),
    ) {
        Row(modifier = Modifier.clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier
                .padding(15.dp)
                .size(50.dp), painter = painterResource(id = imageId), contentDescription = imageDesc)
            Column() {
                Text(text = text, fontWeight = FontWeight.Bold)
                if (subText.isNotBlank()) Text(text = subText, fontWeight = FontWeight.ExtraLight, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}