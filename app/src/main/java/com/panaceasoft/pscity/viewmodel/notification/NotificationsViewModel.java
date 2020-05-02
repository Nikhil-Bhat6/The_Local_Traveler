package com.panaceasoft.pscity.viewmodel.notification;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.notification.NotificationRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.Noti;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class NotificationsViewModel extends PSViewModel {

    //for recent comment list
    private final LiveData<Resource<List<Noti>>> notificationListData;
    private MutableLiveData<NotificationsViewModel.TmpDataHolder> notificationListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNotificationLoadingData;
    private MutableLiveData<NotificationsViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Noti>> notificationDetailListData;
    private MutableLiveData<NotificationsViewModel.TmpDataHolder> notificationDetailObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> notiPostData;
    private MutableLiveData<NotificationsViewModel.TmpDataHolder> notiPostObj = new MutableLiveData<>();

    public String token = "";
    public String notiId = "";

    @Inject
    public NotificationsViewModel(NotificationRepository notificationRepository) {

        notificationListData = Transformations.switchMap(notificationListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Notification List.");
            return notificationRepository.getNotificationList(Config.API_KEY, obj.userId, obj.limit, obj.offset, obj.deviceToken);
        });

        nextPageNotificationLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Notification List.");
            return notificationRepository.getNextPageNotificationList(obj.userId, obj.deviceToken, obj.limit, obj.offset);
        });

        notificationDetailListData = Transformations.switchMap(notificationDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Notification detail List.");
            return notificationRepository.getNotificationDetail(Config.API_KEY, obj.notificationId);
        });

        notiPostData = Transformations.switchMap(notiPostObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Notification detail List.");
            return notificationRepository.uploadNotiPostToServer(obj.notificationId, obj.userId, obj.deviceToken);
        });


    }
    //endregion

    //region Getter And Setter for Comment List

    public void setNotificationListObj(String userId, String deviceToken, String limit, String offset) {
        if (!isLoading) {
            NotificationsViewModel.TmpDataHolder tmpDataHolder = new NotificationsViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.userId = userId;
            tmpDataHolder.deviceToken = deviceToken;
            notificationListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Noti>>> getNotificationListData() {
        return notificationListData;
    }

    //Get Comment Next Page
    public void setNextPageLoadingStateObj(String userId, String deviceToken, String limit, String offset) {

        if (!isLoading) {
            NotificationsViewModel.TmpDataHolder tmpDataHolder = new NotificationsViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.userId = userId;
            tmpDataHolder.deviceToken = deviceToken;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageNotificationLoadingData;
    }

    //endregion

    //region Getter And Setter for product detail List

    public void setNotificationDetailObj(String notificationId) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.notificationId = notificationId;
            notificationDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Noti>> getNotificationDetailData() {
        return notificationDetailListData;
    }
    //endregion

    //region Getter And Setter for noti post

    public void setNotiReadObj(String notificationId, String userId, String deviceToken) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.notificationId = notificationId;
        tmpDataHolder.userId = userId;
        tmpDataHolder.deviceToken = deviceToken;
        notiPostObj.setValue(tmpDataHolder);

        // start loading
        setLoadingState(true);
    }

    public LiveData<Resource<Boolean>> getNotiReadData() {
        return notiPostData;
    }
    //endregion


    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public Boolean isConnected = false;
        public String notificationId = "";
        public String userId = "";
        public String deviceToken = "";
    }


}
