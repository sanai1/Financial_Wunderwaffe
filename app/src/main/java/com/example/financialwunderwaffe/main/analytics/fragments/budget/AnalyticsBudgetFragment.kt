package com.example.financialwunderwaffe.main.analytics.fragments.budget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.analytics.AnalyticsFragment
import com.example.financialwunderwaffe.main.analytics.AnalyticsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class AnalyticsBudgetFragment : Fragment() {
    private lateinit var viewModel: AnalyticsViewModel
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_analytics_budget, container, false)
        viewModel = (parentFragment as AnalyticsFragment).viewModel

        (activity as MainActivity).budgetViewModel.categories.observe(viewLifecycleOwner) { categories ->
            val map = mutableMapOf<Long, Boolean>()
            categories.forEach {
                map[it.id] = true
            }
            viewModel.setCategories(map)
        }

        view.findViewById<ImageView>(R.id.imageViewBackYearReportMain).setOnClickListener {
            viewModel.setDateBudget(viewModel.dateBudget.value!!.minusYears(1))
        }
        view.findViewById<ImageView>(R.id.imageViewForwardYearReportMain).setOnClickListener {
            viewModel.setDateBudget(viewModel.dateBudget.value!!.plusYears(1))
        }
        viewModel.dateBudget.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewYearReportMain).text = it.value.toString()
            searchNowYearBudget()
        }

        view.findViewById<CheckBox>(R.id.checkBoxLargeExpense).setOnClickListener {
            viewModel.setLarge(viewModel.large.value!!.copy(first = viewModel.large.value!!.first.not()))
        }
        view.findViewById<CheckBox>(R.id.checkBoxLargeIncome).setOnClickListener {
            viewModel.setLarge(viewModel.large.value!!.copy(second = viewModel.large.value!!.second.not()))
        }
        viewModel.large.observe(viewLifecycleOwner) {
            searchNowYearBudget()
        }

        view.findViewById<RecyclerView>(R.id.listCategoryReportMain).apply {
            adapter = CategoryAnalyticsAdapter(
                (activity as MainActivity).budgetViewModel.categories.value!!.map {
                    Pair(it, true)
                }.toMutableList(),
            ) { category ->
                viewModel.updateCategories(category.id)
            }
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.STRETCH
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            searchNowYearBudget()
        }

        viewModel.budgetByMonth.observe(viewLifecycleOwner) {
            searchNowYearBudget()
        }

        return view
    }

    private fun searchNowYearBudget() {
        val listMonthlyData: MutableMap<Int, Array<Long>> = mutableMapOf()
        repeat(12) { n ->
            listMonthlyData[n + 1] = arrayOf(0L, 0L)
        }
        viewModel.budgetByMonth.value!!.forEach { budgetAnalytics ->
            if (Year.from(
                    YearMonth.parse(
                        budgetAnalytics.month,
                        DateTimeFormatter.ofPattern("MM.yyyy")
                    )
                ) == viewModel.dateBudget.value
            ) {
                val month = YearMonth.parse(
                    budgetAnalytics.month,
                    DateTimeFormatter.ofPattern("MM.yyyy")
                ).monthValue
                budgetAnalytics.listCategory.forEach { categoryAnalytics ->
                    if (viewModel.categories.value!![categoryAnalytics.id] == true) {
                        when (categoryAnalytics.isIncome) {
                            true -> {
                                listMonthlyData[month]!![1] += if (viewModel.large.value!!.second) {
                                    categoryAnalytics.amountUsual + categoryAnalytics.amountLarge
                                } else {
                                    categoryAnalytics.amountUsual
                                }
                            }

                            else -> {
                                listMonthlyData[month]!![0] += if (viewModel.large.value!!.first) {
                                    categoryAnalytics.amountUsual + categoryAnalytics.amountLarge
                                } else {
                                    categoryAnalytics.amountUsual
                                }
                            }
                        }
                    }
                }
            }
        }
        listMonthlyData.map { entry ->
            MonthlyData(
                date = YearMonth.parse("${
                    entry.key.let {
                        if (it < 10) "0$it"
                        else it
                    }
                }.${viewModel.dateBudget.value}", DateTimeFormatter.ofPattern("MM.yyyy")),
                expense = entry.value[0],
                income = entry.value[1]
            )
        }.also { monthlyDataList ->
            val chart = view.findViewById<LineChart>(R.id.lineChartReportMain)
            val infoText = view.findViewById<TextView>(R.id.textViewReportMain)
            if (monthlyDataList.sumOf { it.expense + it.income } == 0L) {
                chart.visibility = View.GONE
                infoText.visibility = View.VISIBLE
            } else {
                chart.visibility = View.VISIBLE
                infoText.visibility = View.GONE
                initLineChart(monthlyDataList)
            }
        }
    }

    private fun initLineChart(listMonthlyData: List<MonthlyData>) {
        val chart = view.findViewById<LineChart>(R.id.lineChartReportMain)
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            setDrawBorders(false)
            legend.apply {
                form = Legend.LegendForm.LINE
                textSize = 15f
                textColor = Color.BLACK
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                textColor = Color.DKGRAY
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return listMonthlyData.getOrNull(value.toInt())?.date?.month?.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )
                            ?: ""
                    }
                }
            }
            axisLeft.apply {
                setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                textColor = Color.DKGRAY
                axisMinimum = 0f
                granularity = 1000f
                zeroLineWidth = 1f
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
            }
            axisRight.isEnabled = false
        }

        val expenseEntries = mutableListOf<Entry>()
        val incomeEntries = mutableListOf<Entry>()
        listMonthlyData.sortedBy { it.date }.forEachIndexed { index, monthlyData ->
            expenseEntries.add(Entry(index.toFloat(), monthlyData.expense.toFloat()))
            incomeEntries.add(Entry(index.toFloat(), monthlyData.income.toFloat()))
        }

        val expenseLine = LineDataSet(expenseEntries, "Расходы").apply {
            color = Color.RED
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(Color.RED)
            setDrawCircleHole(false)
            valueTextSize = 10f
            valueTextColor = Color.RED
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0f
        }
        val incomeLine = LineDataSet(incomeEntries, "Доходы").apply {
            color = Color.GREEN
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(Color.GREEN)
            setDrawCircleHole(false)
            valueTextSize = 10f
            valueTextColor = Color.GREEN
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.1f
        }

        chart.data = LineData(incomeLine, expenseLine).apply {
            setValueFormatter(DefaultValueFormatter(2))
        }
        chart.animateXY(1000, 1000)
        chart.invalidate()
    }

}