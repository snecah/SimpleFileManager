package com.example.simplefilemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.simplefilemanager.ui.FilesListFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val rootDir: File = Environment.getExternalStorageDirectory()

    companion object {
        private const val READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 1
    }

    override fun onStart() {
        super.onStart()
        if (checkReadExternalStoragePermission()) {
            // Permission is not granted, request to
            PermissionsManager().requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())
                showAndroid11PlusPermissionDialog()
            // Permission has already been granted
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = FilesListFragment.newInstance()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


//    private fun showFilesInDirectory(rootDir: File) {
//        if (rootDir.listFiles().isNullOrEmpty()) {
//            val fileItemList = rootDir.listFiles()!!.map { FileItem(it, onDirectoryItemClicked()) }
//            adapter.update(fileItemList)
//            Toast.makeText(applicationContext, "empty directory", Toast.LENGTH_LONG).show()
//        } else {
//            val fileItemList = rootDir.listFiles()!!.map { FileItem(it, onDirectoryItemClicked()) }
//            adapter.update(fileItemList)
//        }
//    }


//    private fun onDirectoryItemClicked(): (File) -> Unit = { selectedDirectory ->
//        showFilesInDirectory(selectedDirectory)
//    }

    private fun checkReadExternalStoragePermission(): Boolean {
        return (PermissionsManager().checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val android11PlusSettingResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                // Permission Granted for android 11+
            } else {
                // Permission not granted
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                        showAndroid11PlusPermissionDialog()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showAndroid11PlusPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("perm for 11+")
            .setMessage("Need more perm")
            .setPositiveButton("Take it") { dialog, _ ->
                val intent = Intent().apply {
                    action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    data = Uri.fromParts("package", applicationContext.packageName, null)
                }
                dialog.dismiss()
                android11PlusSettingResultLauncher.launch(intent)
            }
            .setNegativeButton("ne dam") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}