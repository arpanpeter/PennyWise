package com.example.pennywise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var transactions: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var balance: TextView
    private lateinit var budget: TextView
    private lateinit var expense: TextView
    private lateinit var save: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf(
            Transaction("WeekEnd Budget", amount = 400.00),
            Transaction("Bananas", amount = -4.00),
            Transaction("Gasoline", amount = -40.00),
            Transaction("Breakfast", amount = -9.99),
            Transaction("Car Parking", amount = -8.00),
        )

        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }
        updateDashboard()
        setOnClickListeners()
    }

    private fun updateDashboard() {
        val totalAmount = transactions.sumOf { it.amount }
        val budgetAmount = transactions.filter { it.amount > 0 }.sumOf { it.amount }
        val expenseAmount = totalAmount - budgetAmount

        balance = findViewById(R.id.balance)
        budget = findViewById(R.id.budget)
        expense = findViewById(R.id.expense)

        balance.text = "Rs %.2f".format(totalAmount)
        budget.text = "Rs %.2f".format(budgetAmount)
        expense.text = "Rs %.2f".format(expenseAmount)
    }

    private fun setOnClickListeners() {
        save = findViewById(R.id.addBtn)
        save.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }
}
