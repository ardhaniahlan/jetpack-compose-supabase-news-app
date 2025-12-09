package org.apps.jajanbarang.presentation.home

import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.model.Article

data class NewsState(
    val newsState: ViewState<List<Article>> = ViewState.Idle,
)
