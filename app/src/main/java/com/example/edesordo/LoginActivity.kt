package com.example.edesordo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edesordo.Viewmodels.LoginViewModel
import com.example.edesordo.data.Validator
import com.example.edesordo.ui.theme.EdesOrdoTheme

class LoginActivity : ComponentActivity() {
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EdesOrdoTheme {
                // A surface container using the 'background' color from the theme
                MyApp(viewModel)
            }
            viewModel.error.observe(this) { validator ->
                if (!validator.isValid) {
                    Toast.makeText(this, validator.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    gotoHomeActivity()
                }
            }
        }

    }

    private fun gotoHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }




}

@Composable
fun Logo() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(2.dp)
    ) {
        Row(modifier = Modifier.padding(3.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.app_name1),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.size(1.dp))
                Divider(
                    color = Color.Black,
                    thickness = 5.dp,
                    modifier = Modifier
                        .width(50.dp)

                )
            }
            Spacer(modifier = Modifier.size(1.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 9.dp)
            ) {
                Divider(
                    color = Color.Black,
                    thickness = 5.dp,
                    modifier = Modifier
                        .width(50.dp)
                )
                Spacer(modifier = Modifier.size(3.dp))
                Text(
                    text = stringResource(id = R.string.app_name2),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,

                    )

            }
        }
    }
}


@Composable
fun login(loginviewmodel: LoginViewModel?) {
    var email by remember {
        mutableStateOf("A@e.c")
    }
    var password by remember {
        mutableStateOf("123")
    }
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            colors = OutlinedTextFieldDefaults.colors(unfocusedTextColor = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                email = it

            },
            label = { Text(text = "email") })
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            colors = OutlinedTextFieldDefaults.colors(unfocusedTextColor = MaterialTheme.colorScheme.onSurface),
            onValueChange = {
                password = it

            },
            label = { Text(text = "password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                // Toggle button to hide or display password
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            })
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Forgot password?", modifier = Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.size(4.dp))
        loginviewmodel?.setEmail(email)
        loginviewmodel?.setPassword(password)
    }

}

@Composable
fun bottomscreen(onValidate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = "google")
            Spacer(modifier = Modifier.size(14.dp))
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "google"
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(onClick = { onValidate() }) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.size(4.dp))
        Row {
            Text(text = "Dont have your account? ")
            Text(text = "Sign Up Now")
        }
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Composable
fun topscreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
    }
}

@Composable
fun MyApp(loginActivity: LoginViewModel) {

    EdesOrdoTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                topscreen()
                login(loginActivity)
                Spacer(modifier = Modifier.weight(1f))
                bottomscreen(onValidate = {
                    loginActivity.isemailvalid()
                })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {

    topscreen()

}