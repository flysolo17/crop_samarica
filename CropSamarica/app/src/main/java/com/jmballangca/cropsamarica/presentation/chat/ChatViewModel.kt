package com.jmballangca.cropsamarica.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.data.local.dao.MessagesDao
import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import com.jmballangca.cropsamarica.data.local.models.chat.MessageUser
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.domain.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val conversation: ConversationRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    private var _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()
    private val _selectedConversation = MutableStateFlow<Conversation?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<Message>> = _selectedConversation
        .onEach { conversation ->
            // update selected conversation in state
            _state.value = _state.value.copy(
                selectedConversation = conversation
            )
        }
        .flatMapLatest { conversation ->
            conversation?.id?.let { conversationId ->
                this.conversation.getMessages(conversationId)
            } ?: flowOf(emptyList())
        }
        .onEach { msgs ->
            // update messages in state
            _state.value = _state.value.copy(
                messages = msgs
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        initialize()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initialize() {
        authRepository.getUserRealTime()
            .onStart {
                _state.value = _state.value.copy(isLoading = true)
            }
            .flatMapLatest { user ->
                _state.value = _state.value.copy(user = user)

                if (user != null) {
                    conversation.getConversations(user.id)
                } else {
                    flowOf(emptyList())
                }
            }
            .onEach { conversations ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    conversations = conversations
                )
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                )
            }
            .launchIn(viewModelScope)
    }



    fun events(e : ChatEvents) {
        when(e) {
            is ChatEvents.SendMessage -> {
               sendMessage(e.message)
            }
        }
    }


    private fun sendMessage(message: String) {
        viewModelScope.launch {
            val userId = _state.value.user?.id ?: run {
                Log.w("SendMessage", "User is null, aborting sendMessage")
                return@launch
            }

            if (_state.value.selectedConversation == null) {
                Log.d("SendMessage", "No selectedConversation → creating new conversation")

                val newMessage = Message(
                    text = message,
                    user = MessageUser.USER,
                    conversationId = 0L // temp, will be replaced in repo
                )

                conversation.insertNew(
                    uid = userId,
                    message = newMessage
                ).onSuccess {
                    Log.i("SendMessage", "New conversation created and message inserted")
                    _state.value = _state.value.copy(
                        isLoading = false,
                    )
                    _selectedConversation.value = it
                }.onFailure { e ->
                    Log.e("SendMessage", "Failed to insert new conversation/message", e)

                    _state.value = _state.value.copy(
                        isLoading = false,

                    )
                }
            } else {
                Log.d(
                    "SendMessage",
                    "Existing conversation → inserting message into conversationId=${_state.value.selectedConversation?.id}"
                )

                val newMessage = Message(
                    text = message,
                    user = MessageUser.USER,
                    conversationId = _state.value.selectedConversation?.id ?: 0L
                )


                try {
                    conversation.insertNew(
                        uid = userId,
                        message = newMessage
                    )
                    Log.i("SendMessage", "Message inserted into existing conversation")
                } catch (e: Exception) {
                    Log.e("SendMessage", "Failed to insert message into existing conversation", e)
                }
            }
        }
    }
}