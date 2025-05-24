package com.prudhvilearning.movieslot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prudhvilearning.movieslot.ui.theme.Indigo
import com.prudhvilearning.movieslot.utils.DataType

/**
 * Reusable See All button with icon
 */
@Composable
fun SeeAllButton(
    heading: String,
    onSeeAllClick: (String, String, Int) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onSeeAllClick(heading , DataType.CATEGORY.name , 0) }
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "See All",
            color = Indigo,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "See all",
            tint = Indigo
        )
    }
}