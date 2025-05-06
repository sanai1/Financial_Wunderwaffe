package com.example.financialwunderwaffe.main.budget.fragments.history

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.LoadingFragment
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.BudgetViewModel
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class BudgetHistoryFragment : Fragment() {
    private lateinit var view: View
    private lateinit var viewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_history, container, false)
        viewModel = (activity as MainActivity).budgetViewModel

        viewModel.transaction.observe(viewLifecycleOwner) {
            searchNowMonthTransaction(it)
        }
        viewModel.dateHistory.observe(viewLifecycleOwner) {
            if (viewModel.transaction.value != null) searchNowMonthTransaction(viewModel.transaction.value!!)
        }
        viewModel.typeTransaction.observe(viewLifecycleOwner) {
            searchNowMonthTransaction(viewModel.transaction.value!!)
        }

        view.findViewById<LinearLayout>(R.id.transaction_type).apply {
            findViewById<LinearLayout>(R.id.linearLayoutExpenseInfo).setOnClickListener {
                viewModel.setTypeTransaction(false)
            }
            findViewById<LinearLayout>(R.id.linearLayoutIncomeInfo).setOnClickListener {
                viewModel.setTypeTransaction(true)
            }
            findViewById<ImageView>(R.id.imageViewBackMonth).setOnClickListener {
                viewModel.setDateHistory(viewModel.dateHistory.value!!.minusMonths(1))
            }
            findViewById<ImageView>(R.id.imageViewForwardMonth).setOnClickListener {
                viewModel.setDateHistory(viewModel.dateHistory.value!!.plusMonths(1))
            }
        }
        view.findViewById<ImageView>(R.id.imageViewBackTransactionHistory).setOnClickListener {
            viewModel.setTypeTransaction(null)
        }

        view.findViewById<FrameLayout>(R.id.container_budget_history).visibility = View.VISIBLE
        childFragmentManager.beginTransaction().apply {
            replace(R.id.container_budget_history, LoadingFragment())
            addToBackStack(null)
            commit()
        }
        view.findViewById<Toolbar>(R.id.toolbarBudgetHistory).visibility = View.GONE
        view.findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility = View.GONE
        check()

        return view
    }

    private fun check() = CoroutineScope(Dispatchers.IO).launch {
        while (viewModel.categories.value == null || viewModel.transaction.value == null) {
            delay(50)
        }
        withContext(Dispatchers.Main) {
            view.findViewById<FrameLayout>(R.id.container_budget_history).visibility = View.GONE
            view.findViewById<Toolbar>(R.id.toolbarBudgetHistory).visibility = View.VISIBLE
        }
    }

    private fun searchNowMonthTransaction(listTransaction: List<Transaction>) {
        val months = mapOf(
            1 to "январь",
            2 to "февраль",
            3 to "март",
            4 to "апрель",
            5 to "май",
            6 to "июнь",
            7 to "июль",
            8 to "август",
            9 to "сентябрь",
            10 to "октябрь",
            11 to "ноябрь",
            12 to "декабрь"
        )
        view.findViewById<LinearLayout>(R.id.transaction_type)
            .findViewById<TextView>(R.id.textViewMonthTransaction).text =
            "${months[viewModel.dateHistory.value!!.monthValue]} ${viewModel.dateHistory.value!!.year}"
        listTransaction.filter {
            YearMonth.parse(
                it.date,
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            ) == viewModel.dateHistory.value
        }.also { transactionByNowYearMonth ->
            transactionByNowYearMonth.filter { transaction ->
                if (viewModel.typeTransaction.value == null) true
                else {
                    val category =
                        viewModel.categories.value?.find { it.id == transaction.categoryID }
                    if (category == null) false
                    else category.type == viewModel.typeTransaction.value
                }
            }.also {
                initListTransaction(it)
                when (viewModel.typeTransaction.value) {
                    null -> {
                        view.findViewById<Toolbar>(R.id.toolbarBudgetHistory).visibility =
                            View.VISIBLE
                        view.findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility =
                            View.GONE
                    }

                    true, false -> {
                        view.findViewById<Toolbar>(R.id.toolbarBudgetHistory).visibility = View.GONE
                        view.findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility =
                            View.VISIBLE
                        initPieChart(it, when (viewModel.typeTransaction.value!!) {
                            true -> mutableListOf<Int>().apply {
                                addAll(ColorTemplate.COLORFUL_COLORS.toList())
                                addAll(ColorTemplate.MATERIAL_COLORS.toList())
                            }

                            false -> mutableListOf<Int>().apply {
                                addAll(ColorTemplate.PASTEL_COLORS.toList())
                                addAll(ColorTemplate.JOYFUL_COLORS.toList())
                            }
                        })
                    }
                }
            }
            initTransactionInfo(transactionByNowYearMonth)
        }
    }

    private fun initListTransaction(listTransaction: List<Transaction>) {
        view.findViewById<RecyclerView>(R.id.listTransaction).apply {
            adapter = TransactionAdapter(
                listTransaction.map { transaction ->
                    val category =
                        viewModel.categories.value?.find { it.id == transaction.categoryID }
                    TransactionState(
                        id = transaction.id,
                        categoryTitle = category?.name ?: "Not title",
                        amount = transaction.amount.let {
                            if (category == null) it.toString()
                            else {
                                if (category.type) "+$it"
                                else "-$it"
                            }
                        },
                        date = transaction.date,
                        type = transaction.type,
                        description = transaction.description
                    )
                }.sortedByDescending {
                    LocalDate.parse(
                        it.date,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    )
                },
                (activity as MainActivity).printToast
            ) { transactionState ->
                if (transactionState.description.isNotEmpty()) {
                    (activity as MainActivity).toast(transactionState.description)
                }
            }
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun initTransactionInfo(listTransaction: List<Transaction>) = view.apply {
        findViewById<LinearLayout>(R.id.transaction_type).apply {
            getSumAmount(listTransaction).also {
                findViewById<TextView>(R.id.textViewExpenseAmount).text =
                    it.first.reversed().chunked(3).joinToString(" ").reversed()
                findViewById<TextView>(R.id.textViewIncomeAmount).text =
                    it.second.reversed().chunked(3).joinToString(" ").reversed()
            }
        }
    }

    private fun getSumAmount(listTransaction: List<Transaction>): Pair<String, String> {
        val first = listTransaction.filter { transaction ->
            viewModel.categories.value?.find { it.id == transaction.categoryID }?.type?.not()
                ?: false
        }.sumOf { it.amount }.toString()
        val second = listTransaction.filter { transaction ->
            viewModel.categories.value?.find { it.id == transaction.categoryID }?.type ?: false
        }.sumOf { it.amount }.toString()
        return Pair(first, second)
    }

    private fun initPieChart(listTransactions: List<Transaction>, colorList: List<Int>) =
        view.findViewById<PieChart>(R.id.pieTransactionHistory).apply {
            description.isEnabled = false
            dragDecelerationFrictionCoef = 15f
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            transparentCircleRadius = 50f
            centerText = when (listTransactions.isEmpty()) {
                true -> ""
                false -> listTransactions.sumOf { it.amount }.toString().reversed().chunked(3)
                    .joinToString(" ").reversed() + "₽"
            }
            setCenterTextColor(Color.BLACK)
            setCenterTextSize(15f)
            val pieData = mutableListOf<PieEntry>()
            val category = mutableListOf<String>()
            listTransactions.groupBy { it.categoryID }.forEach { (categoryId, listTransaction) ->
                pieData.add(PieEntry(listTransaction.sumOf { it.amount }.toFloat()))
                category.add(
                    viewModel.categories.value?.find { it.id == categoryId }?.name ?: "Not Title"
                )
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
                    category.mapIndexed { index, it ->
                        LegendEntry(
                            it,
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