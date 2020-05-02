package com.panaceasoft.pscity.viewmodel.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.city.CityRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.City;
import com.panaceasoft.pscity.viewobject.common.Resource;

import javax.inject.Inject;

public class CityViewModel extends PSViewModel {

    private final LiveData<Resource<City>> cityInfoData;
    private MutableLiveData<CityTmpDataHolder> cityInfoObj = new MutableLiveData<>();

    public String selectedCityId = "";
    public String cityName = "";
    public String lat,lng = "";


    @Inject
    CityViewModel(CityRepository repository) {

        //region Getting City List

        cityInfoData = Transformations.switchMap(cityInfoObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getCityInfo(obj.apiKey);

        });


    }

    //region Getting City Info

    public void setCityInfoObj() {
        CityTmpDataHolder tmpDataHolder = new CityTmpDataHolder(Config.API_KEY);

        this.cityInfoObj.setValue(tmpDataHolder);

        setLoadingState(true);
    }

    public LiveData<Resource<City>> getCityInfoData() {
        return cityInfoData;
    }

    //endregion

    class CityTmpDataHolder {
        String apiKey;

        CityTmpDataHolder(String apiKey) {
            this.apiKey = apiKey;

        }
    }
}
