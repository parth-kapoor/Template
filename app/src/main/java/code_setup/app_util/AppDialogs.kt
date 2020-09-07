package code_setup.app_util

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import code_setup.app_core.BaseApplication
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.app_util.views_.OpenBottonDialogAdapter
import com.electrovese.setup.BuildConfig
import com.electrovese.setup.R
import com.facebook.drawee.view.SimpleDraweeView
import com.rd.PageIndicatorView
import java.util.*
import code_setup.app_util.callback_iface.OnBottomDialogItemListener as OnBottomDialogItemListener1


class AppDialogs {
    companion object {


        /*var mCompositeDisposable: CompositeDisposable? = null
        var mBottomSheetDialog: Dialog? = null
        var selectedDate: String? = null
        private fun getDateWiseCases(
            selectedDate: String,
            mBottomSheetDialog: Dialog
        ) {
            this.mBottomSheetDialog = mBottomSheetDialog
            this.selectedDate = selectedDate
            mCompositeDisposable = CompositeDisposable()
            val apiService = RestInitAPI.RedditApiWithHeader.create()
            mCompositeDisposable?.add(
                apiService.getActiveCases(
                    AppUtils.getCommonHeaders(), RequestCasesModel(selectedDate, "")
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleSuccessUpdate, this::handleFaliurUpdate)
            )

        }

        fun handleSuccessUpdate(baseResponse: CaseResponseModel) {
            var bResponse = baseResponse
            if (bResponse.response_code == NetworkConstant.SUCCESS) {
                if (bResponse.response_obj != null) {
                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog!!.dismiss()
                    }
                    bResponse.response_obj.selectedDate = this.selectedDate.toString()
                    EventBus.getDefault().postSticky(CustomEvent<Any>(EVENTS.DATE_WISE_CASE_RECEIVED, bResponse))
                } else {

                }
            } else {
                AppUtils.showToast(bResponse.response_message)
            }
        }
        fun handleFaliurUpdate(error: Throwable) {
        }
*/

        fun openDialogGoOfflineAlert(
            activity: Activity,
            asf: Any,
            tText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity)
            val child = activity.layoutInflater.inflate(R.layout.view_open_scan_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val dialogTitle: TextView = child.findViewById(R.id.dialogTitle)
            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val dialogBtn: TextView = child.findViewById(R.id.dialogOpenBtn)

            dialogBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//            dialogBtn.setText(asf as String)
//            dialogMessageText.setText(tText as String)
            dialogBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(dialogBtn, 0, 1, Any())
            }
            mBottomSheetDialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
                override fun onDismiss(arg0: DialogInterface?) { // do something
                    listener.onItemClick(dialogBtn, 0, 0, Any())
                }
            })
            mBottomSheetDialog.show()
        }


        fun openDialogScaneAlert(
            activity: Activity,
            asf: Any,
            tText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity)
            val child = activity.layoutInflater.inflate(R.layout.view_open_scan_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val dialogBtn: TextView = child.findViewById(R.id.dialogOpenBtn)
//            dialogBtn.setText(asf as String)
//            dialogMessageText.setText(tText as String)
            dialogBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(dialogBtn, 0, 1, Any())
            }
            mBottomSheetDialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
                override fun onDismiss(arg0: DialogInterface?) { // do something
                    listener.onItemClick(dialogBtn, 0, 0, Any())
                }
            })
            mBottomSheetDialog.show()
        }


        fun openDialogArrivedMark(
            activity: Activity,
            asf: Any,
            tText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_mark_arrived_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val arrivedBtnDialog: TextView = child.findViewById(R.id.arrivedBtnDialog)
            val dialogBtn: ImageView = child.findViewById(R.id.closeDialog)
            arrivedBtnDialog.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(dialogBtn, 0, CommonValues.TOUR_DESTINATION_ARRIVED, Any())
            }

            dialogBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(dialogBtn, 0, 0, Any())
            }
            mBottomSheetDialog.show()
        }


        fun openDialogTwoButtons(
            activity: Activity,
            tText: Any,
            mText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_two_btns_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)
            val cancelBtnDialog: TextView = child.findViewById(R.id.cancelBtnDialog)
            val doneBtnDialog: TextView = child.findViewById(R.id.doneBtnDialog)

            val titleTxtDialog: TextView = child.findViewById(R.id.titleTxtDialog)
            val messageTextDialog: TextView = child.findViewById(R.id.messageTextDialog)
            titleTxtDialog.setText(tText as String)
            messageTextDialog.setText(mText as String)

            doneBtnDialog.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(doneBtnDialog, 0, 1, Any())
            }

            cancelBtnDialog.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(cancelBtnDialog, 0, 0, Any())
            }
            mBottomSheetDialog.show()
        }


        fun openDialogOneButton(
            activity: Activity,
            asf: Any,
            tText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_one_button_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val dialogBtn: TextView = child.findViewById(R.id.dialogBtn)
            dialogBtn.setText(asf as String)
            dialogMessageText.setText(tText as String)
            dialogBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
                listener.onItemClick(dialogBtn, 0, 0, Any())
            }
            mBottomSheetDialog.show()
        }


        fun openDialogThanku(
            activity: Activity,
            aInt: Int,
            tTextList: ArrayList<String>,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, android.R.style.Theme_NoTitleBar_Fullscreen)
            val child = activity.layoutInflater.inflate(R.layout.view_full_thanku_screen, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()
                ?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val closeImage: ImageView = child.findViewById(R.id.closeBtnImage)
            val backLayout: LinearLayout = child.findViewById(R.id.backLayout)
            closeImage.bringToFront()
            backLayout.setOnClickListener {
                activity.finish()
                mBottomSheetDialog.dismiss()

//                listener.onItemClick(backLayout, 0, 0, Any())
            }
            closeImage.setOnClickListener {
                activity.onBackPressed()
                mBottomSheetDialog.dismiss()

//                listener.onItemClick(closeImage, 0, 0, Any())
            }
            mBottomSheetDialog.show()

        }


        fun openDialogFullScreenImage(
            activity: Activity,
            asf: Any,
            tText: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, android.R.style.Theme_NoTitleBar_Fullscreen)
            val child = activity.layoutInflater.inflate(R.layout.view_full_image_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()
                ?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val closeImage: ImageView = child.findViewById(R.id.closeImage)
            closeImage.bringToFront()
            val fullImageView: SimpleDraweeView = child.findViewById(R.id.fullImageView)
//            fullImageView.setImageURI(Uri.parse(tText as String))
//            Picasso.get().load(tText as String).into(fullImageView)
            closeImage.setOnClickListener {
                mBottomSheetDialog.dismiss()
//                listener.onItemClick(closeImage, 0, 0, Any())
            }
            mBottomSheetDialog.show()

        }

        fun openDialogFullScreenImages(
            activity: Activity,
            aInt: Int,
            tTextList: ArrayList<String>,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, android.R.style.Theme_NoTitleBar_Fullscreen)
            val child = activity.layoutInflater.inflate(R.layout.view_full_image_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()
                ?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)

            val closeImage: ImageView = child.findViewById(R.id.closeImage)
            closeImage.bringToFront()
            val fullImageView: SimpleDraweeView = child.findViewById(R.id.fullImageView)
            fullImageView.visibility = View.GONE
            val fullImageViewPager: ViewPager = child.findViewById(R.id.fullImageViewPager)

            val relativeLayout: RelativeLayout = child.findViewById(R.id.pagerHolderView)
            val pagerIndicator: PageIndicatorView = child.findViewById(R.id.pageIndicatorView)
            relativeLayout.visibility = View.VISIBLE

            /*  var mCustomPagerAdapter = CustomPagerAdapter(activity, tTextList)
              fullImageViewPager.bringToFront()
              pagerIndicator.bringToFront()

              fullImageViewPager.setAdapter(mCustomPagerAdapter)
              fullImageViewPager.setCurrentItem(aInt)*/

            closeImage.setOnClickListener {
                mBottomSheetDialog.dismiss()
            }
            mBottomSheetDialog.show()

        }


        /*
               * ----------------------------
               * Location permission dialog
               * -----------------------------
               */

        fun openLocationPermissionAlert(
            activity: Activity, any: Any, any1: Any,
            onBottomDialogItemListener: OnBottomDialogItemListener1<Any>
        ): Dialog {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child =
                activity.layoutInflater.inflate(
                    R.layout.layout_view_location_permission_dialog,
                    null
                )
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()
                ?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.BOTTOM)
//            val resetBtn: TextView = child.findViewById(R.id.resetBtn)
            val settingButtonDialog: Button = child.findViewById(R.id.settingButtonDialog)

            settingButtonDialog.setOnClickListener {
                mBottomSheetDialog.dismiss()
                // Build intent that displays the App settings screen.
                var intent: Intent = Intent()
                intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                )
                var uri: Uri = Uri.fromParts(
                    "package",
                    BuildConfig.APPLICATION_ID, null
                )
                intent.setData(uri)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
            }


            mBottomSheetDialog.show()
            return mBottomSheetDialog
        }

        fun openDialog(
            activity: Activity,
            name: Array<String>,
            icons: Array<Int>,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialogSheet)
            val child = activity.layoutInflater.inflate(R.layout.view_image_pick_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()!!.setGravity(Gravity.BOTTOM)
            mBottomSheetDialog.show()
            val list_view: RecyclerView = child.findViewById(R.id.list_view)
            list_view.setLayoutManager(LinearLayoutManager(activity))
            list_view.setAdapter(
                OpenBottonDialogAdapter(
                    name,
                    icons,
                    object : OnItemClickListener<Any> {
                        override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {
                            when (type) {
                                CommonValues.APAPTER_BOTTOM_DIALOG_CLICK -> {
                                    val s = t as String
                                    Log.e("Data ", s)
                                    listener.onItemClick(view, position, type, s)
                                    mBottomSheetDialog.dismiss()
                                }
                            }
                        }
                    })
            )
        }


        fun openDialogTwoButton(
            activity: Activity,
            strTitle: Any,
            steMsg: Any,
            listener: OnBottomDialogItemListener1<Any>
        ): Dialog {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_two_button_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)
            val dialogMessagTitls: TextView = child.findViewById(R.id.dialogMessagTitls)
            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val cancelBtn: TextView = child.findViewById(R.id.dialogCancelBtn)
            val okBtn: TextView = child.findViewById(R.id.dialogOkBtn)
            okBtn.setText(R.string.str_reject)
            dialogMessagTitls.setText(strTitle as String)
            dialogMessageText.setText(steMsg as String)

            cancelBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
            }

            okBtn.setOnClickListener {
                listener.onItemClick(okBtn, 0, CommonValues.REJECT, Any())
                mBottomSheetDialog.dismiss()
            }
            mBottomSheetDialog.show()
            return mBottomSheetDialog
        }


        fun openSelectConcernAlert(
            activity: Activity, any: Any, any1: Any,
            onBottomDialogItemListener: OnBottomDialogItemListener1<Any>
        ): Dialog {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child =
                activity.layoutInflater.inflate(
                    R.layout.layout_view_bottom_concern_options_dialog,
                    null
                )
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(true)
            mBottomSheetDialog.getWindow()
                ?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.BOTTOM)
            val concernOneTxt: TextView = child.findViewById(R.id.concernOne)
            val concernTwoTxt: TextView = child.findViewById(R.id.concernTwo)
            val concernThreeTxt: TextView = child.findViewById(R.id.concernThree)
            val closeImageBtnDialog: ImageView = child.findViewById(R.id.closeImageBtnDialog)

            concernOneTxt.setOnClickListener {
                onBottomDialogItemListener.onItemClick(child, 0, 0, concernOneTxt.text.toString())
                mBottomSheetDialog.dismiss()
            }
            concernTwoTxt.setOnClickListener {

                onBottomDialogItemListener.onItemClick(child, 0, 1, concernTwoTxt.text.toString())
                mBottomSheetDialog.dismiss()
            }
            concernThreeTxt.setOnClickListener {
                onBottomDialogItemListener.onItemClick(child, 0, 2, concernThreeTxt.text.toString())
                mBottomSheetDialog.dismiss()
            }
            val function: (View) -> Unit = {
                mBottomSheetDialog.dismiss()

            }
            closeImageBtnDialog.setOnClickListener(function)


            mBottomSheetDialog.show()
            return mBottomSheetDialog
        }


        fun openDialogScheduleBooking(
            activity: Activity,
            strTitle: Any,
            steMsg: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_schedule_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)
            val dialogMessagTitls: TextView = child.findViewById(R.id.dialogMessagTitls)
            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val dateFieldTxt: TextView = child.findViewById(R.id.dateFieldTxt)
            val timeFieldTxt: TextView = child.findViewById(R.id.timeFieldTxt)
            dateFieldTxt.setOnClickListener {
                dateFieldTxt.text = showDatePicker(activity)
            }
            timeFieldTxt.setOnClickListener {
                timeFieldTxt.text = showTimePicker(activity)
            }
            val cancelBtn: TextView = child.findViewById(R.id.dialogCancelBtn)
            val confirmBtn: TextView = child.findViewById(R.id.dialogConfirmBtn)
            cancelBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
                BaseApplication.scheduledDate = ""
            }

            confirmBtn.setOnClickListener {
                if (dateFieldTxt.text.isNotBlank() && timeFieldTxt.text.isNotBlank()) {
                    BaseApplication.scheduledDate =
                        dateFieldTxt.text.toString() + " at " + timeFieldTxt.text.toString()
                    listener.onItemClick(
                        confirmBtn,
                        0,
                        1,
                        dateFieldTxt.text.toString() + " at " + timeFieldTxt.text.toString()
                    )
                    mBottomSheetDialog.dismiss()
                }


            }
            mBottomSheetDialog.show()

        }

        private fun showTimePicker(activity: Activity): String {
            var selectedTime = DateUtilizer.getCurrentDate("hh:mm a")
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                activity,
                OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    //                    time.setText("$selectedHour:$selectedMinute")
                    selectedTime = "$selectedHour:$selectedMinute"
                    Log.e("showTimePicker ", "-----  " + selectedTime)
                },
                hour,
                minute,
                true
            ) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
            return selectedTime
        }

        private fun showDatePicker(activity: Activity): String {
            var selectedDate = DateUtilizer.getCurrentDate("dd MMMM, yyyy")
            // calender class's instance and get current date , month and year from calender
            // calender class's instance and get current date , month and year from calender
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR) // current year

            val mMonth: Int = c.get(Calendar.MONTH) // current month

            val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

            // date picker dialog
            // date picker dialog
            var datePickerDialog = DatePickerDialog(
                activity,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // set day of month , month and year value in the edit text
//                    date.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    selectedDate = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    Log.e("showDatePicker ", "-----  " + selectedDate)
                    return@OnDateSetListener
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
            val selectedDate1 = selectedDate
            return selectedDate1
        }


        fun openDialogSoSAlert(
            activity: Activity,
            strTitle: Any,
            steMsg: Any,
            listener: OnBottomDialogItemListener1<Any>
        ) {
            val mBottomSheetDialog = Dialog(activity, R.style.MaterialDialog)
            val child = activity.layoutInflater.inflate(R.layout.view_sos_dialog, null)
            mBottomSheetDialog.setContentView(child)
            mBottomSheetDialog.setCancelable(false)
            mBottomSheetDialog.getWindow()?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mBottomSheetDialog.getWindow()?.setGravity(Gravity.CENTER)
            val dialogMessagTitls: TextView = child.findViewById(R.id.dialogMessagTitls)
            val dialogMessageText: TextView = child.findViewById(R.id.dialogMessageText)
            val callSafetyHelplineNo: TextView = child.findViewById(R.id.callSafetyHelplineNo)

            val closeBtn: TextView = child.findViewById(R.id.dialogCloseBtn)
            closeBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
            }

            closeBtn.setOnClickListener {
                mBottomSheetDialog.dismiss()
            }
            mBottomSheetDialog.show()

        }

    }
}