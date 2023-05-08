package com.example.simplefilemanager.model.groupieItems

import android.content.Intent
import android.net.Uri
import android.view.View
import com.example.simplefilemanager.R
import com.example.simplefilemanager.databinding.FileItemBinding
import com.xwray.groupie.viewbinding.BindableItem
import java.io.File


class FileItem(val file: File, val onDirectoryItemClicked: (File) -> Unit) : BindableItem<FileItemBinding>() {

    override fun bind(viewBindig: FileItemBinding, position: Int) {
        viewBindig.apply {
            fileName.text = file.name
            fileType.text = file.extension
            fileSize.text = file.length().toDouble().toString()

            if(file.isDirectory) {
                fileImage.setImageResource(R.drawable.baseline_folder_24)
                fileImage.setOnClickListener {
                    onDirectoryItemClicked(file)
                }
            } else {
                fileImage.setOnClickListener {
                    val context = fileImage.context
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(file.absolutePath))
                    val path = Uri.fromFile(file)
                    intent.setDataAndType(path, "application/pdf")
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.file_item

    override fun initializeViewBinding(view: View): FileItemBinding  = FileItemBinding.bind(view)

}