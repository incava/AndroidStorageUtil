package com.incava.androidstorageutil.util

import android.net.Uri
import android.os.Environment
import java.io.File

class ExternalFilePath {
    /**
     * FilePath를 가져오고 싶을 때 사용 할 클래스
     */
    companion object {
        fun getExternalFilePath(folderName: String, fileName: String): Uri {
            // 파일 경로를 가져오는 메서드
            // folderName과 fileName을 파라미터로 보내주고 Uri를 가져옵니다.
            val filePath = File(
                    Environment.getExternalStorageDirectory().absolutePath + "/$folderName",
                    fileName
                )
            return Uri.fromFile(filePath)
        }
    }


}