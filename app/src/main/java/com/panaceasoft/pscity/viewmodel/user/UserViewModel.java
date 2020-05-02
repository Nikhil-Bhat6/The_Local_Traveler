package com.panaceasoft.pscity.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.user.UserRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Constants;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewobject.ApiStatus;
import com.panaceasoft.pscity.viewobject.User;
import com.panaceasoft.pscity.viewobject.UserLogin;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 12/12/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class UserViewModel extends ViewModel {


    //region Variables

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public boolean isLoading = false;
    public String profileImagePath = "";
    public User user;

    private final UserRepository repository;

    // for Login
    private final LiveData<Resource<UserLogin>> doUserLoginData;
    private MutableLiveData<User> doUserLoginObj = new MutableLiveData<>();

    // for get User
    private final LiveData<Resource<User>> userData;
    private MutableLiveData<String> userObj = new MutableLiveData<>();

    // for register
    private final LiveData<Resource<User>> registerUserData;
    private MutableLiveData<UserTmpDataHolder> registerUserObj = new MutableLiveData<>();

    // for register FB
    private final LiveData<Resource<UserLogin>> registerFBUserData;
    private MutableLiveData<TmpDataHolder> registerFBUserObj = new MutableLiveData<>();

    // for getting login user from db
    private final LiveData<List<UserLogin>> userLoginData;
    private MutableLiveData<String> userLoginObj = new MutableLiveData<>();

    // for update user
    private final LiveData<Resource<ApiStatus>> updateUserData;
    private MutableLiveData<User> updateUserObj = new MutableLiveData<>();

    // for forgot password
    private final LiveData<Resource<ApiStatus>> forgotpasswordData;
    private MutableLiveData<String> forgotPasswordObj = new MutableLiveData<>();

    // for password update
    private final LiveData<Resource<ApiStatus>> passwordUpdateData;
    private MutableLiveData<TmpDataHolder> passwordUpdateObj = new MutableLiveData<>();

    // for image upload
    private MutableLiveData<String> imgObj = new MutableLiveData<>();

    //endregion

    // for google login
    private final LiveData<Resource<UserLogin>> googleLoginData;
    private MutableLiveData<TmpDataHolder> googleLoginObj = new MutableLiveData<>();

    //for verification code
    private final LiveData<Resource<UserLogin>> verificationEmailData;
    private MutableLiveData<TmpDataHolder> verificationEmailObj = new MutableLiveData<>();

    //for resent verification code
    private final LiveData<Resource<Boolean>> resentVerifyCodeData;
    private MutableLiveData<UserViewModel.resentCodeTmpDataHolder> resentVerifyCodeObj = new MutableLiveData<>();

    // for phone login
    private final LiveData<Resource<UserLogin>> phoneLoginData;
    private MutableLiveData<TmpDataHolder> phoneLoginObj = new MutableLiveData<>();

    //region Constructor

    @Inject
    public UserViewModel(UserRepository repository) {

        this.repository = repository;

        // Login User
        doUserLoginData = Transformations.switchMap(doUserLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : doUserLoginData");
            return repository.doLogin(Config.API_KEY, obj.userEmail, obj.userPassword);
        });

        // Register User
        registerUserData = Transformations.switchMap(registerUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : registerUserData");
            return repository.registerUser(Config.API_KEY, obj.user.userId, obj.user.userName, obj.user.userEmail, obj.user.userPassword, obj.user.userPhone,obj.deviceToken);
        });

        // Register FB User
        registerFBUserData = Transformations.switchMap(registerFBUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : registerFBUserData");
            return repository.registerFBUser(Config.API_KEY, obj.fbId, obj.name, obj.email, obj.imageUrl);
        });

        // Get User Data
        userLoginData = Transformations.switchMap(userLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : userLoginData");
            return repository.getLoginUser();
        });

        // google login User
        googleLoginData = Transformations.switchMap(googleLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : googleLoginData");
            return repository.postGoogleLogin(Config.API_KEY, obj.googleId, obj.name, obj.email, obj.imageUrl, obj.deviceToken);
        });

        verificationEmailData = Transformations.switchMap(verificationEmailObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.verificationCodeForUser(obj.loginUserId, obj.code);
        });

        resentVerifyCodeData = Transformations.switchMap(resentVerifyCodeObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.resentCodeForUser(obj.userEmail);

        });

        // Get User Data
        userData = Transformations.switchMap(userObj, userId -> {
            if (userId == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : userLoginData");
            return repository.getUser(Config.API_KEY, userId);
        });

        // Update User
        updateUserData = Transformations.switchMap(updateUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : updateUserData");
            return repository.updateUser(Config.API_KEY, updateUserObj.getValue());
        });

        // Forgot Password
        forgotpasswordData = Transformations.switchMap(forgotPasswordObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : forgotPasswordData");
            return repository.forgotPassword(Config.API_KEY, obj);
        });

        // Password Update
        passwordUpdateData = Transformations.switchMap(passwordUpdateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : passwordUpdateData");
            return repository.passwordUpdate(Config.API_KEY, obj.loginUserId, obj.password);
        });

        // phone login User
        phoneLoginData = Transformations.switchMap(phoneLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("UserViewModel : phoneLoginData");
            return repository.postPhoneLogin(Config.API_KEY, obj.phoneId, obj.name, obj.phone, obj.deviceToken);
        });
    }

    //endregion


    //region Methods

    // For loading status
    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    // For Login User
    public void setUserLogin(User obj) {
        setLoadingState(true);
        this.doUserLoginObj.setValue(obj);
    }

    public LiveData<Resource<UserLogin>> getUserLoginStatus() {
        return doUserLoginData;
    }


    // For Getting Login User Data
    public LiveData<List<UserLogin>> getLoginUser() {
        userLoginObj.setValue("load");

        return userLoginData;
    }

    // For User Data
    public LiveData<Resource<User>> getUser(String userId) {
        userObj.setValue(userId);

        return userData;
    }


    // For Delete Login User
    public LiveData<Resource<Boolean>> deleteUserLogin(User user) {

        if (user == null) {
            return AbsentLiveData.create();
        }

        return this.repository.delete(user);
    }

    public void setEmailVerificationUser(String loginUserId, String code) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.loginUserId = loginUserId;
        tmpDataHolder.code = code;
        this.verificationEmailObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<UserLogin>> getEmailVerificationUser() {
        return verificationEmailData;
    }


    public void setResentVerifyCodeObj(String userEmail) {
        resentCodeTmpDataHolder tmpDataHolder = new resentCodeTmpDataHolder(userEmail);

        this.resentVerifyCodeObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getResentVerifyCodeData() {
        return resentVerifyCodeData;
    }



    // Upload Image
    public LiveData<Resource<User>> uploadImage(String filePath, String userId) {

        imgObj.setValue("PS");

        return Transformations.switchMap(imgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return this.repository.uploadImage(filePath, userId, Constants.PLATFORM);
        });

    }


    // Update User

    public void setUpdateUserObj(User user) {
        updateUserObj.setValue(user);
    }

    public LiveData<Resource<ApiStatus>> getUpdateUserData() {

        return updateUserData;
    }


    // Register User
//    public LiveData<Resource<UserLogin>> registerUser(User user) {
//        registerUserObj.setValue(user);
//        return registerUserData;
//    }
    public void setRegisterUser(User user) {
        UserTmpDataHolder tmpDataHolder = new UserTmpDataHolder();
        tmpDataHolder.user = user;

        this.registerUserObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<User>> getRegisterUser() {
        return registerUserData;
    }

    // Register User
    public void registerFBUser(String fbId, String name, String email, String imageUrl) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.fbId = fbId;
        tmpDataHolder.name = name;
        tmpDataHolder.email = email;
        tmpDataHolder.imageUrl = imageUrl;

        registerFBUserObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getRegisterFBUserData() {
        return registerFBUserData;
    }

    // Forgot password
    public LiveData<Resource<ApiStatus>> forgotPassword(String email) {
        forgotPasswordObj.setValue(email);
        return forgotpasswordData;
    }

    // Forgot password
    public LiveData<Resource<ApiStatus>> passwordUpdate(String loginUserId, String password) {

        TmpDataHolder holder = new TmpDataHolder();
        holder.loginUserId = loginUserId;
        holder.password = password;

        passwordUpdateObj.setValue(holder);
        return passwordUpdateData;
    }

    // phone login User
    public void setGoogleLoginUser(String googleId, String name, String email,String imageUrl, String deviceToken) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.googleId = googleId;
        tmpDataHolder.name = name;
        tmpDataHolder.email = email;
        tmpDataHolder.imageUrl = imageUrl;
        tmpDataHolder.deviceToken = deviceToken;
        googleLoginObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getGoogleLoginData() {
        return googleLoginData;
    }

    // phone login User
    public void setPhoneLoginUser(String phoneId, String name, String phone, String deviceToken) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.phoneId = phoneId;
        tmpDataHolder.name = name;
        tmpDataHolder.phone = phone;
        tmpDataHolder.deviceToken = deviceToken;
        phoneLoginObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<UserLogin>> getPhoneLoginData() {
        return phoneLoginData;
    }



    //region Tmp Holder

    class TmpDataHolder {

        public String loginUserId = "";
        public String password = "";
        public String fbId = "";
        public String name = "";
        public String email = "";
        public String imageUrl = "";
        public String googleId = "";
        public String deviceToken = "";
        public String phoneId = "";
        public String phone = "";
        public String code = "";

    }

    class UserTmpDataHolder {
        public User user;
        public String deviceToken = "";

    }

    private class resentCodeTmpDataHolder {

        public String userEmail;

        private resentCodeTmpDataHolder(String userEmail) {
            this.userEmail = userEmail;
        }
    }

    //endregion

}
