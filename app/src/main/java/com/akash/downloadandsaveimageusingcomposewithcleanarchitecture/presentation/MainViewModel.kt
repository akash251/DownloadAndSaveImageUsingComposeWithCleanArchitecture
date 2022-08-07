package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.domain.repository.ImageDownloadRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: ImageDownloadRepo
):ViewModel() {


    var state by mutableStateOf(MainScreenState())
        private set



    //for saving and downloading image on button click
    fun downloadAndSaveImage(imageUrl:String,context: Context){
        viewModelScope.launch {
            repo.downloadAndSaveImage(imageUrl, context)
                .collect{
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }



    //we are having a text field so to modify its value
    fun onTextFieldValueChange(value:String){
        viewModelScope.launch {
            state = state.copy(
                imageUrl = value
            )
        }
    }


}