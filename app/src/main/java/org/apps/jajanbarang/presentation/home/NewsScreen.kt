package org.apps.jajanbarang.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.apps.jajanbarang.UiEvent
import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.domain.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    navController: NavController,
    newsViewModelViewModel: NewsViewModel = hiltViewModel()
) {
    val uiState by newsViewModelViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        newsViewModelViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.NavigateToDetail -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set("article", event.article)
                    navController.navigate("newsDetail")
                }
                is UiEvent.NavigateToWebView -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set("url", event.url)
                    navController.navigate("newswebview")
                }

                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        newsViewModelViewModel.getTopHeadlines()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "News",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState.newsState) {

                is ViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ViewState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is ViewState.Success -> {
                    val articles = state.data

                    if (articles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No articles found")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(articles) { article ->
                                NewsArticleItem(
                                    article = article,
                                    onClick = { newsViewModelViewModel.onArticleClick(article) },
                                    onReadMoreClick = { newsViewModelViewModel.onReadMoreClick(article.url) }
                                )
                            }
                        }
                    }
                }
                else -> {}
            }

        }
    }
}

@Composable
fun NewsArticleItem(
    article: Article,
    onClick: () -> Unit,
    onReadMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
            .background(Color.White)
    ) {

        if (!article.urlToImage.isNullOrEmpty()) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${article.source?.name ?: "-"}  |  ${formatDate(article.publishedAt)}",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = article.title ?: "-",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!article.description.isNullOrEmpty()) {
            Text(
                text = article.description ?: "",
                fontSize = 14.sp,
                color = Color(0xFF6F6F6F),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.weight(1f).padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0), CircleShape)
                        .padding(6.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = article.author ?: "Unknown",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Text(
                text = "Read More...",
                color = Color(0xFF1976D2),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onReadMoreClick() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}



fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id"))
        val date = inputFormat.parse(dateString)
        outputFormat.format(date) ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

