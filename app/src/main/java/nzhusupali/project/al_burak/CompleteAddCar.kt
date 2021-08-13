package nzhusupali.project.al_burak

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.databinding.ActivityCompleteAddCarBinding
import nzhusupali.project.al_burak.fragments.completed.adapters.ClientParamComplete
import java.text.SimpleDateFormat
import java.util.*

class CompleteAddCar : AppCompatActivity() {

    private lateinit var _binding: ActivityCompleteAddCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompleteAddCarBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        autoCompleteCarBrand()

        title = getString(R.string.addComplete)

        _binding.addClientComplete.setOnClickListener {
            val employee = _binding.employeeComplete.text.toString()
            val carName = _binding.carNameComplete.text.toString()
            val stateNumber = _binding.stateNumberComplete.text.toString()
            val sum = _binding.sumComplete.text.toString()
            val phoneNumberClient = _binding.numberComplete.text.toString()
            val clientName = _binding.clientNameComplete.text.toString()
            val date = _binding.dateBtnComplete.text.toString()
            val workType = _binding.workTypeComplete.text.toString()

            addClientCar(
                employee,
                carName,
                stateNumber,
                sum,
                phoneNumberClient,
                clientName,
                date,
                workType
            )
        }
        _binding.dateBtnComplete.setOnClickListener {
            showDateTimePicker()
        }
    }

    private fun autoCompleteCarBrand() {
        val carBrand = resources.getStringArray(R.array.carBrands)
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,carBrand)
        _binding.carNameComplete.setAdapter(arrayAdapter)
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                        calendar[Calendar.MINUTE] = minute

                        val simpleDateFormat =
                            SimpleDateFormat("dd MMMM yyyy hh:mm", Locale.forLanguageTag("RU"))
                        _binding.dateBtnComplete.text = simpleDateFormat.format(calendar.time)

                        /**
                         * If we use Button, we need write ->
                         */
                        /**
                        val viewFormat = simpleDateFormat.format(calendar.time)
                        _binding.dateBtn.text = viewFormat
                         */
                    }
                TimePickerDialog(
                    this,
                    timeSetListener,
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE],
                    true
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

    private fun addClientCar(
        employee: String,
        carName: String,
        stateNumber: String,
        sum: String,
        phoneNumberClient: String,
        clientName: String,
        date: String,
        workType: String

    ) {
        val db = FirebaseFirestore.getInstance()
        val addClient = ClientParamComplete(carName, clientName, phoneNumberClient, stateNumber, sum, workType, date, employee)
        if (employee.isEmpty()) {
            _binding.employeeComplete.error = getString(R.string.enter_your_name)
            _binding.employeeComplete.requestFocus()
            return
        }
        if (carName.isEmpty()) {
            _binding.carNameComplete.error = getString(R.string.enter_car_name)
            _binding.carNameComplete.requestFocus()
            return
        }
        if (stateNumber.isEmpty()) {
            _binding.stateNumberComplete.error = getString(R.string.enter_state_number)
            _binding.stateNumberComplete.requestFocus()
            return
        }
        if (sum.isEmpty()) {
            _binding.sumComplete.error = getString(R.string.enterPayment)
            _binding.sumComplete.requestFocus()
            return
        }
        if (phoneNumberClient.isEmpty()) {
            _binding.numberComplete.error = getString(R.string.enterClientPhoneNumber)
            _binding.numberComplete.requestFocus()
            return
        }
        if (clientName.isEmpty()) {
            _binding.clientNameComplete.error = getString(R.string.enterClientName)
            _binding.clientNameComplete.requestFocus()
            return
        }
        if (workType.isEmpty()) {
            _binding.workTypeComplete.error = getString(R.string.enterWorkList)
            _binding.workTypeComplete.requestFocus()
            return
        }

        db.collection("completedWork")
            .add(addClient)
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.successAdd),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.repeatAdd), Toast.LENGTH_SHORT).show()
            }
    }
}