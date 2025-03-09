package com.example.financialwunderwaffe.main.briefcase.fragments.sharesOfAssets.fragments.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class BriefcaseSharesOfAssetsMainFragment : Fragment() {

    private lateinit var shareOfAssetsAdapter: ShareOfAssetsAdapter
    private lateinit var recyclerViewSharesOfAssets: RecyclerView
    private lateinit var pieChart: PieChart
    private lateinit var layoutManagerShareOfAssets: RecyclerView.LayoutManager
    var listStateSharesOfAssetsState: List<SharesOfAssetsState> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_briefcase_shares_of_assets_main, container, false)

        recyclerViewSharesOfAssets = view.findViewById(R.id.list_shares_of_assets)
        pieChart = view.findViewById(R.id.pieChartSharesOfAssets)
        layoutManagerShareOfAssets = LinearLayoutManager(activity)
//        layoutManagerShareOfAssets = object : LinearLayoutManager((activity as MainActivity).context) {
//            override fun canScrollVertically() = false
//        }
        println(listStateSharesOfAssetsState)
        initPieChart()
        initRecycleViewSharesOfAssets()

        return view
    }

    private fun initRecycleViewSharesOfAssets() {
        val onStateClickShareOfAssets = object : ShareOfAssetsAdapter.OnStateClickShareOfAssets {
            override fun onStateClickShareOfAssets(sharesOfAssetsState: SharesOfAssetsState, position: Int) {
                (activity as MainActivity).toast("Нажали на Item - ${sharesOfAssetsState.typeAssets}")
            }
        }
        println(listStateSharesOfAssetsState)
        shareOfAssetsAdapter = ShareOfAssetsAdapter(listStateSharesOfAssetsState, onStateClickShareOfAssets)
        recyclerViewSharesOfAssets.adapter = shareOfAssetsAdapter
        recyclerViewSharesOfAssets.layoutManager = layoutManagerShareOfAssets
    }

    private fun initPieChart() {
        // описание диаграммы
        pieChart.description.isEnabled = false

        // ?
        pieChart.dragDecelerationFrictionCoef = 15f

        // полость внутри
        pieChart.isDrawHoleEnabled = true
        // цвет полости
        pieChart.setHoleColor(Color.TRANSPARENT)
        // ? - появляется подобие тени после 60f
        pieChart.transparentCircleRadius = 50f
        // текст в полости
        pieChart.centerText = "1000р"
        // цвет текста в полости
        pieChart.setCenterTextColor(Color.BLACK)
        // размер текста в полости
        pieChart.setCenterTextSize(25f)

        // список значений на диаграмме
        val pieData = mutableListOf<PieEntry>()
        listStateSharesOfAssetsState.forEach {
            pieData.add(PieEntry(it.goal.toFloat()/100))
        }

        val dataSet = PieDataSet(pieData, "")
        // расстояние между дугами
        dataSet.sliceSpace = 5f
        // ? - уменьшается диаграмма при увеличении значения
        dataSet.selectionShift = 10f
        // установка цвета диг диаграммы
        var colorList: MutableList<Int> = mutableListOf()
        listStateSharesOfAssetsState.forEach {
            colorList.add(
                Color.rgb(
                    it.color[0],
                    it.color[1],
                    it.color[2]
                )
            )
        }
        dataSet.colors = colorList
        // текст значений на дугах
        dataSet.setDrawValues(false)

        // легенда
        pieChart.legend.also { it ->
            // показ
            it.isEnabled = true
            // ориентация по вертикали
            it.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
            // ориентация по горизонтале
            it.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            // ориентация
            it.orientation = Legend.LegendOrientation.VERTICAL
            // форма меток
//            it.form = Legend.LegendForm.CIRCLE
            // цвет текста
            it.textColor = Color.BLACK
            // размер текста
            it.textSize = 15f
            // установка данных
            it.setCustom(
                listStateSharesOfAssetsState.map {
                    LegendEntry(
                        it.typeAssets,
                        Legend.LegendForm.CIRCLE,
                        15f,
                        5f,
                        null,
                        Color.rgb(
                            it.color[0],
                            it.color[1],
                            it.color[2]
                        )
                    )
                }
            )
        }
        // показ процентов на дугах
        pieChart.setUsePercentValues(true)

        // передача значений для построения диаграммы
        pieChart.data = PieData(dataSet)
        // цвет текста на дугах диаграммы
        pieChart.data.setValueTextColor(Color.BLACK)
        // размер текста на диаграмме
        pieChart.data.setValueTextSize(25f)

        // установка времени для анимации
        pieChart.animateXY(2000,2000)
    }

}