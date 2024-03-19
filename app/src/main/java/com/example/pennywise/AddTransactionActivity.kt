package com.example.pennywise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var amountLayout: TextInputLayout
    private lateinit var labelLayout: TextInputLayout
    private lateinit var closeBtn:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        amountLayout = findViewById(R.id.amountLayout)
        labelLayout = findViewById(R.id.labelLayout)
        closeBtn=findViewById(R.id.closeBtn)

        val amountInput = findViewById<TextView>(R.id.amountInput)
        val labelInput = findViewById<TextView>(R.id.labelInput)
        val descriptionInput = findViewById<TextView>(R.id.descriptionInput)


        labelInput.addTextChangedListener {
            if (it!!.count() > 0)
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            if (it!!.count() > 0)
                amountLayout.error = null
        }

        val addTransactionBtn = findViewById<Button>(R.id.addTransactionBtn)
        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                labelLayout.error = "Please enter a valid label"
            else if (amount == null)
                amountLayout.error = "Please enter a valid amount"
             else {
                 val transaction = Transaction(0, label, amount, description)
                 insert(transaction)
             }
        }


        closeBtn.setOnClickListener {
            finish()
        }
    }

     private fun insert(transaction: Transaction){
         val db = Room.databaseBuilder(this,
             AppDatabase::class.java,
             "transactions").build()

         GlobalScope.launch {
             db.transactionDao().insertAll(transaction)
             finish()
         }
     }
}
