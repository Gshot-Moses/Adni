package com.example.adni.presentation.company.add

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageView

class CropImageRequestContract: ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        return CropImage.activity(input)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setGuidelines(CropImageView.Guidelines.ON)
            .getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode == RESULT_OK) {
            val result = CropImage.getActivityResult(intent)
            return result.uri
        }
        return null
    }
}