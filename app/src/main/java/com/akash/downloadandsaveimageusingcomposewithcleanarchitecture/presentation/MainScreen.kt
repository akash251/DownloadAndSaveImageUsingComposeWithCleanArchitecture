package com.akash.downloadandsaveimageusingcomposewithcleanarchitecture.presentation

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    
    val state = viewModel.state
    val context = LocalContext.current
    
    // lets handle runtime permission
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    
    SideEffect {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }

    }
    
    
    //we will just have column with text field and button
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        OutlinedTextField(
            value = state.imageUrl,
            onValueChange = {
                viewModel.onTextFieldValueChange(it)
            },
            
            label = {
                Text(text = "Enter image url")
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(onClick = { 
            viewModel.downloadAndSaveImage(state.imageUrl,context)
        }) {
            Text(text = "Download")
        }
        
    }
    
}