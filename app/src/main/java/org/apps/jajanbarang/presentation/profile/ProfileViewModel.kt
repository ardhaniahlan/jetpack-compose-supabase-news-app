package org.apps.jajanbarang.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val client : SupabaseClient

) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                val currentUser = client.auth.currentUserOrNull()

                if (currentUser != null) {
                    val user = userRepository.getProfile(currentUser.id)
                    if (user != null){
                        _uiState.update { it.copy(userState = ViewState.Success(user)) }
                    } else {
                        _uiState.update { it.copy(userState = ViewState.Error("Profil tidak ditemukan")) }
                    }
                } else {
                    _uiState.update { it.copy(userState = ViewState.Error("User Belum Login")) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(userState = ViewState.Error(e.message ?: "Terjadi Kesalahan")) }
            }
        }
    }
}