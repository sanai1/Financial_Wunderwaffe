package com.example.financialwunderwaffe.retrofit.database.transaction

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.financialwunderwaffe.R

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val columnId: TextView = itemView.findViewById(R.id.column_id)
        val columnAmount: TextView = itemView.findViewById(R.id.column_amount)
        val columnDate: TextView = itemView.findViewById(R.id.column_date)
        val columnType: TextView = itemView.findViewById(R.id.column_type)
        val columnDescription: TextView = itemView.findViewById(R.id.column_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.columnId.text = transaction.id.toString()
        holder.columnAmount.text = transaction.amount.toString()
        holder.columnDate.text = transaction.date
        holder.columnType.text = if (transaction.type) "Income" else "Expense"
        holder.columnDescription.text = transaction.description
    }

    override fun getItemCount(): Int = transactions.size
}