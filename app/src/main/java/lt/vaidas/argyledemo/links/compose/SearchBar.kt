package lt.vaidas.argyledemo.links.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lt.vaidas.argyledemo.ui.theme.AppTheme

@Composable
fun SearchBar(modifier: Modifier = Modifier, text: String, hint: String, onTextChanged: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChanged,
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        placeholder = { Text(text = hint) },
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    modifier = Modifier.clickable { onTextChanged("") },
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Clear search"
                )
            }
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewEmptySearchBar() {
    AppTheme {
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), text = "", "Search", onTextChanged = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewFilledSearchBar() {
    AppTheme {
        SearchBar(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), text = "Amazon", "Search", onTextChanged = {})
    }
}
