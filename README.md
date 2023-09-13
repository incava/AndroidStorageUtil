# AndroidStorageUtil


# 안드로이드 외부 저장소 관리

## 프레임 워크

---

프레임 워크 (ex: flutter ,android, uiKit 등) : Android

## 언어

---

언어(Dart,kotlin) : Kotlin

## 사용 라이브러리

---

없음

## 설명

---

안드로이드의 외부 저장소의 파일을 불러와 사용하기 위한 기능 구현 템플릿입니다.

외부 저장소란 앱 내의 저장소가 아닌, 휴대폰 갤러리 및 다운로드폴더, 자신이 직접 만든 폴더 등 휴대폰 안에 있는 저장소를 의미합니다.  

33버전까지의 버전도 가능하도록 설계가 되어있으며, 만약 파일을 불러오고 싶다면, 템플릿을 보시면서 이해와 적용이 가능하도록 쉽게 설계가 되어있습니다.

## 사용 예

---

의뢰사와 소통 시 매번 앱 사진을 변경해서 주기는 매우 힘든 작업입니다. 

그렇기에 어떤 사진을 넣을지는 의뢰사가 직접 넣어보면서 확인하는 것이 좋습니다. 

제가 사용한 프로젝트는 키오스크에서 사진이 어떻게 나오는지 미리 보여드릴 수 있도록 데모 버전에서 사용하였습니다. 

이 기능을 사용한 예시 스크린샷 입니다. ( 소스에는 관련 UI가 없이 기능만 있습니다. )

앱을 다시 만들 필요없이 외부에서 사진만 변경하였습니다.
외부저장소에서 사진 변경 전
![Screenshot_20230913_120045](https://github.com/incava/AndroidStorageUtil/assets/68988975/42d8835f-3c31-4da9-8179-5674112effa1)



외부저장소에서 사진 변경 후
![Screenshot_20230913_123242](https://github.com/incava/AndroidStorageUtil/assets/68988975/949dbd02-c056-4e37-a55a-eb33bb7eadb6)



## 코드 작성 전 세팅 방법

---

1. 내장 저장공간 안에 원하는 폴더를 만듦니다. 저는 “Cafe”라는 폴더를 생성하였습니다. 
    ![KakaoTalk_Photo_2023-09-13-12-42-59 005](https://github.com/incava/AndroidStorageUtil/assets/68988975/f233a704-880d-425e-88e0-cfe1592cd450)


    

1. 이 안에 원하는 사진을 넣어주세요. 이름은 편하게 지으시면 됩니다.
   ![KakaoTalk_Photo_2023-09-13-12-50-18 (1)](https://github.com/incava/AndroidStorageUtil/assets/68988975/009c225f-8706-4d7c-b311-a37ad61f14b2)


각 폴더와 이름 명,그리고 확장명(jpg)까지 알고 있어야만 합니다.

### 전체코드

---

MainActivity.kt

```kotlin
package com.incava.androidstorageutil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.incava.androidstorageutil.databinding.ActivityMainBinding
import com.incava.androidstorageutil.util.ExternalFilePath
import com.incava.androidstorageutil.util.ExternalFilePath.Companion.getExternalFilePath

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkPermission()

    }

    private val versionPermission = if (Build.VERSION.SDK_INT >= 33) {
        // 33버전은 Media_Image 퍼미션 필요. 추후 영상이 들어가면 퍼미션 추가 해야함!
        // 이미지 및 사진 READ_MEDIA_IMAGES
        // 동영상 READ_MEDIA_VIDEO
        // 오디오 파일 READ_MEDIA_AUDIO
        // 매니페스트에도 필요한 권한을 추가하고 권한 또한 승인 받아야함.
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        // 33버전 이하 버전에서는 READ_EXTERNAL_STORAGE권한만 있으면 됩니다.
        Manifest.permission.READ_EXTERNAL_STORAGE

    }

    private fun checkPermission() {
        // 권한이 이미 허용 되었는지 확인
        if (checkSelfPermission(
                versionPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {// 권한이 이미 허용된 경우
            // 이곳에서 이미지 주소를 가져와 적용하면 됩니다.
            setCafeImage()
        } else {
            // 권한이 허용되지 않은 경우 권한 요청 다이얼로그를 띄움
            requestPermission.launch(versionPermission)
        }
    }

    // 권한 승인 런쳐
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // 권한이 허용된 경우
                // 이곳에서 이미지 주소를 가져와 적용하면 됩니다.
                setCafeImage()
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "미디어 읽기 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    fun setCafeImage(){
        // 카페이미지를 가져와 적용.
        binding.ivPicture.setImageURI(getExternalFilePath(FOLDER_NAME, IMAGE_NAME))
    }

    companion object{
        // 데모 형식이기에 직접 대입.
        const val FOLDER_NAME = "Cafe"
        const val IMAGE_NAME = "americano.jpg"
    }

}
```

util/ExternalFilePath

```kotlin
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
```

activity_main.xml
