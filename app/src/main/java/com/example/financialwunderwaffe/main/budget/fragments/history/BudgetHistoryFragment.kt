package com.example.financialwunderwaffe.main.budget.fragments.history

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.financialwunderwaffe.R
import com.example.financialwunderwaffe.main.MainActivity
import com.example.financialwunderwaffe.main.budget.BudgetFragment
import com.example.financialwunderwaffe.retrofit.database.transaction.Transaction
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.appbar.CollapsingToolbarLayout
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class BudgetHistoryFragment : Fragment() {
    private lateinit var view: View
    private var date = YearMonth.parse(LocalDate.now().let { localDate ->
        "${
            localDate.monthValue.let { if (it < 10) "0$it" else it }
        }.${localDate.year}"
    }, DateTimeFormatter.ofPattern("MM.yyyy"))
    private val listTransactionNowMonth = mutableListOf<Transaction>()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionLayoutManager: LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_budget_history, container, false)

        transactionLayoutManager = LinearLayoutManager(activity)
        searchNowMonthTransaction()

        view.findViewById<LinearLayout>(R.id.transaction_type).apply {
            findViewById<LinearLayout>(R.id.linearLayoutExpenseInfo).setOnClickListener {
                getExpense()
            }
            findViewById<LinearLayout>(R.id.linearLayoutIncomeInfo).setOnClickListener {
                getIncome()
            }
            findViewById<ImageView>(R.id.imageViewBackMonth).setOnClickListener {
                date = date.minusMonths(1)
                searchNowMonthTransaction()
            }
            findViewById<ImageView>(R.id.imageViewForwardMonth).setOnClickListener {
                date = date.plusMonths(1)
                searchNowMonthTransaction()
            }
        }

        view.findViewById<ImageView>(R.id.imageViewBackTransactionHistory).setOnClickListener {
            searchNowMonthTransaction()
        }

        return view
    }

    private fun searchNowMonthTransaction() {
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
            "${months[date.monthValue]} ${date.year}"
        listTransactionNowMonth.clear()
        (parentFragment as BudgetFragment).listTransaction.forEach {
            if (YearMonth.from(
                    LocalDate.parse(
                        it.date,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    )
                ) == date
            ) {
                listTransactionNowMonth.add(it)
            }
        }
        initListTransaction(listTransactionNowMonth)
        initTransactionInfo()
    }

    private fun initListTransaction(listTransaction: List<Transaction>) {
        transactionAdapter = TransactionAdapter(
            listTransaction.map { transaction ->
                val category =
                    (parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }
                TransactionState(
                    id = transaction.id,
                    categoryTitle = category.name,
                    amount = transaction.amount.let {
                        if (category.type) "+$it"
                        else "-$it"
                    },
                    date = transaction.date,
                    type = transaction.type,
                    description = transaction.description
                )
            },
            (activity as MainActivity).printToast
        ) { transactionState ->
            if (transactionState.description.isNotEmpty()) {
                (activity as MainActivity).toast(transactionState.description)
            }
        }
        view.findViewById<RecyclerView>(R.id.listTransaction).apply {
            adapter = transactionAdapter
            layoutManager = transactionLayoutManager
        }
    }

    private fun initTransactionInfo() = view.apply {
        findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
        findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility = View.GONE
        findViewById<LinearLayout>(R.id.transaction_type).apply {
            getSumAmount().also {
                findViewById<TextView>(R.id.textViewExpenseAmount).text =
                    it.first.reversed().chunked(3).joinToString(" ").reversed()
                findViewById<TextView>(R.id.textViewIncomeAmount).text =
                    it.second.reversed().chunked(3).joinToString(" ").reversed()
            }
        }
    }

    private fun getSumAmount(): Pair<String, String> {
        val first = listTransactionNowMonth.filter { transaction ->
            (parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }.type.not()
        }.sumOf { it.amount }.toString()
        val second = listTransactionNowMonth.filter { transaction ->
            (parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }.type
        }.sumOf { it.amount }.toString()
        return Pair(first, second)
    }

    private fun getExpense() {
        view.findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
        view.findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility = View.VISIBLE
        listTransactionNowMonth.filter { transaction ->
            (parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }.type.not()
        }.also {
            initPieChart(it, ColorTemplate.PASTEL_COLORS.toList())
            initListTransaction(it)
        }
    }

    private fun getIncome() {
        view.findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
        view.findViewById<CollapsingToolbarLayout>(R.id.pieChartInfo).visibility = View.VISIBLE
        listTransactionNowMonth.filter { transaction ->
            (parentFragment as BudgetFragment).listCategory.first { it.id == transaction.categoryID }.type
        }.also {
            initPieChart(it, ColorTemplate.JOYFUL_COLORS.toList())
            initListTransaction(it)
        }
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
                false -> listTransactions.sumOf { it.amount }.toString()
            }
            setCenterTextColor(Color.BLACK)
            setCenterTextSize(15f)
            val pieData = mutableListOf<PieEntry>()
            val category = mutableListOf<String>()
            listTransactions.groupBy { it.categoryID }.forEach { (categoryId, listTransaction) ->
                pieData.add(PieEntry(listTransaction.sumOf { it.amount }.toFloat()))
                category.add((parentFragment as BudgetFragment).listCategory.first { it.id == categoryId }.name)
            }
            val dataSet = PieDataSet(pieData, "").apply {
                sliceSpace = 5f
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