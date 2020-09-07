package code_setup.ui_.settings

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import code_setup.app_core.CoreActivity
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_models.response_.ProfileResponseModel
import code_setup.app_models.response_.ProfileUpdateResponseModel
import code_setup.app_util.*
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.views.HomeActivity
import code_setup.ui_.settings.di_settings.DaggerSettingsComponent
import code_setup.ui_.settings.di_settings.SettingsModule
import code_setup.ui_.settings.settings_mvp.SettingsPresenter
import code_setup.ui_.settings.settings_mvp.SettingsView
import code_setup.ui_.settings.views.profile.ComplimentsAdapter
import com.base.mvp.BasePresenter
import com.electrovese.setup.R
import com.google.gson.Gson
import com.ivan200.photobarcodelib.PhotoBarcodeScannerBuilder
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import kotlinx.android.synthetic.main.default_loading.*
import kotlinx.android.synthetic.main.layout_profile_fragment.*
import kotlinx.android.synthetic.main.profile_detail_fields_view.*
import kotlinx.android.synthetic.main.profile_top_image_selection.*
import kotlinx.android.synthetic.main.profile_top_view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList


class ProfileActivity : CoreActivity(), SettingsView {
    lateinit var compAapter: ComplimentsAdapter
    var TAG: String = ProfileActivity::class.java.simpleName
    override fun onResponse(list: Any, int: Int) {
        Log.e(TAG, "" + Gson().toJson(list))
        try {
            avd!!.stop()
        } catch (e: Exception) {
        }
        progress_loading.visibility = View.GONE
        when (int) {
            NetworkRequest.REQUEST_PROFILE_CODE -> {
                var responseData = list as ProfileResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    setupsideMenuDetail(responseData.response_obj)
                    if (responseData.response_obj.complements != null && responseData.response_obj.complements.isNotEmpty()) {
                        compAapter.updateAll(responseData.response_obj.complements)
                    }
                }
            }
            NetworkRequest.REQUEST_UPDATE_PROFILE -> {
                var responseData = list as ProfileUpdateResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    var userData = getUserData() as LoginResponseModel.ResponseObj
                    userData.user_image = responseData.response_obj.user_image
                    Prefs.putString(CommonValues.USER_DATA, Gson().toJson(userData))
                    try {
                        userImgProfile.setImageURI(responseData.response_obj.user_image)
                    } catch (e: Exception) {
                    }
                    refreshHomeData()
                }
            }
        }
    }

    private fun refreshHomeData() {
        if (HomeActivity.homeInstance != null) {
            HomeActivity.homeInstance.setupsideMenuDetail()
        }
    }


    override fun showProgress() {

    }

    override fun hideProgress() {
        avd!!.stop()
        progress_loading.visibility = View.GONE
    }

    override fun noResult() {
    }

    lateinit var selectedImageUri: String
    var avd: AnimatedVectorDrawable? = null
    lateinit var action: Runnable
    @Inject
    lateinit var presenter: SettingsPresenter

    override fun onActivityInject() {
        DaggerSettingsComponent.builder().appComponent(getAppcomponent())
            .settingsModule(SettingsModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_profile_fragment
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        toolbar_root.setBackgroundColor(resources.getColor(R.color.colorWhite))
        txtTitletoolbar.setTextColor(resources.getColor(R.color.colorTextLabel))
        backToolbar.setColorFilter(getResources().getColor(R.color.colorTextLabel))
        txtTitletoolbar.setText(R.string.strProfile)
//        AppUtils.statusBarIconTheme(this, true)
        backToolbar.setOnClickListener {
            onBackPressed()
        }
        setupbasicDetail(getUserData() as LoginResponseModel.ResponseObj)
        Handler().post(Runnable {
            getUserProfileData()
        })
        userImgProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT < 23) {
                //Do not need to check the permission
                openPickerAlert()
            } else {
                if (checkAndRequestPermissions()) {
                    //If you have already permitted the permission
                    openPickerAlert()
                }
            }
        }
    }

    private fun openPickerAlert() {
        val name = arrayOf("TAKE A PHOTO", "CHOOSE FROM GALLERY")
        val icons =
            intArrayOf(R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery)
        AppDialogs.openDialog(
            this,
            name,
            icons as Array<Int>,
            object : OnBottomDialogItemListener<Any> {
                override fun onItemClick(view: View, position: Int, type: Int, t: Any) {
                    when (t as String) {
                        "TAKE A PHOTO" -> {
                            openCam()
                        }
                        "CHOOSE FROM GALLERY" -> {
                            pickFromGallry()
                        }
                    }
                }
            })
    }

    private fun pickFromGallry() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val startActivityForResult: Any = startActivityForResult(
            Intent.createChooser(intent, "Select avatar..."),
            111
        )
    }

    private fun openCam() {
        var photoBarcodeScanner = PhotoBarcodeScannerBuilder()
            .withActivity(this)
            .withTakingPictureMode()
            .withTakingPictureMode()               //Activate takingPicture mode instead of taking barcode (barcode mode is default)
            .withPreviewImage(true)                //Allow preview image and redo it before it returned
//            .withPictureListener(Consumer<File>)   //Set listener to take picture, file will saved in context.getFilesDir()/photos
//            .withFacingListener(Consumer<Boolean>) //Sets listener when camera facing changed (for ability to save the last used camera facing in settings)
            .withFlipFaceFrontResultImage(false)   //Enables or disables flip result image of facing front camera
            .withThumbnails(false)                 //In addition to the photo the thumbnail will be saved too (in context.getFilesDir()/thumbnails)
            .withCameraTryFixOrientation(true)     //Automatically try to rotate final image by phone sensors
            .withImageLargerSide(800)             //Once the picture is taken, if its too big, it automatically resizes by the maximum side
            .withSavePhotoToGallery("/FreshBasket")
            .withCameraLockRotate(false)
            .withChangeCameraAllowed(false)
            .withAutoFocus(true)
            .withPictureListener {
                Log.d("openCamera  ", " " + Uri.fromFile(it))
                //                imh.setImageURI(Uri.fromFile(it))
                userImgProfile.setImageURI(Uri.fromFile(it))
                selectedImageUri = it.absolutePath

                uploadImageUpdates()
            }.build()
        photoBarcodeScanner.start()
    }

    private fun uploadImageUpdates() {
        presenter.updtaeProfileImage(NetworkRequest.REQUEST_UPDATE_PROFILE, getRequiredData())
    }

    private fun getRequiredData(): MultipartBody.Part {
        var file = File(selectedImageUri)
        // Create a request body with file and image media type
        // Create a request body with file and image media type
        val fileReqBody: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // Create MultipartBody.Part using file request-body,file name and part name
        // Create MultipartBody.Part using file request-body,file name and part name
        val part = MultipartBody.Part.createFormData("user_image", file.getName(), fileReqBody)
        //Create request body with text description and text media type
        //Create request body with text description and text media type
        val description = RequestBody.create(MediaType.parse("text/plain"), "image-type")

        return part
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getUserProfileData() {
        try {
            action = Runnable { repeatAnimation() }
            avd = iv_line.getBackground() as AnimatedVectorDrawable
            avd!!.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    avd = iv_line.getBackground() as AnimatedVectorDrawable
                    avd!!.start()
                }
            })
            avd!!.start()
        } catch (e: Exception) {
        }
        presenter.getProfileData(NetworkRequest.REQUEST_PROFILE_CODE)
    }

    private fun repeatAnimation() {
        avd!!.start()
        iv_line.postDelayed(action, 5000) // Will repeat animation in every 1 second
    }

    private fun setupbasicDetail(userData: LoginResponseModel.ResponseObj) {
        if (userData != null) {
            userImgProfile.setImageURI(userData.user_image)
            userNameText.setText(userData.name)
            userNumberText.setText(userData.contact)
            try {
                userEmailText.setText(userData.email)
            } finally {

            }
        }
    }

    private fun setupsideMenuDetail(userData: ProfileResponseModel.ResponseObj) {
        if (userData != null) {
            userImageView.setImageURI(userData.user_image)

        }
    }

    override fun onResume() {
        super.onResume()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: kotlin.IntArray
    ) {
        when (requestCode) {
            CommonValues.REQUEST_CODE_PERMISSIONS_CAMERA -> {
                Log.e(
                    "onRequestPermissionsResult :: ", "User permissions granted" + ""
                )
                openPickerAlert()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            finish()
            return
        }
        when (requestCode) {
            111 -> {
                var outputFile = ImageUtility.getString(getApplicationContext(), data!!.getData());
                Log.d("onActivityResult ", " REQUEST_TAKE_IMAGE " + outputFile);
                userImgProfile.setImageURI(Uri.parse(outputFile))
                selectedImageUri = outputFile
                uploadImageUpdates()
            }
            UCrop.REQUEST_CROP -> if (data != null) {
//                handleCropResult(data)
            }
        }

    }

}