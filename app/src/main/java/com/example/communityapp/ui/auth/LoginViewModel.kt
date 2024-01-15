package com.example.communityapp.ui.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.communityapp.data.models.FamilyData
import com.example.communityapp.data.models.Member
import com.example.communityapp.data.repository.DashboardRepo
import com.example.communityapp.utils.Constants
import com.example.communityapp.utils.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class LoginViewModel@Inject constructor(private val auth: FirebaseAuth,
                                        private var dashboardRepo: DashboardRepo
) : ViewModel(){


    val TAG = "Login View Model"
    private val verification = MutableLiveData<Resource<Pair<Int,String>>>()
    val verificationStatus: MutableLiveData<Resource<Pair<Int,String>>>
        get() = verification
    fun OnVerificationCodeSent(phoneNumber:String, activity: Activity){

        verification.value = Resource.loading()

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential,activity)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                verification.value = Resource.error(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")
                verification.value = Resource.success(Pair(1,verificationId))
                Constants.verID = verificationId
                Constants.token = token.toString()
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)



    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: Activity) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    verificationStatus.value = Resource.success(Pair(2,user.toString()))
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                }
            }
    }

    private val _user_data = MutableLiveData<Resource<FamilyData>>()

    val user_data : LiveData<Resource<FamilyData>>
        get() = _user_data

    fun getMember(contact : String){
        _user_data.value = Resource.loading()
        viewModelScope.launch {
            try{
                val user = dashboardRepo.findMember(contact)
                val data = FamilyData(user)
                _user_data.value = Resource.success(data)
            }catch (e : Exception){
                _user_data.value = Resource.error(e)
            }
        }
    }
}