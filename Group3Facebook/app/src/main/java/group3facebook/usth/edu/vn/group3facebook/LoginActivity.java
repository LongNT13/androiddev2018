package group3facebook.usth.edu.vn.group3facebook;


import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String PROFILE_FIRST_NAME = "PROFILE_FIRST_NAME";
    public static final String PROFILE_LAST_NAME = "PROFILE_LAST_NAME";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static String id;
    public static String email;
    public static String gender;
    ProfilePictureView profilePictureView;
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        //LoginManager.getInstance().logOut();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());

        if(isLoggedIn()){

            loginSuccess();

        }else {

            callbackManager = CallbackManager.Factory.create();

            initVariables();

            //get permission
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_posts", "user_photos","user_birthday"));
            setLogin_Button();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //find all views by id here
    private void initVariables() {
        profilePictureView = (ProfilePictureView)findViewById(R.id.currentProfilePicture);
        loginButton = (LoginButton)findViewById(R.id.btnLoginFB);
    }

    //control login
    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginSuccess();
            }

            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void loginSuccess() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.d("JSON", graphResponse.getJSONObject().toString());
                        try {
                            email=jsonObject.getString("email");
                            gender=jsonObject.getString("gender");
                            id = jsonObject.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle para = new Bundle();
        para.putString("fields","email,gender,birthday,id");
        graphRequest.setParameters(para);
        graphRequest.executeAsync();




        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Profile mProfile = Profile.getCurrentProfile();
        String firstName = mProfile.getFirstName();
        String lastName = mProfile.getLastName();
        String profileImageUrl = mProfile.getProfilePictureUri(96,96).toString();
        intent.putExtra(PROFILE_FIRST_NAME, firstName);
        intent.putExtra(PROFILE_LAST_NAME, lastName);
        intent.putExtra(PROFILE_IMAGE_URL, profileImageUrl);
        startActivity(intent);
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }
}

