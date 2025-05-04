package com.example.financialwunderwaffe.main.analytics.fragments.asset

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.analytics.AnalyticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


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
            }.let { it ->
                it.listAsset.map { assetItem ->
                    assetItem.title to assetItem.amount
                }
            })
        }

        return view
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

}