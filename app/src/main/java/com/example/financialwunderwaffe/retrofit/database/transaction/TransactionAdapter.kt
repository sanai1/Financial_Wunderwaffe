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
        val amountTextView: TextView = itemView.findViewById(R.id.textViewAmount)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val typeTextView: TextView = itemView.findViewById(R.id.textViewType)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.amountTextView.text = "Amount: ${transaction.amount}"
        holder.dateTextView.text = "Date: ${transaction.date}"
        holder.typeTextView.text = if (transaction.type) "Type: Income" else "Type: Expense"
        holder.descriptionTextView.text = "Description: ${transaction.description}"
    }

    override fun getItemCount(): Int = transactions.size
}