package org.apps.jajanbarang.presentation.profile

import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.model.User

data class ProfileState(
    val name: String = "",
    val email: String = "",

    val userState: ViewState<User> = ViewState.Idle
)
