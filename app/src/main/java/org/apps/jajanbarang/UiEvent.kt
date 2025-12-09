package org.apps.jajanbarang

import org.apps.jajanbarang.domain.model.Article

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Navigate : UiEvent()
    data class NavigateToDetail(val article: Article) : UiEvent()
    data class NavigateToWebView(val url: String) : UiEvent()
}