package com.example.simplefilemanager.ui

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplefilemanager.model.groupieItems.FileItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

class FilesListViewModel : ViewModel() {
    private val _fileGroupiesItems = MutableLiveData<List<FileItem>>()
    val fileGroupiesItems: LiveData<List<FileItem>> = _fileGroupiesItems
    lateinit var selectedFilePath: String

    val onDirectoryItemClickedEvent = Channel<String>()


    fun fetchContent(path: String?) {
        if (path == null) {
            val rootDir = Environment.getExternalStorageDirectory()
            _fileGroupiesItems.value =
                rootDir.listFiles()?.map { FileItem(it, onDirectoryItemClicked()) }
        } else {
            val file = File(path)
            _fileGroupiesItems.value =
                file.listFiles()?.map { FileItem(it, onDirectoryItemClicked()) }
        }
    }

    private fun onDirectoryItemClicked(): (File) -> Unit = { selectedFile ->
        selectedFilePath = selectedFile.absolutePath
        viewModelScope.launch {
            onDirectoryItemClickedEvent.send(selectedFilePath)
        }
    }
}