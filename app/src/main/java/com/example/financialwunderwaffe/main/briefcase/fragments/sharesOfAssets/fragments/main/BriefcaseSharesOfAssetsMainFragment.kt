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
        shareOfAssetsAdapter = ShareOfAssetsAdapter(listStateSharesOfAssetsState, onStateClickShareOfAssets)
        recyclerViewSharesOfAssets.adapter = shareOfAssetsAdapter
        recyclerViewSharesOfAssets.layoutManager = layoutManagerShareOfAssets
    }

    private fun initPieChart() {
        // описание диаграммы
        pieChart.description.isEnabled = false

        // ?
        pieChart.dragDecelerationFrictionCoef = 0.99f

        // полость внутри
        pieChart.isDrawHoleEnabled = true
        // цвет полости
        pieChart.setHoleColor(Color.rgb(255,255,255))
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

        val dataSet = PieDataSet(pieData, "Hello")
        // расстояние между дугами
        dataSet.sliceSpace = 10f
        // ? - уменьшается диаграмма при увеличении значения
        dataSet.selectionShift = 10f
        // установка цвета диг диаграммы
        dataSet.colors = listStateSharesOfAssetsState.map {
            Color.rgb(
                it.color[0],
                it.color[1],
                it.color[2]
            )
        }.toMutableList()

        // легенда
        pieChart.legend.isEnabled = false
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