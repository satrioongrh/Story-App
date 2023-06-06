package com.example.mystoryapp2.data.viewmodel


import android.content.Context
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.mystoryapp2.data.api.ApiConfig
import com.example.mystoryapp2.data.model.AddStoryResponse
import com.example.mystoryapp2.data.model.ListStoryItem
import com.example.mystoryapp2.data.model.StoryListResponse
import com.example.mystoryapp2.view.utils.Constant
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccesUpStory = MutableLiveData<Boolean>()
    val isSuccesUpStory: LiveData<Boolean> = _isSuccesUpStory

    private val _storyList = MutableLiveData<List<ListStoryItem>?>()
    val storyList: LiveData<List<ListStoryItem>?> = _storyList

    val error = MutableLiveData<String>()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    //maps
    private val _isStoryPicked = MutableLiveData<Boolean>()
    val isStoryPicked: LiveData<Boolean> = _isStoryPicked

    private val _corLat = MutableLiveData<Long>()
    val corLat: LiveData<Long> = _corLat

    private val _corLng = MutableLiveData<Long>()
    val corLng: LiveData<Long> = _corLng

    val corTemp = MutableLiveData(Constant.indonesiaLocation)

    companion object {
        private val TAG = AuthViewModel::class.simpleName
    }

    fun loadStoryLocationData(context: Context, token: String){
        val client = ApiConfig.getApiService().getStoryListLocation(token, 100)
        client.enqueue(object : Callback<StoryListResponse>{
            override fun onResponse(
                call: Call<StoryListResponse>,
                response: Response<StoryListResponse>
            ) {
                if (response.isSuccessful) {
                    _isError.postValue(false)
                    _storyList.postValue(response.body()?.listStory)
                } else {
                    _isError.postValue(true)
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
                Log.e(Constant.TAG_STORY, "onFailure Call: ${t.message}")
                error.postValue("error fetch data : ${t.message}")
            }

        })
    }

    fun loadData(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoryList(token, 30)
        client.enqueue(object : Callback<StoryListResponse> {
            override fun onResponse(
                call: Call<StoryListResponse>,
                response: Response<StoryListResponse>
            ) {
                if (response.isSuccessful) {
                    _storyList.postValue(response.body()!!.listStory as List<ListStoryItem>?)
                } else {
                    error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure Call: ${t.message}")
                error.postValue("error fetch data : ${t.message}")
            }

        })
    }

    fun addStory(token: String, image: File, description: String) {
        _isLoading.value = true
        "${image.length() / 1024 / 1024} MB" // manual parse from bytes to Mega Bytes
        val storyDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            requestImageFile
        )
        val client = ApiConfig.getApiService().uploadStory(token, imageMultipart, storyDescription)
            .enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    when (response.code()) {
                        401 -> error.postValue("invalid token")
                        413 -> error.postValue("maximum image is 1 MB")
                        201 -> _isSuccesUpStory.postValue(true)
                        else -> error.postValue("Error ${response.code()} : ${response.message()}")
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure Call: ${t.message}")
                    error.postValue("error sending image : ${t.message}")
                }

            })
    }


}