package com.example.simplefilemanager.ui

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplefilemanager.Utils
import com.example.simplefilemanager.model.FileData
import com.example.simplefilemanager.model.FileItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

class FilesListViewModel : ViewModel() {
    private val _fileGroupiesItems = MutableLiveData<List<FileItem>>()
    val fileGroupiesItems: LiveData<List<FileItem>> = _fileGroupiesItems

    private var isSortedByAscending: Boolean = false


    val onDirectoryItemClickedEvent = Channel<String>()

    fun fetchContent(path: String?) {
        if (path == null) {
            val rootDir = Environment.getExternalStorageDirectory()
            _fileGroupiesItems.value = rootDir.listFiles()?.map {
                FileItem(
                    FileData(
                        it.name,
                        Utils.getSize(it.length()),
                        it.extension,
                        Utils.formatDate(it.lastModified()),
                        it.absolutePath,
                        it.isDirectory
                    ), onDirectoryItemClicked()
                )
            }
        } else {
            val file = File(path)
            if(file.listFiles().isNullOrEmpty()) {
            }
            _fileGroupiesItems.value = file.listFiles()?.map {
                FileItem(
                    FileData(
                        it.name,
                        Utils.getSize(it.length()),
                        it.extension,
                        Utils.formatDate(it.lastModified()),
                        it.absolutePath,
                        it.isDirectory
                    ), onDirectoryItemClicked()
                )
            }
        }
    }

    private fun onDirectoryItemClicked(): (String) -> Unit = { selectedFilePath ->
        viewModelScope.launch {
            onDirectoryItemClickedEvent.send(selectedFilePath)
        }
    }

    fun sortBySize() {
        viewModelScope.launch(Dispatchers.Default) {
            isSortedByAscending = if (isSortedByAscending) {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.bySizeComparator)?.reversed()
                )
                false
            } else {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.bySizeComparator)
                )
                true
            }
        }
    }

    fun sortByDate() {
        viewModelScope.launch(Dispatchers.Default) {
            isSortedByAscending = if (isSortedByAscending) {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.byDateComparator)?.reversed()
                )
                false
            } else {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.byDateComparator)
                )
                true
            }
        }
    }

    fun sortByExtension() {
        viewModelScope.launch(Dispatchers.Default) {
            isSortedByAscending = if (isSortedByAscending) {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.byExtensionComparator)?.reversed()
                )
                false
            } else {
                _fileGroupiesItems.postValue(
                    _fileGroupiesItems.value?.sortedWith(Utils.byExtensionComparator)
                )
                true
            }
        }
    }
}