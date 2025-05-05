package com.example.financialwunderwaffe.main.analytics.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.analytics.AnalyticsViewModel
import com.example.financialwunderwaffe.retrofit.database.analytics.model.CapitalAnalytics
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class AnalyticsCapitalFragment : Fragment() {
    private lateinit var view: View
    private lateinit var viewModel: AnalyticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_analytics_capital, container, false)
        viewModel = (activity as MainActivity).analyticsViewModel

        view.findViewById<ImageView>(R.id.imageViewBackYearCapitalAnalytics).setOnClickListener {
            viewModel.setDateCapital(viewModel.dateCapital.value!!.minusYears(1))
        }
        view.findViewById<ImageView>(R.id.imageViewForwardYearCapitalAnalytics).setOnClickListener {
            viewModel.setDateCapital(viewModel.dateCapital.value!!.plusYears(1))
        }

        viewModel.capitalByMonth.observe(viewLifecycleOwner) {
            checkData(mapperDataForCombinedChart(it))
        }
        viewModel.dateCapital.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewYearCapitalAnalytics).text =
                it.value.toString()
            checkData(mapperDataForCombinedChart(viewModel.capitalByMonth.value!!))
        }

        return view
    }

    private fun checkData(listData: List<Triple<YearMonth, Long, Double>>) {
        val chart = view.findViewById<CombinedChart>(R.id.combinedChartCapitalAnalytics)
        val textTitle = view.findViewById<TextView>(R.id.textViewCombinedChartCapitalAnalytics)
        if (listData.isEmpty() || listData.sumOf { it.second.toDouble() + it.third } == 0.0) {
            chart.visibility = View.GONE
            textTitle.visibility = View.VISIBLE
        } else {
            chart.visibility = View.VISIBLE
            textTitle.visibility = View.GONE
            initCombinedChart(listData)
        }
    }

    private fun mapperDataForCombinedChart(listCapitalAnalytics: List<CapitalAnalytics>): List<Triple<YearMonth, Long, Double>> {
        val listCapitalNowYear = listCapitalAnalytics.filter {
            Year.parse(
                it.month,
                DateTimeFormatter.ofPattern("MM.yyyy")
            ) == viewModel.dateCapital.value
        }
        if (listCapitalNowYear.isEmpty()) return emptyList()
        val listMonths = mutableMapOf<Int, Pair<Long, Double>>()
        repeat(12) { n ->
            listMonths[n + 1] = Pair(0L, 0.0)
        }
        listCapitalNowYear.forEach { capitalAnalytics ->
            val month = YearMonth.parse(
                capitalAnalytics.month,
                DateTimeFormatter.ofPattern("MM.yyyy")
            ).monthValue
            listMonths[month] =
                listMonths[month]!!.copy(
                    first = capitalAnalytics.listAsset.sumOf { it.amount },
                    second = capitalAnalytics.saveRate
                )
        }
        return listMonths.map { entry ->
            Triple(
                first = YearMonth.parse(
                    "${
                        entry.key.let {
                            if (it < 10) "0$it"
                            else it
                        }
                    }.${viewModel.dateCapital.value!!.value}",
                    DateTimeFormatter.ofPattern("MM.yyyy")
                ),
                second = entry.value.first, third = entry.value.second
            )
        }
    }

    private fun initCombinedChart(listData: List<Triple<YearMonth, Long, Double>>) {
        val chart = view.findViewById<CombinedChart>(R.id.combinedChartCapitalAnalytics)
        // 1. Базовые настройки графика
        chart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawOrder(
                arrayOf(
                    CombinedChart.DrawOrder.BAR,
                    CombinedChart.DrawOrder.LINE
                )
            )
        }

        // 2. Настройка оси X (месяца)
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return listData.getOrNull(value.toInt())?.first?.month?.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    ) ?: ""
                }
            }
            setDrawGridLines(false)
            axisMinimum = -0.5f
            axisMaximum = listData.size - 0.5f
        }

        // 3. Настройка левой оси Y (для BarChart - капитал)
        chart.axisLeft.apply {
            setDrawGridLines(true)
            granularity = 1f
            axisMinimum = listData.minOfOrNull { it.second }?.toFloat()?.times(0.9f) ?: 0f
            axisMaximum = listData.maxOfOrNull { it.second }?.toFloat()?.times(1.1f) ?: 100f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toLong().toString() // Форматирование для Long значений
                }
            }
        }

        // 4. Настройка правой оси Y (для LineChart - норма сбережения %)
        chart.axisRight.apply {
            setDrawGridLines(false)
            granularity = 1f
            axisMinimum = (listData.minOfOrNull { it.third }?.toFloat() ?: 0f) * 0.9f
            axisMaximum = (listData.maxOfOrNull { it.third }?.toFloat() ?: 100f) * 1.1f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.0f%%".format(value) // Форматирование с %
                }
            }
        }

        // 5. Настройка легенды
        chart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            form = Legend.LegendForm.CIRCLE
            textSize = 15f
            isWordWrapEnabled = true
        }

        // 6. Подготовка данных для BarChart (капитал)
        val barEntries = listData.mapIndexed { index, (_, capital, _) ->
            BarEntry(index.toFloat(), capital.toFloat())
        }
        val barDataSet = BarDataSet(barEntries, "Капитал").apply {
            color = Color.rgb(64, 89, 128)
            setDrawValues(false)
            axisDependency = YAxis.AxisDependency.LEFT
        }

        // 7. Подготовка данных для LineChart (норма сбережения)
        val lineEntries = listData.mapIndexed { index, (_, _, savingsRate) ->
            Entry(index.toFloat(), savingsRate.toFloat())
        }
        val lineDataSet = LineDataSet(lineEntries, "Норма сбережения").apply {
            color = Color.rgb(255, 102, 0)
            lineWidth = 2.5f
            setCircleColor(Color.rgb(255, 102, 0))
            circleRadius = 4f
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0f
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        // 8. Собираем все данные вместе
        val combinedData = CombinedData().apply {
            setData(BarData(barDataSet))
            setData(LineData(lineDataSet))
        }

        // 9. Применяем данные и настраиваем отображение
        chart.apply {
            data = combinedData
            moveViewToX(data.xMax) // Прокручиваем к последним данным
            animateXY(1000, 1000)
            invalidate()
        }
    }

}