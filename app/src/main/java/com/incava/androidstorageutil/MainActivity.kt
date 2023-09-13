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