package org.izv.flora.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.izv.flora.model.Repository;
import org.izv.flora.model.entity.ImagesRowsResponse;

public class GetImagesViewModel extends AndroidViewModel {

    Repository repository;

    public GetImagesViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<ImagesRowsResponse> getFloraImagesLiveData() {
        return repository.getFloraImagesLiveData();
    }

    public void getImages(long idflora) {
        repository.getImages(idflora);
    }
}
