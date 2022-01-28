// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    GlobalClipboard().createListener()

    val requester = FocusRequester()
    Box(
        Modifier
            .onKeyEvent {
                if (it.isCtrlPressed && it.key.nativeKeyCode == 65 && it.type == KeyEventType.KeyDown) {
                    println("Ctrl + A is pressed, ${it.key.nativeKeyCode}")
                    true
                } else {
                    // let other handlers receive this event
                    false
                }
            }
            .focusRequester(requester)
            .focusable()
            .size(10.dp)
    )
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

fun main() {
    GlobalClipboard().createListener()
//    Window(onCloseRequest = ::exitApplication) {
//        App()
//    }
}