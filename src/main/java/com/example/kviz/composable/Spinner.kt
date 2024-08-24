package com.example.kviz.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset

@Composable
fun Spinner(
    value: String,
    onSelect: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier
) {
    var isExpended by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier.padding(horizontal = 16.dp)){
        Row(modifier = Modifier
            .align(Alignment.Center)
            .wrapContentSize()
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { isExpended = !isExpended }){
                Text(text = value)
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        DropdownMenu(
            expanded = isExpended,
            onDismissRequest = { isExpended=false },
            modifier = Modifier.wrapContentSize(),
            offset = DpOffset(0.dp, (-32).dp)) {
            options.forEach{
                DropdownMenuItem(text = {Text(text = it)},onClick = {
                    onSelect(it)
                    isExpended=false
                    }
                )
            }

        }
    }

}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSpiner() {
    Spinner(
        value = "Izbor",
        onSelect = {},
        options = listOf("Luka", "Sara", "Vukasin")
    )
}
