package nzhusupali.project.al_burak.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ActivityPreOrderAddCarBinding
import nzhusupali.project.al_burak.fragments.preOrder.adapters.ClientParamPreOrder
import java.text.SimpleDateFormat
import java.util.*


class PreOrderAddCar : AppCompatActivity() {

    private lateinit var _binding: ActivityPreOrderAddCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreOrderAddCarBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        autoCompleteCarBrand()
        title = getString(R.string.addPreOrder)


        _binding.addClientPreOrder.setOnClickListener {
            val carName = _binding.carNamePreOrder.text.toString()
            val clientName = _binding.clientNamePreOrder.text.toString()
            val phoneNumber = _binding.numberPreOrder.text.toString()
            val stateNumber = _binding.stateNumberPreOrder.text.toString()
            val sum = _binding.sumPreOrder.text.toString()
            val notes = _binding.notesPreOrder.text.toString()
            val date = _binding.dateBtnPreOrder.text.toString()

            addClient(carName, clientName, phoneNumber, stateNumber, sum, notes, date)
        }
        _binding.dateBtnPreOrder.setOnClickListener {
            showDateTimeDialog()
        }

    }

    private fun autoCompleteCarBrand() {
        val cars = resources.getStringArray(R.array.carBrands)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cars)
        _binding.carNamePreOrder.setAdapter(arrayAdapter)
    }

    private fun addClient(
        carName: String,
        clientName: String,
        phoneNumber: String,
        stateNumber: String,
        sum: String,
        notes: String,
        date: String
    ) {
        val db = FirebaseFirestore.getInstance()

        val addClient =
            ClientParamPreOrder(carName, clientName, phoneNumber, stateNumber, sum, notes, date)

        if (carName.isEmpty()) {
            _binding.carNamePreOrder.error = getString(R.string.enter_car_name)
            _binding.carNamePreOrder.requestFocus()
            return
        }
        if (clientName.isEmpty()) {
            _binding.clientNamePreOrder.error = getString(R.string.enterClientName)
            _binding.clientNamePreOrder.requestFocus()
            return
        }
        if (phoneNumber.isEmpty()) {
            _binding.numberPreOrder.error = getString(R.string.enterClientPhoneNumber)
            _binding.numberPreOrder.requestFocus()
            return
        }
        if (stateNumber.isEmpty()) {
            _binding.stateNumberPreOrder.error = getString(R.string.enter_state_number)
            _binding.stateNumberPreOrder.requestFocus()
            return
        }
        if (sum.isEmpty()) {
            _binding.sumPreOrder.error = getString(R.string.enterPayment)
            _binding.sumPreOrder.requestFocus()
            return
        }
        if (notes.isEmpty()) {
            _binding.notesPreOrder.error = getString(R.string.enterWorkList)
            _binding.notesPreOrder.requestFocus()
            return
        }

        db.collection("preOrder")
            .add(addClient)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, getString(R.string.successAdd), Toast.LENGTH_SHORT).show()
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.repeatAdd),
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun showDateTimeDialog() {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val timeSetListener =
                    OnTimeSetListener { _, hourOfDay, minute ->
                        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                        calendar[Calendar.MINUTE] = minute

                        val simpleDateFormat =
                            SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.forLanguageTag("RU"))
                        _binding.dateBtnPreOrder.text = simpleDateFormat.format(calendar.time)

                        /** Если вместо EditText или TextView используем Button то пишем код снизу
                        val viewFormat = simpleDateFormat.format(calendar.time)
                        _binding.dateBtn.text = viewFormat
                         **/
                    }
                TimePickerDialog(
                    this, timeSetListener,
                    calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], true
                ).show()
            }
        DatePickerDialog(
            this,
            dateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}