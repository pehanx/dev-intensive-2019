package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import ru.skillbranch.devintensive.ui.custom.CircleImageView

class ProfileViewModel:ViewModel(){
    private val repository:PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val circleImageViewData = MutableLiveData<CircleImageView>()
    private val appTheme = MutableLiveData<Int>()
    
    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getProfileData():LiveData<Profile> = profileData

    fun getTheme():LiveData<Int> = appTheme

    fun saveProfileData(profile: Profile){
        repository.saveProfile(profile)
        circleImageViewData.value?.invalidate()
        profileData.value = profile
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES){
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }else{
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }
}