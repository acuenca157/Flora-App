package org.izv.flora.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.izv.flora.model.Repository;
import org.izv.flora.model.entity.Flora;

public class EditFloraViewModel extends AndroidViewModel {

    Repository repository;

    public EditFloraViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<Boolean> getEditFloraLiveData() {
        return repository.getEditFloraLiveData();
    }

    public void editFlora(long id, Flora flora) {
        repository.editFlora(id, flora);
    }
}
