package com.example.financialwunderwaffe.main.budget.fragments.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financialwunderwaffe.R

class TransactionAdapter(
    private val listTransactionState: List<TransactionState>,
    private val toast: (String) -> Unit,
    private val callback: (TransactionState) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_transaction_budget, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listTransactionState.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(listTransactionState[position])
        holder.click(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val linearLayoutTransaction: LinearLayout =
            view.findViewById(R.id.linearLayoutTransaction)
        private val textViewCategoryTransaction: TextView =
            view.findViewById(R.id.textViewCategoryTransaction)
        private val textViewDateTransaction: TextView =
            view.findViewById(R.id.textViewDateTransaction)
        private val textViewAmountTransaction: TextView =
            view.findViewById(R.id.textViewAmountTransaction)
        private val imageViewTransactionInfo: ImageView =
            view.findViewById(R.id.imageViewTransactionInfo)

        fun click(position: Int) {
            linearLayoutTransaction.setOnClickListener {
                callback(listTransactionState[position])
            }
            imageViewTransactionInfo.setOnClickListener {
                when (listTransactionState[position].amount[0]) {
                    '+' -> toast("Крупный доход")
                    else -> toast("Крупная покупка")
                }
            }
        }

        fun update(transactionState: TransactionState) {
            textViewCategoryTransaction.text = transactionState.categoryTitle
            textViewDateTransaction.text = transactionState.date
            textViewAmountTransaction.apply {
                text = transactionState.amount.let {
                    it[0] + it.substring(1).reversed().chunked(3).joinToString(" ").reversed()
                }
                setTextColor(
                    if (transactionState.amount[0] == '+') Color.GREEN
                    else Color.RED
                )
            }
            when (transactionState.type) {
                true -> imageViewTransactionInfo.visibility = View.VISIBLE
                else -> imageViewTransactionInfo.visibility = View.GONE
            }
        }
    }
}
