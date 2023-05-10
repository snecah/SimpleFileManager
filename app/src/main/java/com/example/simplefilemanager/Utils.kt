package com.example.simplefilemanager

import com.example.simplefilemanager.model.FileData
import com.example.simplefilemanager.model.FileItem
import java.io.File
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.Comparator
import java.util.Locale

class Utils {
    companion object {
        private const val FORMAT_DATE_PATTERN = "dd/MM/yyyy HH:mm"
        val videoExtensions = listOf<String>(
            "3gp",
            "3gp",
            "3gpp2",
            "asf",
            "avi",
            "dv",
            "m2t",
            "m4v",
            "mkv",
            "mov",
            "mp4",
            "mpeg",
            "mpg",
            "mts",
            "oggtheora",
            "ogv",
            "rm",
            "ts",
            "vob",
            "webm",
            "wmv",
            "flv"
        )
        val c = listOf("sdf", "Sd").joinToString("")
        val audioExtensions = listOf("aac", "flac", "m4a", "mp3", "oga", "wav", "wma")
        val textExtensions = listOf("txt", "csv", "rtf", "odt", "md", "doc", "docx")
        val imageExtensions = listOf("bmp", "gif", "jpeg", "jpg", "png", "tif", "tiff", "svg")
        const val pdfExtension = "pdf"


        val bySizeComparator = Comparator { f1: FileItem, f2: FileItem ->
            compareSizes(f1.fileData, f2.fileData)
        }

        private fun compareSizes(file1: FileData, file2: FileData): Int {
            val fileSize1 = file1.fileSize
            val fileSize2 = file2.fileSize

            if (file1.isDirectory) {
                return if (file2.isDirectory) 0
                else -1
            } else {
                if (file2.isDirectory) return 1
                else {
                    if ("MB" in fileSize1) {
                        if ("MB" in fileSize2) {
                            for (i in 0..fileSize1.length - 3) {
                                if (fileSize1[i].code > fileSize2[i].code) return 1
                                if (fileSize1[i].code < fileSize2[i].code) return -1
                            }
                            return 0
                        } else return 1
                    }

                    if ("KB" in fileSize1) {
                        if ("KB" in fileSize2) {
                            for (i in 0..fileSize1.length - 3) {
                                if (fileSize1[i].code > fileSize2[i].code) return 1
                                if (fileSize1[i].code < fileSize2[i].code) return -1
                            }
                            return 0
                        } else return -1
                    }
                    return 1
                }
            }
        }

        val byExtensionComparator = Comparator { f1: FileItem, f2: FileItem ->
            compareExtensions(f1.fileData, f2.fileData)
        }

        private fun compareExtensions(file1: FileData, file2: FileData): Int {
            val fileExtension1 = file1.fileExtension
            val fileExtension2 = file2.fileExtension

            if (file1.isDirectory) {
                return if (file2.isDirectory) 0
                else -1
            } else {
                if (file2.isDirectory) return 1
                else {
                    val minLength = min(fileExtension1.length, fileExtension2.length)
                    for (i in 0 until minLength) {
                        if (fileExtension1[i] > fileExtension2[i]) return 1
                        if (fileExtension1[i] < fileExtension2[i]) return -1
                    }
                    return if (fileExtension1.length > fileExtension2.length) 1
                    else -1
                }
            }
        }

        val byDateComparator = Comparator { f1: FileItem, f2: FileItem ->
            compareDates(f1.fileData.lastModifiedDate, f2.fileData.lastModifiedDate)
        }

        private fun compareDates(Date1: String, Date2: String): Int {
            // split return [dd, MM, yyyy, HH, mm]
            val splittedDate1 = Date1.split(" ", "/", ":")
            val splittedDate2 = Date2.split(" ", "/", ":")

            val date1InNumber =
                splittedDate1[2] + splittedDate1[1] + splittedDate1[0] + splittedDate1[3] + splittedDate1[4]
            val date2InNumber =
                splittedDate2[2] + splittedDate2[1] + splittedDate2[0] + splittedDate2[3] + splittedDate2[4]

            for (i in date1InNumber.indices) {
                if (date1InNumber[i].code > date2InNumber[i].code) return 1
                if (date1InNumber[i].code < date2InNumber[i].code) return -1
            }
            return 0
        }


        fun formatDate(date: Long): String {
            val formatter = SimpleDateFormat(FORMAT_DATE_PATTERN, Locale.US)
            return formatter.format(date)
        }

        fun getSize(length: Long): String {
            val fileSizeInKB = length / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            return if (fileSizeInMB == 0L) "${fileSizeInKB}KB"
            else "${fileSizeInMB}MB"
        }
    }
}