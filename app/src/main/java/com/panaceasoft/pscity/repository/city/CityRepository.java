package com.panaceasoft.pscity.repository.city;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.pscity.AppExecutors;
import com.panaceasoft.pscity.api.ApiResponse;
import com.panaceasoft.pscity.api.PSApiService;
import com.panaceasoft.pscity.db.PSCoreDb;
import com.panaceasoft.pscity.repository.common.NetworkBoundResource;
import com.panaceasoft.pscity.repository.common.PSRepository;
import com.panaceasoft.pscity.utils.Utils;
import com.panaceasoft.pscity.viewobject.City;
import com.panaceasoft.pscity.viewobject.common.Resource;

import javax.inject.Inject;

public class CityRepository extends PSRepository {

    @Inject
    protected SharedPreferences pref;

    @Inject
    public CityRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside CityRepository");
    }

    public LiveData<Resource<City>> getCityInfo(String apiKey) {

        return new NetworkBoundResource<City, City>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull City city) {
                Utils.psLog("SaveCallResult of getCityList.");

                try {
                    db.runInTransaction(() -> {
                        db.cityDao().insert(city);

                    });

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getCityList.", e);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable City data) {

                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<City> loadFromDb() {
                Utils.psLog("getCityList From Db");

                return db.cityDao().getCityInfo();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<City>> createCall() {
                Utils.psLog("Call API Service to getCityList.");

                return psApiService.getCityInfo(apiKey);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getCityList) : " + message);
            }
        }.asLiveData();
    }

}
