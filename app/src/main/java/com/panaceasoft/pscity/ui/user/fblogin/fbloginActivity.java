//package com.panaceasoft.mokets.ui.user.fblogin;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.TextView;
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.LoggingBehavior;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.login.widget.ProfilePictureView;
//import com.panaceasoft.mokets.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class fbloginActivity extends AppCompatActivity {
//
//    TextView Name,ID,Birthday;//link;
//    LoginButton loginButton;
//    CallbackManager callbackManager;
////    JSONObject jsonObject;
//    private ProfilePictureView profilePictureView;
////    ConstraintLayout info;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        try {
////            PackageInfo info = getPackageManager().getPackageInfo(
////                    "com.panaceasoft.mokets",
////                    PackageManager.GET_SIGNATURES);
////            for (Signature signature : info.signatures) {
////                MessageDigest md = MessageDigest.getInstance("SHA");
////                md.update(signature.toByteArray());
////                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
////            }
////        } catch (PackageManager.NameNotFoundException e) {
////
////        } catch (NoSuchAlgorithmException e) {
////
////        }
//
//
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//
//        callbackManager = CallbackManager.Factory.create();
//
//        setContentView(R.layout.activity_fblogin);
//        profilePictureView = findViewById(R.id.image);
//        Name = findViewById(R.id.NAME);
//        ID =findViewById(R.id.ID);
//        Birthday = findViewById(R.id.birthday);
//        //link = findViewById(R.id.link);
//        //info = findViewById(R.id.container);
//
//
//        loginButton = (LoginButton)findViewById(R.id.login_button);
//        //loginButton.setReadPermissions(Arrays.asList("email,name,id"));
//
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                Log.d("aaaaaaa", loginResult.getAccessToken().toString());
//
//                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
//                        (object, response) -> {
//                            //parseJSON(object);
//
//                            try {
//                                if(object != null) {
//                                    Log.d("aaaaaaa", object.toString());
//                                }else {
//                                    Log.d("aaaaaa","Object is null");
//                                }
//                                Name.setText(object.getString("email"));
//                                ID.setText(object.getString("name"));
//                                profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//                                profilePictureView.setProfileId(object.getString("id"));
//                                Birthday.setText(object.getString("id"));
//                                //link.setText(object.getString("link"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "email,name,id");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Name.setText("Login attempt canceled.");
//
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                Name.setText("Login attempt failed.");
//            }
//
//
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//}
