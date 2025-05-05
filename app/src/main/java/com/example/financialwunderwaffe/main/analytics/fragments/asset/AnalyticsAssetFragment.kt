package com.example.financialwunderwaffe.main.analytics.fragments.asset

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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs


class AnalyticsAssetFragment : Fragment() {
    private lateinit var viewModel: AnalyticsViewModel
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_analytics_asset, container, false)
        viewModel = (activity as MainActivity).analyticsViewModel

        viewModel.capitalByMonth.observe(viewLifecycleOwner) { capitalByMonth ->
            initPieChart(capitalByMonth.maxBy {
                YearMonth.parse(
                    it.month,
                    DateTimeFormatter.ofPattern("MM.yyyy")
                )
            }.let {
                it.listAsset.map { assetItem ->
                    assetItem.title to assetItem.amount
                }
            })
            checkDataForLineChart(mapperDataForLineChart(capitalByMonth))
        }

        viewModel.dateAsset.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewYearBarChart).text =
                viewModel.dateAsset.value!!.value.toString()
            checkDataForLineChart(mapperDataForLineChart(viewModel.capitalByMonth.value!!))
        }

        view.findViewById<ImageView>(R.id.imageViewBackYearBarChart).setOnClickListener {
            viewModel.setDateAsset(viewModel.dateAsset.value!!.minusYears(1))
        }
        view.findViewById<ImageView>(R.id.imageViewForwardYearBarChart).setOnClickListener {
            viewModel.setDateAsset(viewModel.dateAsset.value!!.plusYears(1))
        }

        return view
    }

    private fun checkDataForLineChart(listData: List<Pair<YearMonth, Array<Pair<String, Long>>>>) {
        if (listData.isEmpty()) {
            view.findViewById<TextView>(R.id.textViewLineChartAssetAnalytics).visibility =
                View.VISIBLE
            view.findViewById<LineChart>(R.id.lineChartAssetAnalytics).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.textViewLineChartAssetAnalytics).visibility = View.GONE
            view.findViewById<LineChart>(R.id.lineChartAssetAnalytics).visibility = View.VISIBLE
            initLineChart(listData)
        }
    }

    private fun initPieChart(listAssets: List<Pair<String, Long>>) =
        view.findViewById<PieChart>(R.id.pieChartAssetAnalytics).apply {
            val colorList = mutableListOf<Int>().apply {
                addAll(ColorTemplate.MATERIAL_COLORS.toList())
                addAll(ColorTemplate.JOYFUL_COLORS.toList())
                addAll(ColorTemplate.PASTEL_COLORS.toList())
                addAll(ColorTemplate.COLORFUL_COLORS.toList())
            }
            description.isEnabled = false
            dragDecelerationFrictionCoef = 15f
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            transparentCircleRadius = 50f
            centerText = when (listAssets.isEmpty()) {
                true -> ""
                false -> listAssets.sumOf { it.second }.toString()
            }
            setCenterTextColor(Color.BLACK)
            setCenterTextSize(20f)
            val pieData = mutableListOf<PieEntry>()
            listAssets.forEach {
                pieData.add(PieEntry(it.second.toFloat()))
            }
            val dataSet = PieDataSet(pieData, "").apply {
                sliceSpace = 1f
                selectionShift = 10f
                colors = colorList
                setDrawValues(false)
            }
            legend.apply {
                isEnabled = true
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                isWordWrapEnabled = true
                textColor = Color.BLACK
                textSize = 15f
                setCustom(
                    listAssets.mapIndexed { index, it ->
                        LegendEntry(
                            it.first,
                            Legend.LegendForm.CIRCLE,
                            15f,
                            5f,
                            null,
                            colorList[index % colorList.size]
                        )
                    }
                )
            }
            setUsePercentValues(true)
            data = PieData(dataSet)
            data.apply {
                setValueTextColor(Color.BLACK)
                setValueTextSize(25f)
            }
            animateXY(1500, 1500)
        }

    private fun mapperDataForLineChart(listCapitalAnalytics: List<CapitalAnalytics>): List<Pair<YearMonth, Array<Pair<String, Long>>>> {
        val listCapitalNowYear = listCapitalAnalytics.filter {
            Year.parse(
                it.month,
                DateTimeFormatter.ofPattern("MM.yyyy")
            ) == viewModel.dateAsset.value
        }
        if (listCapitalNowYear.isEmpty()) return emptyList()
        val listLines = mutableMapOf<Int, Array<Pair<String, Long>>>()
        val cntLines = listCapitalNowYear.maxOf { it.listAsset.size }
        repeat(12) { n ->
            listLines[n + 1] = Array(cntLines) { Pair("", 0L) }
        }
        listCapitalNowYear.forEach { capitalAnalytics ->
            val month = YearMonth.parse(
                capitalAnalytics.month,
                DateTimeFormatter.ofPattern("MM.yyyy")
            ).monthValue
            capitalAnalytics.listAsset.forEachIndexed { index, assetAnalytics ->
                listLines[month]!![index] = assetAnalytics.title to assetAnalytics.amount
            }
        }
        return listLines.map { entry ->
            Pair(first = YearMonth.parse("${
                entry.key.let {
                    if (it < 10) "0$it"
                    else it
                }
            }.${viewModel.dateAsset.value!!.value}", DateTimeFormatter.ofPattern("MM.yyyy")),
                second = entry.value
            )
        }
    }

    private fun initLineChart(listData: List<Pair<YearMonth, Array<Pair<String, Long>>>>) {
        val chart = view.findViewById<LineChart>(R.id.lineChartAssetAnalytics)
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
                isWordWrapEnabled = true
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                textColor = Color.DKGRAY
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return listData.getOrNull(value.toInt())?.first?.month?.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        ) ?: ""
                    }
                }
                textSize = 10f
                axisMinimum = -0.5f
                axisMaximum = listData.size - 0.5f
            }

            // 4. Настройка оси Y
            axisLeft.apply {
                setDrawGridLines(true)
                granularity = 1f
                textSize = 10f
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
        }
        val colorList = ColorTemplate.COLORFUL_COLORS.toList()
        // 5. Собираем все уникальные названия активов
        val assetNames = listData.flatMap { (_, assets) ->
            assets.map { it.first }
        }.distinct()

        // 6. Подготовка данных для каждой линии (по имени актива)
        val lineDataSets = assetNames.map { assetName ->
            // Создаем точки для текущего актива
            val entries = listData.mapIndexed { dataIndex, (_, assets) ->
                Entry(
                    dataIndex.toFloat(),
                    assets.find { it.first == assetName }?.second?.toFloat() ?: 0f
                )
            }

            LineDataSet(entries, assetName).apply {  // Используем имя актива как label
                color =
                    colorList[abs(assetName.hashCode()) % colorList.size]  // Уникальный цвет для каждого актива
                lineWidth = 2f
                setDrawCircles(true)
                circleRadius = 4f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.2f
            }
        }

        // 7. Применяем данные
        chart.data = LineData(lineDataSets)

        // 8. Анимация и обновление
        chart.animateX(1000)
        chart.animateY(1000)
        chart.invalidate()
    }

}