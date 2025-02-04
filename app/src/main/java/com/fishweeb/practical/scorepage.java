package com.fishweeb.practical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

public class scorepage extends Activity implements View.OnClickListener,StateBase
{
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    private static final String EMAIL = "email";

    private LoginButton btn_fbLogin;

    ProfilePictureView profile_pic;

    List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private float highscore;
    private String playername;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.scorepage);

        TextView scoreText;
        scoreText = (TextView)findViewById(R.id.scoreText);

        Typeface myfont;
        myfont = Typeface.createFromAsset(getAssets(),"fonts/akashi.ttf");

        highscore = PlayerInfo.Instance.GetScore();
        playername = "Your";

        scoreText.setText(String.format(playername + " highscore is " + (int)highscore));

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        btn_fbLogin = (LoginButton) findViewById(R.id.fb_login_button);
        btn_fbLogin.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().logInWithReadPermissions(this,Arrays.asList("public_profile","email"));

        callbackManager = CallbackManager.Factory.create();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
                if (currentAccessToken == null)
                {
                    profile_pic.setProfileId("");
                }
                else
                {
                    profile_pic.setProfileId(Profile.getCurrentProfile().getId());
                }
            }
        };
        accessTokenTracker.startTracking();

        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this,PERMISSIONS);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                profile_pic.setProfileId(Profile.getCurrentProfile().getId());
                shareScore();

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                loginResult.getAccessToken().getUserId();
            }

            @Override
            public void onCancel()
            {
                System.out.println("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error)
            {
                System.out.println("Login attempt failed.");
            }
        });
    }

    public void shareScore()
    {
        Bitmap image = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        SharePhoto photo = new SharePhoto.Builder().setBitmap(image).setCaption("Thank you for playing MGP2018. Your final score is " + highscore).build();

        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();

        ShareApi.share(content,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    public void onClick(View v)
    {
        if (v == btn_back)
        {
            StateManager.Instance.ChangeState("GameOver");
            GamePage.Instance.ChangePage(GamePage.class);
        }
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
    }

    @Override
    public void OnExit()
    {

    }

    @Override
    public String GetName()
    {
        return "ScorePage";
    }

    @Override
    public void Render(Canvas _canvas)
    {

    }

    @Override
    public void Update(float _dt)
    {
    }
}