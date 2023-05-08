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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplefilemanager.databinding.ActivityMainBinding
import com.example.simplefilemanager.model.groupieItems.FileItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.io.File

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    private val rootDir: File = Environment.getExternalStorageDirectory()
    private val adapter1 by lazy { GroupAdapter<GroupieViewHolder>() }

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
            Toast.makeText(this, "perm granted first time", Toast.LENGTH_LONG).show()

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager())
                showAndroid11PlusPermissionDialog()
            // Permission has already been granted
            Toast.makeText(this, "all permission granted", Toast.LENGTH_LONG).show()
        }

        with(binding) {
            recyclerView.adapter = adapter1
        }
        showFilesInDirectory(rootDir)
    }


    private fun showFilesInDirectory(rootDir: File) {
        if (rootDir.listFiles().isNullOrEmpty()) {
            val fileItemList = rootDir.listFiles()!!.map { FileItem(it, onDirectoryItemClicked()) }
            adapter1.update(fileItemList)
            Toast.makeText(applicationContext, "empty directory", Toast.LENGTH_LONG).show()
        } else {
            val fileItemList = rootDir.listFiles()!!.map { FileItem(it, onDirectoryItemClicked()) }
            adapter1.update(fileItemList)
        }
    }


    private fun onDirectoryItemClicked(): (File) -> Unit = { selectedDirectory ->
        showFilesInDirectory(selectedDirectory)
    }

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