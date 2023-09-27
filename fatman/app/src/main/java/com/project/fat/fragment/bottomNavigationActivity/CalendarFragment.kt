package com.project.fat.fragment.bottomNavigationActivity

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.project.fat.R
import com.project.fat.data.dto.GetHistoryResponse
import com.project.fat.databinding.FragmentCalendarBinding
import com.project.fat.retrofit.client.HistoryRetrofit
import com.project.fat.manager.TokenManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import org.threeten.bp.DayOfWeek
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CalendarFragment : Fragment() {
    private lateinit var calendarView: MaterialCalendarView
    private var selectedDate: CalendarDay? = null
    //프래그먼트는 액티비티보다 수명이 길기에 뷰바인딩 정보가 필요 이상으로 저장되어 있을 수 있습니다.
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    var currentDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        calendarView = binding.calendarview

        var targetDate: LocalDate = LocalDate.now()

        fun fetchHistory(targetDate: LocalDate){
            HistoryRetrofit.getApiService()!!
                .getHistory(targetDate, resources.getString(R.string.prefix_of_access_token) + TokenManager.getAccessToken())
                .enqueue(object : Callback<GetHistoryResponse> {
                    override fun onResponse(call: Call<GetHistoryResponse>, response: Response<GetHistoryResponse>) {
                        val historyResponse = response.body()
                        Log.d("calendar", "Data fetch success $targetDate")
                        //val distance=historyResponse!!.GetHistoryResponse!!.distance
                    }

                    override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                        Log.d("calendar", t.message ?: "Error occurred")
                    }
                })}

        //처음 실행했을시 오늘 날짜 선택&오늘 날짜 표시
        calendarView.selectedDate = CalendarDay.today()
        val month = CalendarDay.today().month.toString()
        val today = CalendarDay.today().day.toString()
        val title1 = month + "월 " + today + "일"
        binding.tvTitle.text = title1
        fetchHistory(targetDate)

        //날짜 데코레이터
        val defaultDateDecorator = SelectedDateDecorator.DefaultDateDecorator(requireContext())
        calendarView.addDecorator(defaultDateDecorator)

        //요일 커스텀 데코레이터
        val customWeekDayFormatter = CustomWeekDayFormatter(requireContext())
        calendarView.setWeekDayFormatter(customWeekDayFormatter)

        val todayDecorator = TodayDecorator(requireContext()) //얘는 선언만 해두고 적용은 setTitleFormatter에서했음

        //Header세팅
        calendarView.setTitleFormatter { day ->
            val month = day.month.toString() //현재 달력에 보여지는 달.
            val showyear = day.year
            val thisyear = CalendarDay.today().year


            if (showyear != thisyear) {
                binding.tvYearmonth.text = showyear.toString() + ".${month}"
                binding.tvMonth.text = ""
            } else {
                binding.tvMonth.text = month
                binding.tvYearmonth.text = ""
            }
            calendarView.addDecorator(todayDecorator)

            ""//캘린더 기본 헤드 없애버림(커스텀 하기 어려움)
        }

        // 날짜 클릭시 커스텀 변경
        calendarView.setOnDateChangedListener { widget, date, selected ->
            targetDate = LocalDate.of(date.year, date.month, date.day)
            val formattedDate = date.day.toString()
            val formattedTitle = date.month.toString()
            val title2 = formattedTitle + "월 " + formattedDate + "일"
            binding.tvTitle.text = title2

            selectedDate?.let {
                val deselectDecorator = SelectedDateDecorator.DefaultDateDecorator(requireContext())
                calendarView.addDecorator(deselectDecorator)
                if (selectedDate == CalendarDay.today())
                    calendarView.addDecorator(todayDecorator)
            }
            if (selected) {
                val selectedDecorator = SelectedDateDecorator(requireContext(),date)
                calendarView.addDecorator(selectedDecorator)
                selectedDate = date

            } else {
                val decorator = SelectedDateDecorator.DefaultDateDecorator(requireContext())
                calendarView.addDecorator(decorator)
            }
            fetchHistory(targetDate)

        } //Changed 메서드 끝
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 오늘 날짜 데코레이터
class TodayDecorator(context: Context) : DayViewDecorator {
    private val date = CalendarDay.today()
    private val drawable: Drawable? = context.getDrawable(R.drawable.todaybackground)
    private val textColor = Color.WHITE

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date) ?: false
    }

    override fun decorate(view: DayViewFacade?) {
        if (drawable != null) {
            view?.setSelectionDrawable(drawable)
            view?.addSpan(ForegroundColorSpan(textColor))
        }
    }
}

// 선택한 날짜 데코레이터
class SelectedDateDecorator(context: Context, private val selectedDate: CalendarDay) : DayViewDecorator {
    private val drawable: Drawable? = context.getDrawable(R.drawable.selectedbackground)
    private val textColor = Color.WHITE

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == selectedDate
    }

    override fun decorate(view: DayViewFacade?) {
        if (drawable != null) {
            view?.setSelectionDrawable(drawable)
            view?.addSpan(ForegroundColorSpan(textColor))
        }
    }

    // 날짜 기본 데코레이터
    class DefaultDateDecorator(private val context: Context) : DayViewDecorator {
        private val drawable: Drawable? = context.getDrawable(R.drawable.defaultbackground)
        private val textColor = Color.parseColor("#6AC9FF")

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(CalendarDay.today())?.not() ?: true
        }

        override fun decorate(view: DayViewFacade?) {
            if (drawable != null) {
                view?.setSelectionDrawable(drawable)
                view?.addSpan(ForegroundColorSpan(textColor))
            }
        }
    }}


//요일 레이블 커스텀
class CustomWeekDayFormatter(private val context: Context) : WeekDayFormatter {
    private val weekDayLabels = context.resources.getStringArray(R.array.custom_weekdays)
    @RequiresApi(Build.VERSION_CODES.O)
    private val customFont: Typeface = context.resources.getFont(R.font.concert_one_regular)
    private val redColor: Int = Color.parseColor("#FF6262")

    @RequiresApi(Build.VERSION_CODES.P)
    override fun format(dayOfWeek: DayOfWeek): CharSequence {
        val weekDayText = weekDayLabels[dayOfWeek.value- 1]

        val colorRes = if (dayOfWeek == DayOfWeek.SATURDAY|| dayOfWeek == DayOfWeek.SUNDAY)redColor else Color.WHITE
        val spannable = SpannableString(weekDayText)
        spannable.setSpan(ForegroundColorSpan(colorRes), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(TypefaceSpan(customFont), 0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }
}

//// 러닝데이 데코레이터
//class RunningDateDecorator(private val context: Context, dates: Collection<CalendarDay>) : DayViewDecorator {
//    private val drawable: Drawable? = context.getDrawable(R.drawable.runningdaybackground)
//    private val textColor = Color.WHITE
//    var dates: HashSet<CalendarDay> = HashSet(dates)
//
//
//    override fun shouldDecorate(day: CalendarDay?): Boolean {
//        return dates.contains(day)
//    }
//
//    override fun decorate(view: DayViewFacade?) {
//        if (drawable != null) {
//            view?.setSelectionDrawable(drawable)
//            view?.addSpan(ForegroundColorSpan(textColor))
//        }
//    }
//}