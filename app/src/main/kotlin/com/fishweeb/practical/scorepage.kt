package com.fishweeb.practical

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.Profile.id
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.login.widget.ProfilePictureView
import com.facebook.share.ShareApi
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhoto.Builder.build
import com.facebook.share.model.SharePhoto.Builder.setBitmap
import com.facebook.share.model.SharePhoto.Builder.setCaption
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.SharePhotoContent.Builder.addPhoto
import com.facebook.share.model.SharePhotoContent.Builder.build
import java.util.Arrays

class scorepage : Activity(), View.OnClickListener, StateBase {
    private var callbackManager: CallbackManager? = null
    private var loginManager: LoginManager? = null

    private var btn_fbLogin: LoginButton? = null

    var profile_pic: ProfilePictureView? = null

    var PERMISSIONS: List<String> = mutableListOf("publish_actions")

    private var highscore = 0f
    private var playername: String? = null
    private var btn_back: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdkInitialize(this.applicationContext)

        setContentView(R.layout.scorepage)
        val scoreText = findViewById<View>(R.id.scoreText) as TextView
        val myfont = Typeface.createFromAsset(assets, "fonts/akashi.ttf")

        highscore = PlayerInfo.Companion.Instance.GetScore().toFloat()
        playername = "Your"

        scoreText.text = String.format(playername + " highscore is " + highscore.toInt())

        btn_back = findViewById<View>(R.id.btn_back) as Button
        btn_back!!.setOnClickListener(this)

        btn_fbLogin = findViewById<View>(R.id.fb_login_button) as LoginButton
        btn_fbLogin!!.setReadPermissions(Arrays.asList(EMAIL))
        LoginManager.getInstance()
            .logInWithReadPermissions(this, mutableListOf("public_profile", "email"))

        callbackManager = create()

        val accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if (currentAccessToken == null) {
                    profile_pic!!.profileId = ""
                } else {
                    profile_pic!!.profileId = getCurrentProfile.getCurrentProfile().id
                }
            }
        }
        accessTokenTracker.startTracking()

        loginManager = LoginManager.getInstance()
        loginManager!!.logInWithPublishPermissions(this, PERMISSIONS)

        loginManager!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult) {
                profile_pic!!.profileId = getCurrentProfile.getCurrentProfile().id
                shareScore()

                val accessToken = AccessToken.getCurrentAccessToken()
                loginResult.accessToken.userId
            }

            override fun onCancel() {
                println("Login attempt canceled.")
            }

            override fun onError(error: FacebookException) {
                println("Login attempt failed.")
            }
        })
    }

    fun shareScore() {
        val image = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val photo: SharePhoto = Builder().setBitmap(image)
            .setCaption("Thank you for playing MGP2018. Your final score is $highscore").build()

        val content: SharePhotoContent = Builder().addPhoto(photo).build()

        ShareApi.share(content, null)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onClick(v: View) {
        if (v === btn_back) {
            StateManager.Companion.Instance.ChangeState("GameOver")
            GamePage.Companion.Instance!!.ChangePage(GamePage::class.java)
        }
    }

    override fun OnEnter(_view: SurfaceView) {
    }

    override fun OnExit() {
    }

    override fun GetName(): String {
        return "ScorePage"
    }

    override fun Render(_canvas: Canvas) {
    }

    override fun Update(_dt: Float) {
    }

    companion object {
        private const val EMAIL = "email"
    }
}