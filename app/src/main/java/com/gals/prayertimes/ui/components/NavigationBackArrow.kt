package com.gals.prayertimes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gals.prayertimes.R

@Composable
fun NavigationBackArrow(
    onBackAction: () -> Unit
) {
    IconButton(onClick = { onBackAction() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.content_descriptor_back_arrow)
        )
    }
}