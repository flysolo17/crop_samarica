package com.jmballangca.cropsamarica.domain.repository.impl


import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.domain.repository.UserWithVerifiedStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepository {
    private val _userRef = firestore.collection("users")
    override suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            delay(1000)
            Result.success("Logged in successfully")
        } catch (e: FirebaseAuthException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(
                id = result.user?.uid ?: "",
                name = name,
                email = email
            )
            _userRef.document(result.user?.uid ?: "").set(user).await()
            delay(1000)
            Result.success("Registered successfully")
        } catch (e: FirebaseAuthException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            auth.signOut()
            delay(1000)
            Result.success("Logged out successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(): Result<UserWithVerifiedStatus?> {
        return try {
            val uid = auth.currentUser?.uid
            if (uid == null) {
                Result.success(null)
            }
            val result = _userRef.document(uid!!).get().await()
            val user = result.toObject(User::class.java)
            delay(1000)
            Result.success(UserWithVerifiedStatus(user!!, auth.currentUser!!.isEmailVerified))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserRealTime(): Flow<User?> {
        return callbackFlow {
            val uid = auth.currentUser?.uid
            if (uid == null) {
                trySend(null).isSuccess
                return@callbackFlow
            }
            val listener = _userRef.document(uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val user = snapshot.toObject(User::class.java)
                    trySend(user).isSuccess
                }
            }
            awaitClose { listener.remove() }
        }
    }

    override suspend fun forgotPassword(
        email: String
    ): Result<String> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            delay(1000)
            Result.success("Password reset email sent successfully")
        } catch (e : Exception) {
            Result.failure(e)
        }
    }


    override suspend fun signInWithGoogle(): Result<String> {
        return try {
            val credential = getCredential(true).getOrThrow()
            if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                return Result.failure(Exception("Invalid Google credential"))
            }

            val token = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(token, null)

            val authResult = auth.signInWithCredential(firebaseCredential).await()
                ?: return Result.failure(Exception("Authentication failed"))

            val uid = authResult.user?.uid ?: return Result.failure(Exception("User UID missing"))

            val userDoc = _userRef.document(uid).get().await()
            if (!userDoc.exists()) {
                Result.failure(Exception("User not found"))
            } else {
                Result.success("Logged in successfully")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithGoogle(): Result<String> {
        return try {
            val credential = getCredential(false).getOrThrow()
            if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                return Result.failure(Exception("Invalid Google credential"))
            }

            val token = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(token, null)

            val authResult = auth.signInWithCredential(firebaseCredential).await()
                ?: return Result.failure(Exception("Authentication failed"))

            val user = authResult.user ?: return Result.failure(Exception("User not found"))

            val userDoc = _userRef.document(user.uid).get().await()
            if (userDoc.exists()) {
                return Result.failure(Exception("User already exists"))
            }

            val newUser = User(
                id = user.uid,
                name = user.displayName ?: "",
                email = user.email ?: "",
                profile = user.photoUrl?.toString() ?: ""
            )
            _userRef.document(user.uid).set(newUser).await()
            Result.success("Registered successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun changeName(
        uid: String,
        name: String,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        delay(1000)
        _userRef.document(uid).update("name", name).addOnCompleteListener {
            if (it.isSuccessful) {
                result.invoke(UIState.Success("Name changed successfully"))
            } else {
                result.invoke(UIState.Error(it.exception?.message ?: "Unknown error"))
            }
        }.addOnFailureListener {
            result.invoke(UIState.Error(it.message ?: "Unknown error"))
        }
    }
    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (UIState<String>) -> Unit
    ) {
        result(UIState.Loading)

        try {
            val user = auth.currentUser ?: return result(
                UIState.Error("User not logged in")
            )

            val email = user.email ?: return result(
                UIState.Error("User email not found")
            )

            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
            delay(1000)
            result(UIState.Success("Password changed successfully"))
        } catch (e: Exception) {
            result(UIState.Error(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun sendEmailVerification(): Result<String> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
            user.sendEmailVerification().await()
            delay(1000)
            Result.success("Verification email sent successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend  fun checkUserVerified(): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                currentUser.reload().await()
                val isVerified = currentUser.isEmailVerified
                Result.success(isVerified)
            } else {
                Result.failure(Exception("No authenticated user found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun getCredential(
        filterByAuthorizedAccounts: Boolean = false
    ): Result<Credential> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val credential = credentialManager.getCredential(context,request).credential
            Result.success(credential)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error getting credential", e)
            Result.failure(e)
        }
    }
}