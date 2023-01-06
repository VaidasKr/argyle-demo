package lt.vaidas.argyledemo.links.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import lt.vaidas.argyledemo.links.LinkItem
import lt.vaidas.argyledemo.ui.theme.AppTheme

@Composable
fun LinkRow(modifier: Modifier = Modifier, link: LinkItem) {
    Row(modifier = modifier) {
        GlideImage(
            modifier = Modifier.size(72.dp),
            imageModel = { link.logo },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop, alignment = Alignment.Center),
            loading = {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                )
            },
            failure = {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = link.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = link.kind)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun PreviewLinkRow() {
    AppTheme {
        LinkRow(link = LinkItem("Amazon", "", "Gig"))
    }
}
