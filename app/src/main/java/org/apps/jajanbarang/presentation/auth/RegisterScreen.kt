package org.apps.jajanbarang.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import org.apps.jajanbarang.UiEvent
import org.apps.jajanbarang.ViewState
import org.apps.jajanbarang.ui.theme.BluePrimary
import org.apps.jajanbarang.ui.theme.GreyLight
import org.apps.jajanbarang.ui.theme.JajanBarangTheme
import org.apps.jajanbarang.ui.theme.poppinsFontFamily

@Composable
fun RegisterScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthViewModel = hiltViewModel()
){
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                is UiEvent.Navigate -> {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                        launchSingleTop = true
                    }
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RegisterContent(
            uiState = uiState,
            onNameChange = authViewModel::onNameChange,
            onEmailChange = authViewModel::onEmailChange,
            onPasswordChange = authViewModel::onPasswordChange,
            onConfirmPasswordChange = authViewModel::onConfirmPasswordChange,
            onVisibilityChange = authViewModel::togglePasswordVisibility,
            onSignInClick = {
                authViewModel::clearForm
                navController.navigate("login"){
                    popUpTo("register"){
                        inclusive = true
                    }
                }
            },
            onSignUpClick = authViewModel::register
        )

        val isActionLoading = uiState.authState is ViewState.Loading

        if (isActionLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RegisterContent(
    uiState: AuthState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onVisibilityChange: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BluePrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 50.dp, end = 20.dp, bottom = 50.dp)
        ) {
            Text(
                text = "Create Account",
                fontSize = 25.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Register your account today \nusing a valid email and password",
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ))
                .background(Color.White)
                .padding(start = 20.dp, top = 30.dp, end = 20.dp)
                .weight(1f),
        ) {
            Text(
                text = "Sign Up",
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = uiState.name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        text = "Enter your name",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccountBox,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = GreyLight,
                    focusedContainerColor = GreyLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                isError = uiState.nameError != null,
            )
            uiState.nameError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                placeholder = {
                    Text(
                        text = "Enter your mail",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.MailOutline,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = GreyLight,
                    focusedContainerColor = GreyLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                isError = uiState.emailError != null,
            )
            uiState.emailError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                placeholder = {
                    Text(
                        text = "Enter your password",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation =
                    if (uiState.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    if (uiState.password.isNotEmpty()){
                        val image = if (uiState.passwordVisible)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff

                        IconButton(onClick = { onVisibilityChange() }) {
                            Icon(imageVector  = image, contentDescription = null)
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = GreyLight,
                    focusedContainerColor = GreyLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                isError = uiState.passwordError != null,
            )
            uiState.passwordError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            
            TextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                placeholder = {
                    Text(
                        text = "Confirm your password",
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation =
                    if (uiState.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                trailingIcon = {
                    if (uiState.confirmPassword.isNotEmpty()){
                        val image = if (uiState.passwordVisible)
                            Icons.Default.Visibility
                        else Icons.Default.VisibilityOff

                        IconButton(onClick = { onVisibilityChange() }) {
                            Icon(imageVector  = image, contentDescription = null)
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = GreyLight,
                    focusedContainerColor = GreyLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                isError = uiState.confirmPasswordError != null,
            )
            uiState.confirmPasswordError?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                onClick = onSignUpClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Sign In",
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = BluePrimary,
                modifier = Modifier.clickable{ onSignInClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    JajanBarangTheme {
        RegisterContent(
            uiState = AuthState(
                email = "",
                password = ""
            ),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onVisibilityChange = {},
            onSignInClick = {},
            onSignUpClick = {}
        )
    }
}