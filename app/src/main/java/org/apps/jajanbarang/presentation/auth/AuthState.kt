package org.apps.jajanbarang.presentation.auth

import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.model.User

data class AuthState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    var passwordVisible: Boolean = false,

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val authState: ViewState<User> = ViewState.Idle,
    val statusMessage: String = ""
)