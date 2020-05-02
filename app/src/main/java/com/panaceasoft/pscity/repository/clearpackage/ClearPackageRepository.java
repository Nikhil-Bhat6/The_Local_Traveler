package com.panaceasoft.pscity.repository.clearpackage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.panaceasoft.pscity.AppExecutors;
import com.panaceasoft.pscity.api.PSApiService;
import com.panaceasoft.pscity.db.PSCoreDb;
import com.panaceasoft.pscity.repository.common.PSRepository;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewobject.common.Resource;

import javax.inject.Inject;

public class ClearPackageRepository extends PSRepository {

    @Inject
    ClearPackageRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside CityCategoryRepository");
    }

    public LiveData<Resource<Boolean>> clearAllTheData() {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                db.runInTransaction(() -> {

                    db.cityDao().deleteAll();
                    db.itemDao().deleteAll();
                    db.blogDao().deleteAll();
                    db.commentDao().deleteAll();
                    db.commentDetailDao().deleteAll();
                    db.deletedObjectDao().deleteAll();
                    db.imageDao().deleteTable();
                    db.notificationDao().deleteAllNotificationList();
                    db.specsDao().deleteAll();
                    db.psAppInfoDao().deleteAll();
                    db.psAppVersionDao().deleteAll();
                    db.ratingDao().deleteAll();

                });
            } catch (NullPointerException ne) {
                Utils.psErrorLog("Null Pointer Exception : ", ne);

                statusLiveData.postValue(Resource.error(ne.getMessage(), false));
            } catch (Exception e) {
                Utils.psErrorLog("Exception : ", e);

                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }

            statusLiveData.postValue(Resource.success(true));


        });

        return statusLiveData;
    }

}
