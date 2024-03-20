package com.example.pennywise

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailedActivity : AppCompatActivity() {
    private lateinit var amountLayout: TextInputLayout
    private lateinit var labelLayout: TextInputLayout
    private lateinit var closeBtn: ImageButton

    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        amountLayout = findViewById(R.id.amountLayout)
        labelLayout = findViewById(R.id.labelLayout)
        closeBtn = findViewById(R.id.closeBtn)
        val amountInput = findViewById<TextView>(R.id.amountInput)
        val labelInput = findViewById<TextView>(R.id.labelInput)
        val descriptionInput = findViewById<TextView>(R.id.descriptionInput)

        labelInput.text = transaction.label
        amountInput.text = transaction.amount.toString()
        descriptionInput.text = transaction.description

        labelInput.addTextChangedListener {
            if (it != null) {
                if (it.count() > 0)
                    labelLayout.error = null
            }
        }

        amountInput.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty())
                    amountLayout.error = null
            }
        }

        val updateButton = findViewById<Button>(R.id.updateBtn)
        updateButton.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                labelLayout.error = "Please enter a valid label"
            else if (amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                val updatedTransaction = Transaction(transaction.id, label, amount, description)
                update(updatedTransaction)
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
