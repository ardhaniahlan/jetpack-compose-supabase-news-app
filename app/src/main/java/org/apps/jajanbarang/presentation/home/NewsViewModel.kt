package org.apps.jajanbarang.presentation.home

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
import org.apps.jajanbarang.domain.model.Article
import org.apps.jajanbarang.domain.repository.NewsRepository
import javax.inject.Inject
import kotlin.onFailure

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getTopHeadlines(country: String = "us", page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(newsState = ViewState.Loading) }

            val result = newsRepository.getTopHeadlines(country, page)
            result.onSuccess { articles ->
                _uiState.update { it.copy(newsState = ViewState.Success(articles)) }
            }.onFailure { e ->
                _uiState.update { it.copy(newsState = ViewState.Error(e.message ?: "Terjadi Kesalahan")) }
            }
        }
    }

    fun searchNews(query: String, page: Int = 1) {
        viewModelScope.launch {
            _uiState.update { it.copy(newsState = ViewState.Loading) }

            val result = newsRepository.searchNews(query, page)
            result.onSuccess { articles ->
                _uiState.update { it.copy(newsState = ViewState.Success(articles)) }
            }.onFailure { e ->
                _uiState.update { it.copy(newsState = ViewState.Error(e.message ?: "Terjadi Kesalahan")) }
            }
        }
    }

    fun onArticleClick(article: Article) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateToDetail(article))
        }
    }

    fun onReadMoreClick(url: String) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateToWebView(url))
        }
    }
}