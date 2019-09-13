package com.uvtech.makerwala.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonObject
import com.uvtech.makerwala.ApplicationConstants
import com.uvtech.makerwala.ApplicationConstants.REQUEST_PERMISSIONS_GOOGLE_LOGIN
import com.uvtech.makerwala.R
import com.uvtech.makerwala.api.ApiCallingHelper
import com.uvtech.makerwala.api.ApiCallingInterface
import com.uvtech.makerwala.api.WebFields
import com.uvtech.makerwala.api.WebLinks
import com.uvtech.makerwala.helpers.*
import com.uvtech.makerwala.utilities.Utility
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        tvTitle.text = resources.getString(R.string.str_login)
        llLeft.setOnClickListener { onBackPressed() }

        btnUserLogin.setOnClickListener { onClickUserLogin() }
        btnRegister.setOnClickListener { Utility.callActivity(this, RegisterActivity::class.java) }
        tvForgotPassword.setOnClickListener { Utility.callActivity(this, ForgotPasswordActivity::class.java) }

        tvLoginWithGoogle.setOnClickListener { onClickGoogleLogin() }
        btnSendOTP.setOnClickListener { onClickSendOtp() }
        btnOTPLogin.setOnClickListener { onClickOTPLogin() }

        tvLoginWithUser.setOnClickListener {
            llOTPVerifySection.visibility = View.GONE
            btnSendOTP.isEnabled = true
            if (tvLoginWithUser.text.toString() == resources.getString(R.string.str_login_with_email)) {
                tvLoginWithUser.text = resources.getString(R.string.str_login_with_otp)
                llOTP.visibility = View.GONE
                llUser.visibility = View.VISIBLE
            } else {
                tvLoginWithUser.text = resources.getString(R.string.str_login_with_email)
                llOTP.visibility = View.VISIBLE
                llUser.visibility = View.GONE
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .addConnectionCallbacks(this)
            .build()

//        etEmailAddress.setText("tom@gmail.com")
//        etPassword.setText("1234567890")

    }

    private fun checkUserValidation(): Boolean {
        if (etEmailAddress.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_email_address)
            return false
        }
        if (etPassword.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_password)
            return false
        }
        return true
    }

    private fun onClickUserLogin() {
        if (checkUserValidation()) {
            val jsonObjectParam = JsonObject()
            jsonObjectParam.addProperty(WebFields.USERNAME, etEmailAddress.text.toString())
            jsonObjectParam.addProperty(WebFields.PASSWORD, etPassword.text.toString())
            jsonObjectParam.addProperty(WebFields.TYPE, 2)
            jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)

            ApiCallingHelper().callPostApi(this, WebLinks.LOGIN, jsonObjectParam,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        var jsonObject = JSONObject(response)
                        jsonObject = jsonObject.getJSONObject(WebFields.DATA)
                        val jsonObjectLogin = jsonObject.getJSONObject(WebFields.LOGIN_DETAIL)

                        val u = User(
                            JsonHelper.getInt(jsonObjectLogin, WebFields.TEACHER_ID),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.BLOCK_ID),
                            JsonHelper.getString(jsonObjectLogin, WebFields.BLOCK_NAME),
                            JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_MOBILE),
                            JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_EMAIL),
                            JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_ADDRESS),
                            JsonHelper.getString(jsonObjectLogin, WebFields.PASSWORD),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.COUNTRY_ID),
                            JsonHelper.getString(jsonObjectLogin, WebFields.COUNTRY_NAME),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.STATE_ID),
                            JsonHelper.getString(jsonObjectLogin, WebFields.STATE_NAME),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.CITY_ID),
                            JsonHelper.getString(jsonObjectLogin, WebFields.CITY_NAME),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.USER_ROLE_ID),
                            JsonHelper.getInt(jsonObjectLogin, WebFields.SCHOOL_ID),
                            JsonHelper.getString(jsonObjectLogin, WebFields.SCHOOL_NAME),
                            JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_FIRST_NAME),
                            JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_LAST_NAME),
                            JsonHelper.getString(jsonObjectLogin, WebFields.IMG_PROFILE)
                        )

                        val jsonArrayClass = jsonObject.getJSONArray(WebFields.TEACHER_CLASS)
                        val jsonArraySubject = jsonObject.getJSONArray(WebFields.TEACHER_SUBJECT)
                        val classIds = ArrayList<Int>()
                        val classNames = ArrayList<String>()
                        val subjectIds = ArrayList<Int>()
                        val subjectNames = ArrayList<String>()

                        for (i in 0 until jsonArrayClass.length()) {
                            val classes = jsonArrayClass.getJSONObject(i)
                            classIds.add(classes.getInt(WebFields.CLASS_LEVEL_ID))
                            classNames.add(classes.getString(WebFields.CLASS_LEVEL_NAME))
                        }
                        for (i in 0 until jsonArraySubject.length()) {
                            val subjects = jsonArraySubject.getJSONObject(i)
                            subjectIds.add(subjects.getInt(WebFields.SUBJECT_ID))
                            subjectNames.add(subjects.getString(WebFields.SUBJECT_NAME))
                        }

                        u.classIds = classIds
                        u.classNames = classNames
                        u.subjectIds = subjectIds
                        u.subjectNames = subjectNames

                        LoginHelper.instance.userData = u
                        Utility.callNewActivity(this@LoginActivity, MainActivity::class.java)
                    }

                    override fun onFailure(errorMessage: String) {
                        ToastHelper.displayInfo(errorMessage)
                    }
                })
        }
    }

    private fun checkOtpValidation(): Boolean {
        if (etMobileNumber.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_mobile_number)
            return false
        }
        if (ValidationHelper.isValidMobileNumber(etMobileNumber.text.toString())) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_valid_mobile_number)
            return false
        }
        return true
    }

    lateinit var jsonObjectOTP: JSONObject

    private fun onClickSendOtp() {
        if (checkOtpValidation()) {
            val jsonObjectParam = JsonObject()
            jsonObjectParam.addProperty(WebFields.USERNAME, etMobileNumber.text.toString())
            jsonObjectParam.addProperty(WebFields.TYPE, 3)
            jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)

            ApiCallingHelper().callPostApi(this, WebLinks.LOGIN, jsonObjectParam,
                object : ApiCallingInterface {
                    override fun onSuccess(response: String, message: String) {
                        val jsonObject = JSONObject(response)
                        jsonObjectOTP = jsonObject.getJSONObject(WebFields.DATA)
                        llOTPVerifySection.visibility = View.VISIBLE
                        btnSendOTP.isEnabled = false
                    }

                    override fun onFailure(errorMessage: String) {
                        ToastHelper.displayInfo(errorMessage)
                    }
                })
        }
    }

    private fun checkVerifyOtpValidation(): Boolean {
        if (etOTP.text.toString().isEmpty()) {
            ToastHelper.displayDialog(this, R.string.str_please_enter_OTP)
            return false
        }
        return true
    }

    private fun onClickOTPLogin() {
        if (checkVerifyOtpValidation()) {
            val jsonObjectLogin = jsonObjectOTP.getJSONObject(WebFields.LOGIN_DETAIL)
            if (etOTP.text.toString() == JsonHelper.getString(jsonObjectLogin,WebFields.OTP_CODE)) {

                val u = User(
                    JsonHelper.getInt(jsonObjectLogin, WebFields.TEACHER_ID),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.BLOCK_ID),
                    JsonHelper.getString(jsonObjectLogin, WebFields.BLOCK_NAME),
                    JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_MOBILE),
                    JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_EMAIL),
                    JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_ADDRESS),
                    JsonHelper.getString(jsonObjectLogin, WebFields.PASSWORD),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.COUNTRY_ID),
                    JsonHelper.getString(jsonObjectLogin, WebFields.COUNTRY_NAME),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.STATE_ID),
                    JsonHelper.getString(jsonObjectLogin, WebFields.STATE_NAME),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.CITY_ID),
                    JsonHelper.getString(jsonObjectLogin, WebFields.CITY_NAME),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.USER_ROLE_ID),
                    JsonHelper.getInt(jsonObjectLogin, WebFields.SCHOOL_ID),
                    JsonHelper.getString(jsonObjectLogin, WebFields.SCHOOL_NAME),
                    JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_FIRST_NAME),
                    JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_LAST_NAME),
                    JsonHelper.getString(jsonObjectLogin, WebFields.IMG_PROFILE)
                )

                val jsonArrayClass = jsonObjectOTP.getJSONArray(WebFields.TEACHER_CLASS)
                val jsonArraySubject = jsonObjectOTP.getJSONArray(WebFields.TEACHER_SUBJECT)
                val classIds = ArrayList<Int>()
                val classNames = ArrayList<String>()
                val subjectIds = ArrayList<Int>()
                val subjectNames = ArrayList<String>()

                for (i in 0 until jsonArrayClass.length()) {
                    val classes = jsonArrayClass.getJSONObject(i)
                    classIds.add(JsonHelper.getInt(classes,WebFields.CLASS_LEVEL_ID))
                    classNames.add(JsonHelper.getString(classes,WebFields.CLASS_LEVEL_NAME))
                }
                for (i in 0 until jsonArraySubject.length()) {
                    val subjects = jsonArraySubject.getJSONObject(i)
                    subjectIds.add(JsonHelper.getInt(subjects,WebFields.SUBJECT_ID))
                    subjectNames.add(JsonHelper.getString(subjects,WebFields.SUBJECT_NAME))
                }

                u.classIds = classIds
                u.classNames = classNames
                u.subjectIds = subjectIds
                u.subjectNames = subjectNames

                LoginHelper.instance.userData = u
                Utility.callNewActivity(this@LoginActivity, MainActivity::class.java)
            } else {
                ToastHelper.displayInfo(R.string.str_please_enter_correct_otp)
            }
        }
    }

    private fun onClickGoogleLogin() {
        Utility.hideKeyboard(this, etOTP)
        if (ConnectivityHelper.isConnectingToInternet()) {
            if (Utility.checkPlayServices(this)) {
                if (mGoogleApiClient != null) {
                    mGoogleApiClient?.clearDefaultAccountAndReconnect()
                    val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                    startActivityForResult(signInIntent, REQUEST_PERMISSIONS_GOOGLE_LOGIN)
                }
            } else {
                ToastHelper.displayInfo("Device not supported")
            }
        } else {
            ToastHelper.displayInfo(R.string.no_internet_connection)
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        ToastHelper.displayDialog(this, p0.errorMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSIONS_GOOGLE_LOGIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            LogHelper.e("asdf " + result.status)
            if (result.isSuccess) {
                val acct = result.signInAccount

                if (acct != null) {
//                    LogHelper.e(acct.toString())
//                    LogHelper.e(acct.id + " " + acct.email + " " + acct.displayName + " " + acct.familyName + " " + acct.givenName)
                    emailAddress = acct.email
                    firstName = acct.givenName!!.replace(" ", "")
                    lastName = acct.familyName!!.replace(" ", "")
                    callSocialLogin()
                }
            } else {
                ToastHelper.displayInfo("Google login Failed")
            }
        }
    }

    private var emailAddress: String? = ""
    private var firstName: String? = ""
    private var lastName: String? = ""

    private fun callSocialLogin() {
//        ToastHelper.displayInfo(emailAddress + " " + userName)
        val jsonObjectParam = JsonObject()
        jsonObjectParam.addProperty(WebFields.USERNAME, emailAddress)
        jsonObjectParam.addProperty(WebFields.TYPE, 1)
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY)

        ApiCallingHelper().callPostApi(this, WebLinks.LOGIN, jsonObjectParam,
            object : ApiCallingInterface {
                override fun onSuccess(response: String, message: String) {
                    var jsonObject = JSONObject(response)
                    jsonObject = jsonObject.getJSONObject(WebFields.DATA)
                    val jsonObjectLogin = jsonObject.getJSONObject(WebFields.LOGIN_DETAIL)

                    val u = User(
                        JsonHelper.getInt(jsonObjectLogin, WebFields.TEACHER_ID),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.BLOCK_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.BLOCK_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_MOBILE),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_EMAIL),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_ADDRESS),
                        JsonHelper.getString(jsonObjectLogin, WebFields.PASSWORD),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.COUNTRY_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.COUNTRY_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.STATE_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.STATE_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.CITY_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.CITY_NAME),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.USER_ROLE_ID),
                        JsonHelper.getInt(jsonObjectLogin, WebFields.SCHOOL_ID),
                        JsonHelper.getString(jsonObjectLogin, WebFields.SCHOOL_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_FIRST_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.TEACHER_LAST_NAME),
                        JsonHelper.getString(jsonObjectLogin, WebFields.IMG_PROFILE)
                    )

                    val jsonArrayClass = jsonObject.getJSONArray(WebFields.TEACHER_CLASS)
                    val jsonArraySubject = jsonObject.getJSONArray(WebFields.TEACHER_SUBJECT)
                    val classIds = ArrayList<Int>()
                    val classNames = ArrayList<String>()
                    val subjectIds = ArrayList<Int>()
                    val subjectNames = ArrayList<String>()

                    for (i in 0 until jsonArrayClass.length()) {
                        val classes = jsonArrayClass.getJSONObject(i)
                        classIds.add(classes.getInt(WebFields.CLASS_LEVEL_ID))
                        classNames.add(classes.getString(WebFields.CLASS_LEVEL_NAME))
                    }
                    for (i in 0 until jsonArraySubject.length()) {
                        val subjects = jsonArraySubject.getJSONObject(i)
                        subjectIds.add(subjects.getInt(WebFields.SUBJECT_ID))
                        subjectNames.add(subjects.getString(WebFields.SUBJECT_NAME))
                    }

                    u.classIds = classIds
                    u.classNames = classNames
                    u.subjectIds = subjectIds
                    u.subjectNames = subjectNames

                    LoginHelper.instance.userData = u
                    Utility.callNewActivity(this@LoginActivity, MainActivity::class.java)
                }

                override fun onFailure(errorMessage: String) {
                    ToastHelper.displayInfo(errorMessage)
                    Utility.callActivity(
                        this@LoginActivity, RegisterActivity::class.java,
                        WebFields.TEACHER_EMAIL, emailAddress!!,
                        WebFields.TEACHER_FIRST_NAME, firstName!!,
                        WebFields.TEACHER_LAST_NAME, lastName!!
                    )
                }
            })
    }

    override fun onConnected(p0: Bundle?) {
        LogHelper.e("onconnected " + p0.toString())
    }

    override fun onConnectionSuspended(p0: Int) {
        LogHelper.e("onconnesuspended " + p0)
    }

    override fun onBackPressed() {
        Utility.killActivity(this)
    }
}
