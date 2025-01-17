package com.pwojtowicz.buybuddies.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val _messageFlow = MutableSharedFlow<AppMessage>()
    val messageFlow = _messageFlow.asSharedFlow()

    fun showMessage(message: AppMessage) {
        viewModelScope.launch {
            _messageFlow.emit(message)
        }
    }
}