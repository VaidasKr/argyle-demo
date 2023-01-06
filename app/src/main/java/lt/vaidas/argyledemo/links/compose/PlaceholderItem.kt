package lt.vaidas.argyledemo.links.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lt.vaidas.argyledemo.ui.theme.AppTheme

@Composable
fun PlaceholderItem(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        val color = MaterialTheme.colors.primary.copy(alpha = 0.1f)
        Box(
            modifier = Modifier
                .size(72.dp)
                .border(1.dp, color, RoundedCornerShape(4.dp))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color),
                text = ""
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color),
                text = ""
            )
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun PreviewPlaceholderItem() {
    AppTheme {
        PlaceholderItem(modifier = Modifier.fillMaxWidth())
    }
}
