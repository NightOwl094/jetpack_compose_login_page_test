package com.nightowl.jetpack_architecture.ui.page

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.glide.rememberGlidePainter
import com.nightowl.jetpack_architecture.R
import com.nightowl.jetpack_architecture.ui.const.profileImageUrl
import com.nightowl.jetpack_architecture.ui.theme.Jetpack_ArchitectureTheme

class LoginActivity : ComponentActivity() {
    private val vm by viewModels<LoginActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val focusManager = LocalFocusManager.current

            Jetpack_ArchitectureTheme {

                val id by vm.id.observeAsState(initial = "")
                val password by vm.pw.observeAsState(initial = "")
                val isValidationPass by vm.validationPass.observeAsState(initial = false)

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(80.dp))
                        Image(
                            painter = rememberGlidePainter(request = profileImageUrl),
                            contentDescription = "logo",
                            modifier = Modifier
                                .width(120.dp)
                                .height(120.dp)
                        )
                        Spacer(modifier = Modifier.height(48.dp))

                        Column() {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "아이디를 입력해주세요.") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                                value = id,
                                onValueChange = { text ->
                                    vm.onIdChanged(text)
                                })

                            PasswordTextField(
                                value = password,
                                valueChangeCallback = { pw -> vm.onPasswordChanged(pw) },
                                keyboardActionCallback = { focusManager.clearFocus() }
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = isValidationPass,
                                onClick = { vm.submit() }
                            ) {
                                Text(text = "로그인")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(text = "회원가입")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Jetpack_ArchitectureTheme {
    }
}

@Composable
fun PasswordTextField(
    value: String,
    valueChangeCallback: (String) -> Unit,
    keyboardActionCallback: () -> Unit,
    hint: String = "비밀번호를 입력해주세요."
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    val visualTransformation =
        if (passwordVisibility)
            VisualTransformation.None
        else
            PasswordVisualTransformation()

    val trailingIcon =
        if (passwordVisibility)
            painterResource(id = R.drawable.ic_password_visibility)
        else
            painterResource(id = R.drawable.ic_password_visibility_off)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = hint) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions { keyboardActionCallback() },
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(
                onClick = { passwordVisibility = passwordVisibility.not() }
            ) {
                Icon(
                    painter = trailingIcon,
                    contentDescription = "password visibility"
                )
            }
        },
        value = value,
        onValueChange = { text -> valueChangeCallback(text) }
    )
}

class LoginActivityViewModel : ViewModel() {
    val id = MutableLiveData("")

    fun onIdChanged(id: String) {
        this.id.value = id
        checkValidation()
    }

    val pw = MutableLiveData("")

    fun onPasswordChanged(pw: String) {
        this.pw.value = pw
        checkValidation()
    }

    val validationPass = MutableLiveData(false)

    private fun checkValidation() {
        (id.value.isNullOrEmpty().not() and pw.value.isNullOrEmpty().not()).let {
            validationPass.value = it
        }
    }

    fun submit() {
        Log.d("ttt", "LoginActivityViewModel submit: ${id.value}, ${pw.value}")
    }
}