package org.apps.jajanbarang.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apps.jajanbarang.UiEvent
import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(){
        viewModelScope.launch {
            _uiState.update { it.copy(authState = ViewState.Loading) }

            repository.login(
                email = _uiState.value.email,
                password = _uiState.value.password
            ).onSuccess { user ->
                _uiState.update { it.copy(authState = ViewState.Success(user)) }
                _eventFlow.emit(UiEvent.ShowSnackbar("Selamat datang, ${user.name}"))
                _eventFlow.emit(UiEvent.Navigate)
            }.onFailure { e ->
                _uiState.update { it.copy(authState = ViewState.Error(e.message ?: "Login gagal")) }
            }
        }
    }

    fun register(){
        viewModelScope.launch {
            _uiState.update { it.copy(authState = ViewState.Loading) }
            repository.register(
                name = _uiState.value.name,
                email = _uiState.value.email,
                password = _uiState.value.password
            ).onSuccess { user ->
                _uiState.update { it.copy(authState = ViewState.Success(user)) }
                _eventFlow.emit(UiEvent.ShowSnackbar("Register berhasil"))
                _eventFlow.emit(UiEvent.Navigate)
            }.onFailure { e ->
                _uiState.update { it.copy(authState = ViewState.Error(e.message ?: "Register gagal")) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                Log.d("AuthViewModel", "Logout dari auth berhasil")
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Logout error: ${e.message}")
            }

            // ✅ Clear authState (tidak perlu _currentUser terpisah)
            _uiState.value = AuthState()
            Log.d("AuthViewModel", "AuthState cleared to Idle")

            _eventFlow.emit(UiEvent.Navigate)
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = if (name.isNotEmpty() && name.length < 3) {
                    "Minimal 3 digit dan tidak boleh Kosong"
                } else null
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = if (email.isNotEmpty() && !isValidGmail(email)) {
                    "Email tidak valid"
                } else null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = if (password.isNotEmpty() && password.length < 6) {
                    "Password minimal 6 karakter"
                } else null
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update{
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = if (confirmPassword.isNotEmpty() && confirmPassword != _uiState.value.password) {
                    "Password tidak sama"
                } else null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(
                passwordVisible = !it.passwordVisible
            )
        }
    }

    fun isValidGmail(input: String): Boolean {
        return Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$").matches(input)
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                name = "",
                email = "",
                password = "",
                confirmPassword = ""
            )
        }
    }

}