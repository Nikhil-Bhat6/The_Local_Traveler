package com.panaceasoft.pscity.viewmodel.image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.pscity.repository.image.ImageRepository;
import com.panaceasoft.pscity.utils.AbsentLiveData;
import com.panaceasoft.pscity.viewmodel.common.PSViewModel;
import com.panaceasoft.pscity.viewobject.Image;
import com.panaceasoft.pscity.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 12/8/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ImageViewModel extends PSViewModel {


    //region Variables

    // Get Image Video List
    private final LiveData<Resource<List<Image>>> imageListLiveData;
    private MutableLiveData<TmpDataHolder> imageParentObj = new MutableLiveData<>();

    //Image list show in item upload after image upload
    private final LiveData<Resource<List<Image>>> imageListByIdLiveData;
    private MutableLiveData<ImageViewModel.listTmpHolder2> imageListByIdObj = new MutableLiveData<>();

    //Image List next page
    private final LiveData<Resource<Boolean>> imageNextListByIdLiveData;
    private MutableLiveData<ImageViewModel.listTmpHolder> imageNextListByIdObj = new MutableLiveData<>();

    //Deleting Image
    private final LiveData<Resource<Boolean>> deleteImageData;
    private MutableLiveData<ImageViewModel.deleteImageTmp> deleteImageObj = new MutableLiveData<>();


    public String id;
    public List<Image> newsImageList;
    public String imgId;
    public String imgType;
    public String item_id;
    //endregion


    //region Constructors

    @Inject
    ImageViewModel(ImageRepository repository) {

        imageListLiveData = Transformations.switchMap(imageParentObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getImageList(obj.imgType, obj.imageParentId);
        });

        imageListByIdLiveData = Transformations.switchMap(imageListByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getImagesFromServer(obj.itemId, obj.limit, obj.offset,obj.lim);
        });

        deleteImageData = Transformations.switchMap(deleteImageObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.deleteImage(obj.itemId,obj.imageId);
        });
        imageNextListByIdLiveData = Transformations.switchMap(imageNextListByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextImagesFromServer(obj.itemId, obj.limit, obj.offset);
        });
    }

    //endregion


    //region Methods

    public void setImageParentId(String imageType, String imageParentId) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(imageType, imageParentId);
        this.imageParentObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Image>>> getImageListLiveData() {
        return imageListLiveData;
    }


    //region image list show in item upload after image upload
    public LiveData<Resource<List<Image>>> getImageListByIdLiveData()
    {
        return imageListByIdLiveData;
    }

    public void setImageListByIdObj(String itemId, String limit, String offset,String lim)
    {if(!isLoading) {
        setLoadingState(true);
        ImageViewModel.listTmpHolder2 tmpHolder2 = new ImageViewModel.listTmpHolder2(itemId, limit, offset, lim);

        imageListByIdObj.setValue(tmpHolder2);
    }
    }
    //endregion

    //region image next page
    public void setNextImageListByIdObj(String itemId, String limit, String offset)
    {
        if(!isLoading) {
            setLoadingState(true);
            ImageViewModel.listTmpHolder tmpHolder = new ImageViewModel.listTmpHolder(itemId, limit, offset);

            imageNextListByIdObj.setValue(tmpHolder);
        }
    }

    public LiveData<Resource<Boolean>> getNextImageListByIdLiveData()
    {
        return imageNextListByIdLiveData;
    }
    //endregion

    //region Delete image
    public void setDeleteImageObj(String itemId, String imageId)
    {
        ImageViewModel.deleteImageTmp tmp = new ImageViewModel.deleteImageTmp(itemId,imageId);

        deleteImageObj.setValue(tmp);
    }

    public LiveData<Resource<Boolean>> getDeleteImageData(){
        return deleteImageData;
    }
    //endregion

    class TmpDataHolder {
        String imgType ;
        String imageParentId ;

        public TmpDataHolder(String imgType, String imageParentId) {
            this.imgType = imgType;
            this.imageParentId = imageParentId;
        }
    }

    class listTmpHolder {
        String itemId , limit , offset;

        private listTmpHolder(String itemId, String limit, String offset) {
            this.itemId = itemId;
            this.limit = limit;
            this.offset = offset;
        }
    }

    class listTmpHolder2 {
        String itemId, limit, offset, lim;

        private listTmpHolder2(String itemId, String limit, String offset,String lim) {
            this.itemId = itemId;
            this.limit = limit;
            this.offset = offset;
            this.lim = lim;
        }
    }

    class deleteImageTmp{
        protected String itemId,imageId;

        private deleteImageTmp(String itemId, String imageId) {
           this.itemId = itemId;
           this.imageId = imageId;
        }
    }

    //endregion

}
