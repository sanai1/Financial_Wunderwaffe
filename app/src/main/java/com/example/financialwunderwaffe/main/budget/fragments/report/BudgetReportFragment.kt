package com.example.financialwunderwaffe.main.budget.fragments.report

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
import com.example.financialwunderwaffe.main.analytics.fragments.budget.CategoryAnalyticsAdapter
import com.example.financialwunderwaffe.main.analytics.fragments.budget.MonthlyData
import com.example.financialwunderwaffe.main.budget.BudgetFragment
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
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
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class BudgetReportFragment : Fragment() {
    private var date =
        Year.parse(LocalDate.now().year.toString(), DateTimeFormatter.ofPattern("yyyy"))
    private lateinit var categoryReportAdapter: CategoryAnalyticsAdapter
    private lateinit var layoutManagerCategoryReport: RecyclerView.LayoutManager
    private val listNowYearTransaction = mutableListOf<Transaction>()
    private lateinit var view: View
    private val categoriesIdNow = mutableMapOf<Long, Boolean>()
    private var large = Pair(false, false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_report, container, false)

//        (parentFragment as BudgetFragment).listCategory.forEach {
//            categoriesIdNow[it.id] = it.type
//        }

        view.findViewById<ImageView>(R.id.imageViewBackYearReportMain).setOnClickListener {
            date = date.minusYears(1)
            searchNowYearTransaction()
        }
        view.findViewById<ImageView>(R.id.imageViewForwardYearReportMain).setOnClickListener {
            date = date.plusYears(1)
            searchNowYearTransaction()
        }

        layoutManagerCategoryReport = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.STRETCH
        }
//        categoryReportAdapter =
//            CategoryAnalyticsAdapter((parentFragment as BudgetFragment).listCategory.map {
//                Pair(
//                    it,
//                    true
//                )
//            }.toMutableList()) { category ->
//                if (category.id in categoriesIdNow.keys) {
//                    categoriesIdNow -= category.id
//                } else {
//                    categoriesIdNow[category.id] = category.type
//                }
//                searchNowYearTransaction()
//            }
        view.findViewById<RecyclerView>(R.id.listCategoryReportMain).apply {
            adapter = categoryReportAdapter
            layoutManager = layoutManagerCategoryReport
        }

        view.findViewById<CheckBox>(R.id.checkBoxLargeExpense).setOnClickListener {
            large = large.copy(first = large.first.not())
            searchNowYearTransaction()
        }
        view.findViewById<CheckBox>(R.id.checkBoxLargeIncome).setOnClickListener {
            large = large.copy(second = large.second.not())
            searchNowYearTransaction()
        }

        searchNowYearTransaction()

        return view
    }

    private fun searchNowYearTransaction() {
        view.findViewById<TextView>(R.id.textViewYearReportMain).text = date.value.toString()
        listNowYearTransaction.clear()
//        (parentFragment as BudgetFragment).listTransaction.forEach { transaction ->
//            if (Year.from(
//                    LocalDate.parse(
//                        transaction.date,
//                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
//                    )
//                ) == date
//            ) {
//                if (transaction.type.not()) {
//                    listNowYearTransaction.add(transaction)
//                } else {
//                    when ((parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }.type) {
//                        true -> if (large.second) listNowYearTransaction.add(transaction)
//                        false -> if (large.first) listNowYearTransaction.add(transaction)
//                    }
//                }
//            }
//        }
        mapperTransactions(listNowYearTransaction).also { monthlyDataList ->
            if (monthlyDataList.sumOf { it.expense + it.income } == 0L) {
                view.findViewById<TextView>(R.id.textViewReportMain).visibility = View.VISIBLE
                view.findViewById<LineChart>(R.id.lineChartReportMain).visibility = View.GONE
            } else {
                view.findViewById<TextView>(R.id.textViewReportMain).visibility = View.GONE
                view.findViewById<LineChart>(R.id.lineChartReportMain).visibility = View.VISIBLE
                initLineChartMain(monthlyDataList)
            }
        }
    }

    private fun mapperTransactions(listTransaction: List<Transaction>): List<MonthlyData> {
        val listMonthlyData: MutableMap<Int, Array<Long>> = mutableMapOf()
        repeat(12) { n ->
            listMonthlyData[n + 1] = arrayOf(0L, 0L)
        }
        listTransaction.forEachIndexed { index, transaction ->
            if (transaction.categoryID in categoriesIdNow.keys) {
                val month = YearMonth.from(
                    LocalDate.parse(
                        transaction.date,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    )
                ).monthValue
                if (categoriesIdNow[transaction.categoryID] == true) {
                    listMonthlyData[month]!![1] += transaction.amount
                } else {
                    listMonthlyData[month]!![0] += transaction.amount
                }
            }
        }
        return listMonthlyData.map { entry ->
            MonthlyData(
                date = YearMonth.parse("${
                    entry.key.let {
                        if (it < 10) "0$it"
                        else it
                    }
                }.${date.value}", DateTimeFormatter.ofPattern("MM.yyyy")),
                expense = entry.value[0],
                income = entry.value[1]
            )
        }
    }

    private fun initLineChartMain(listMonthlyData: List<MonthlyData>) {
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
                        return listMonthlyData.getOrNull(value.toInt())?.let {
                            it.date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        } ?: ""
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
            cubicIntensity = 0.1f
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