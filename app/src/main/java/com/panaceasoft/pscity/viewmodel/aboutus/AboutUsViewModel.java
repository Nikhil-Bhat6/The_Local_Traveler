package com.panaceasoft.pscity.viewmodel.aboutus;

import com.panaceasoft.pscity.Config;
import com.panaceasoft.pscity.repository.aboutus.AboutUsRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.AboutUs;
import com.panaceasoft.pscity.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Panacea-Soft on 12/30/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class AboutUsViewModel extends PSViewModel {


    //region Variables

    // Get AboutUs
    private final LiveData<Resource<AboutUs>> aboutUsLiveData;
    private MutableLiveData<String> aboutUsObj = new MutableLiveData<>();

    public String aboutId;
    //endregion


    //region Constructors

    @Inject
    AboutUsViewModel(AboutUsRepository repository) {

        aboutUsLiveData = Transformations.switchMap(aboutUsObj, newsId -> {
            if (newsId.isEmpty()) {
                return AbsentLiveData.create();
            }
            return repository.getAboutUs(Config.API_KEY);
        });

    }

    //endregion


    //region Public Methods

    public void setAboutUsObj(String aboutUsObj) {
        this.aboutUsObj.setValue(aboutUsObj);
    }

    public LiveData<Resource<AboutUs>> getAboutUsData() {
        return aboutUsLiveData;
    }

    //endregion

}
