package code_setup.app_util

import android.util.Log
import code_setup.app_models.other_.DateModel
import code_setup.app_util.location_utils.log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DateUtilizer() {


    companion object {
        var DEFAULT_DATE_TIME_FORMAT: String = "dd-MM-YYYY hh:mm a"// "EEEE,MMMM YY";
        val SERVER_DATE_FORMAT:String="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"//"yyyy EEE MMM dd HH:mm:ss zzzz"//"2020-04-21T13: 56: 36.907Z",
        val FULL_FORMAT: String = "dd MM YYYY, hh:mm:ss a"
        var DEFAULT_DATE_FORMAT: String = "dd-MM-YYYY"// "EEEE,MMMM YY";
        var MONTH_FORMAT: String = "MMMM"
        var YEAR_FORMAT: String = "YYYY"


        /**
         * get required current date format.
         */
        fun getCurrentDate(format: String): String {
            val dateFormat = SimpleDateFormat(format)
            val date = Date()
            Log.d("Date :: ", dateFormat.format(date))
            return dateFormat.format(date)
        }

        /**
         * get current date .
         */
        fun getCurrentDate(): Date {
            return Calendar.getInstance().getTime()
        }

        fun differenceBetweenDates(date1: Long, date2: Long): Long {
            Log.d("differenceBetweenDates ", "  " + date1 + "    " + date2)
            val diff: Long = date1 - date2
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            Log.d("Difference ", "in seconds" + seconds)
            Log.d("Difference ", "in minutes" + minutes)
            return seconds
        }


        /**
         * get last sunday date
         */
        fun getLastDay(): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) - 1))
            Log.d(" month :::::", "" + -(cal.get(Calendar.DAY_OF_WEEK) - 1))
            System.out.println(cal.get(Calendar.DATE))
            System.out.println(cal.get(Calendar.MONTH))
            var intMonth = cal.get(Calendar.MONTH)
            intMonth = (intMonth + 1)
            Log.d(" month :::::", "" + (intMonth + 1))
            return "" + cal.get(Calendar.DATE) + "-" + intMonth
        }

        /**
         * get last sunday date
         */
        fun getNextMonthDay(): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) - 1))
            System.out.println(cal.get(Calendar.DATE))
            System.out.println(cal.get(Calendar.MONTH))
            var intMonth = cal.get(Calendar.MONTH)
            intMonth = (intMonth + 1)
            Log.d(" month :::::", "" + (intMonth + 1))
            return "" + cal.get(Calendar.DATE) + "-" + intMonth
        }


        fun getFormatedDate(inFormat: String, outFormat: String, dateText: String): String {

            var str: String? = ""
            try {
                Log.i(" getFormatedDate ", " indate ------------ " + dateText)
                val inputFormat = SimpleDateFormat(inFormat)
                val outputFormat = SimpleDateFormat(outFormat)

                var date: Date? = null
                str = null

                try {
                    date = inputFormat.parse(dateText)
                    str = outputFormat.format(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                Log.i(" getFormatedDate ", " outdate ----------- " + outFormat + "   " + str)
            } catch (e: Exception) {
                e.printStackTrace()
                str = dateText
            }
            return str.toString()
        }

        fun getNextDays(dCount: Int, iDate: String): ArrayList<DateModel> {
            Log.i(" getNextDays ", "1 --------------------------- $iDate")
            val modelList = ArrayList<DateModel>()
            var mDay = DateModel()
            val calendar = GregorianCalendar.getInstance();
            if (!iDate.equals("0")) {
                try {
//                val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, iDate.split("-")[2].toInt())
                    calendar.set(Calendar.MONTH, (iDate.split("-")[1].toInt() - 1))
                    calendar.set(Calendar.DAY_OF_MONTH, iDate.split("-")[0].toInt())

                    Log.i("TAG", "1 ---------------------------d " + iDate.split("-")[0].toInt())
                    Log.i(
                        "TAG",
                        "1 ---------------------------m " + (iDate.split("-")[1].toInt() - 1)
                    )
                    Log.i("TAG", "1 ---------------------------y " + iDate.split("-")[2].toInt())
                    Log.i("TAG", "1 --------------------------- $iDate")
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

            val sdfFull = SimpleDateFormat("MMMM/dd/yyyy")
            val sdf = SimpleDateFormat("EE")
            val sdf1 = SimpleDateFormat("dd")
            if (!iDate.equals("0")) {
                val day = sdf.format(calendar.time)
                val date = sdf1.format(calendar.time)
                val fullDate = sdfFull.format(calendar.time)
                Log.i(" First Day", day + " " + date + "  " + fullDate)
                mDay = DateModel()
                mDay.date = Integer.parseInt(date)
                mDay.day = day.substring(0, 2)
                mDay.month = fullDate.split("/")[0]
                mDay.monthYear = fullDate.split("/")[0] + " " + fullDate.split("/")[2]
                mDay.fullDate = fullDate
                mDay.isSelected = false
                modelList.add(mDay)
            }




            for (i in 0 until dCount) {
                Log.i("TAG", "2--------------------------- $i")
                calendar.add(Calendar.DATE, 1)
                val day = sdf.format(calendar.time)
                val date = sdf1.format(calendar.time)
                val fullDate = sdfFull.format(calendar.time)
                Log.i("TAG", day + " " + date + "  " + fullDate)
                mDay = DateModel()
                mDay.date = Integer.parseInt(date)
                mDay.day = day.substring(0, 2)
                mDay.month = fullDate.split("/")[0]
                mDay.monthYear = fullDate.split("/")[0] + " " + fullDate.split("/")[2]
                mDay.fullDate = fullDate
                if (fullDate.equals(getCurrentDate("MMMM/dd/yyyy")))
                    mDay.isSelected = true
                modelList.add(mDay)
            }

            return modelList
        }
         fun getTimeDiffereence(startTime: String, endTime: String): Long {
            val startDate = startTime
            val stopDate = endTime

// Custom date format
            // Custom date format
            val format = SimpleDateFormat("dd-MM-YYYY, HH:mm")

            var d1: Date? = null
            var d2: Date? = null
            try {
                d1 = format.parse(startDate)
                d2 = format.parse(stopDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

// Get msec from each, and subtract.
            // Get msec from each, and subtract.
            val diff = d2!!.time - d1!!.time
            val diffSeconds = diff / 1000
            val diffMinutes = diff / (60 * 1000)
            val diffHours = diff / (60 * 60 * 1000)
            println("Time in seconds: $diffSeconds seconds.")
            println("Time in minutes: $diffMinutes minutes.")
            println("Time in hours: $diffHours hours.")
            return diffMinutes
        }
        /*
        * Get calendar from string
        * */
         fun getcalenderFromString(month: String,format:String): Calendar {
            log("getcalenderFromString    " + month)
            val aTime = month
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val cal = Calendar.getInstance()
            try {
                cal.time = sdf.parse(aTime)
                Log.i("getcalenderFromString", "time = " + cal.timeInMillis)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return cal
        }

        /**
         * Get Previous Week
         */
        fun getPastDays(iDay: Int, iDate: String): ArrayList<DateModel> {
            Log.i("getPastWeek", "1 --------------------------- $iDate")
            val modelList = ArrayList<DateModel>()
            var mDay = DateModel()
            val calendar = GregorianCalendar.getInstance()

            try {
//                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, iDate.split("-")[2].toInt())
                calendar.set(Calendar.MONTH, (iDate.split("-")[1].toInt() - 1))
                calendar.set(Calendar.DAY_OF_MONTH, iDate.split("-")[0].toInt())

                Log.i("TAG", "1 ---------------------------d " + iDate.split("-")[0].toInt())
                Log.i("TAG", "1 ---------------------------m " + (iDate.split("-")[1].toInt() - 1))
                Log.i("TAG", "1 ---------------------------y " + iDate.split("-")[2].toInt())
                Log.i("TAG", "1 --------------------------- $iDate")
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val sdfFull = SimpleDateFormat("MMMM/dd/yyyy")
            val sdf = SimpleDateFormat("EE")
            val sdf1 = SimpleDateFormat("dd")
            for (i in 1 until iDay) {

                calendar.add(Calendar.DATE, -i)
                val day = sdf.format(calendar.time)
                val date = sdf1.format(calendar.time)
                val fullDate = sdfFull.format(calendar.time)
                Log.i("TAG", "$day $date")
                mDay = DateModel()
                mDay.date = Integer.parseInt(date)
                mDay.day = day.substring(0, 2)
                mDay.month = fullDate.split("/")[0]
                mDay.monthYear = fullDate.split("/")[0] + " " + fullDate.split("/")[2]
                mDay.fullDate = fullDate
                mDay.isSelected = false
                modelList.add(mDay)
                modelList.add(0, mDay)
            }
            //        specList.clear();
            modelList.addAll(0, modelList)

            return modelList
        }

        fun getPreText(sFormat: String, bookingDate: String): String {
            var strPreJoinTxt = ""
            try {
                val sdf =
                    SimpleDateFormat(sFormat, Locale.getDefault())
                val date1 = sdf.parse(getCurrentDate(sFormat))
                val date2 = sdf.parse(bookingDate)


                if (date1.compareTo(date2) > 0) {
                    Log.i("app", "Date1 is after Date2")
                } else if (date1.compareTo(date2) < 0) {
                    Log.i("app", "Date1 is before Date2")
//                    if(date1.compareTo(date2) == -1){
//                        strPreJoinTxt = "Tomorrow, "
//                    }
                } else if (date1.compareTo(date2) == 0) {
                    Log.i("app", "Date1 is equal to Date2")
                    strPreJoinTxt = "Today, "
                }

                Log.d(
                    "TAG",
                    " " + date1 + "   " + date2 + "  " + date1.compareTo(date2) + " " + strPreJoinTxt + "  " + bookingDate
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return strPreJoinTxt
        }

        fun getDateFromString(inFormat: String, strDate: String): Date? {
            var dtStart = strDate
            val format = SimpleDateFormat(inFormat);
            var date = try {
                format.parse(dtStart);

            } catch (e: ParseException) {
                e.printStackTrace();
            }
            try {
                System.out.println(date);
            } catch (e: Exception) {
            }
            return date as Date?
        }
    }

}