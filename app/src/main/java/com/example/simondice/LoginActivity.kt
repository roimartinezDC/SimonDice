package com.example.simondice

import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val TAG = "OneTap:"
    private val REQ_ONE_TAP = 2
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        auth = Firebase.auth

        // CONFIGURACIÓN CLIENTE DE ACCESO ONE TAP
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            // para el acceso con Google
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // ID de cliente que aparece en OAuth2.0 de la Consola de Google Cloud
                    .setServerClientId("1086336482884-kehqrq7u9nhu6ljtv5c0to2u2rensnjk.apps.googleusercontent.com")
                    // esto hace que el cliente OneTap solicite a los usuarios sólo las
                    // cuentas Google con las que ya hayan acedido previamente
                        //en nuestro caso nos interesa ponerla en false, si no probablemente nos de el error 16 en el Listener
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        // MOSTRAR IU DE ACCESO ONE TAP
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0)

                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                e.localizedMessage?.let { Log.d(TAG, "FailureListener: $it") }
            }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    if (data == null) {
                        Log.w(TAG, "hhhhhhnull")
                    } else {
                        data.identifier?.let { Log.w(TAG, it) }
                        data.action?.let { Log.w(TAG, it) }
                        data.type?.let { Log.w(TAG, it) }
                    }
                    //val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    /*
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val text : TextView = findViewById(R.id.recordMsg)
                            text.text = credential.id
                            // Got an ID token from Google. Use it to authenticate with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }

                     */
                } catch (e: ApiException) {
                    Log.e(TAG, "hhhhhh"+e.toString())
                }
            }
        }
    }

}

