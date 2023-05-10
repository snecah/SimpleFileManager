package com.example.simplefilemanager.model

import android.view.View
import com.example.simplefilemanager.R
import com.example.simplefilemanager.Utils
import com.example.simplefilemanager.databinding.FileItemBinding
import com.xwray.groupie.viewbinding.BindableItem


class FileItem(val fileData: FileData, val onDirectoryItemClicked: (String) -> Unit) :
    BindableItem<FileItemBinding>() {

    override fun bind(viewBindig: FileItemBinding, position: Int) {
        viewBindig.apply {
            if (fileData.fileName.isEmpty()) fileName.text = fileData.fileName
            lastModifiedDate.text = fileData.lastModifiedDate
            fileSize.text = if (fileData.isDirectory) "" else fileData.fileSize

            if (fileData.isDirectory) {
                fileImage.setImageResource(R.drawable.baseline_folder_24)
                fileItemBlock.setOnClickListener {
                    onDirectoryItemClicked(fileData.filePath)
                }
            } else {
                when (fileData.fileExtension) {
                    in Utils.videoExtensions -> fileImage.setImageResource(R.drawable.baseline_video_library_24)
                    Utils.pdfExtension -> fileImage.setImageResource(R.drawable.baseline_picture_as_pdf_24)
                    in Utils.audioExtensions -> fileImage.setImageResource(R.drawable.baseline_audiotrack_24)
                    in Utils.imageExtensions -> fileImage.setImageResource(R.drawable.baseline_photo_24)
                    in Utils.textExtensions -> fileImage.setImageResource(R.drawable.baseline_text_snippet_24)
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.file_item

    override fun initializeViewBinding(view: View): FileItemBinding = FileItemBinding.bind(view)

}