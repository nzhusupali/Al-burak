package nzhusupali.project.al_burak.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ActivityEditCompleteItemBinding
import java.text.SimpleDateFormat
import java.util.*

class EditCompleteItem : AppCompatActivity() {

    private lateinit var binding: ActivityEditCompleteItemBinding
    private var dbName = "completedWork"
    private var logFirestore = "Firestore save complete"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCompleteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        autoCompleteCarBrand()
        title = getString(R.string.editOrder)

        val employee: TextView = findViewById(R.id.employee_edit_complete)
        val carName: TextView = findViewById(R.id.carName_edit_complete)
        val stateNumber: TextView = findViewById(R.id.stateNumber_edit_complete)
        val sum: TextView = findViewById(R.id.sum_edit_complete)
        val phoneNumberClient: TextView = findViewById(R.id.number_edit_complete)
        val clientName: TextView = findViewById(R.id.clientName_edit_complete)
        val date: TextView = findViewById(R.id.dateBtn_edit_complete)
        val workType: TextView = findViewById(R.id.workType_edit_complete)

        employee.text = intent.extras!!.getString("employee_edit_completed").toString().trim()
        carName.text = intent.extras!!.getString("carName_edit_completed").toString().trim()
        stateNumber.text = intent.extras!!.getString("stateNumber_edit_completed").toString().trim()
        sum.text = intent.extras!!.getString("sum_edit_completed").toString().trim()
        phoneNumberClient.text = intent.extras!!.getString("phoneNumber_edit_completed").toString().trim()
        clientName.text = intent.extras!!.getString("clientName_edit_completed").toString().trim()
        date.text = intent.extras!!.getString("date_edit_completed").toString().trim()
        workType.text = intent.extras!!.getString("workType_edit_completed").toString().trim()

        binding.saveEditComplete.setOnClickListener {
            saveEdit(
                employee,
                carName,
                clientName,
                phoneNumberClient,
                stateNumber,
                sum,
                date,
                workType
            )
        }
        binding.dateBtnEditComplete.setOnClickListener { showDateTimeDialog() }


    }

    private fun saveEdit(
        employee: TextView,
        carName: TextView,
        clientName: TextView,
        phoneNumberClient: TextView,
        stateNumber: TextView,
        sum: TextView,
        date: TextView,
        workType: TextView
    ) {
        val db = FirebaseFirestore.getInstance()

        // Текст для замены в Firestore
        val eEmployee = employee.text.toString().trim()
        val eCarName = carName.text.toString().trim()
        val eClientName = clientName.text.toString().trim()
        val ePhoneNumberClient = phoneNumberClient.text.toString().trim()
        val eStateNumber = stateNumber.text.toString().trim()
        val eSum = sum.text.toString().trim()
        val eWorkType = workType.text.toString().trim()
        val eDate = date.text.toString().trim()

        // Предыдущий текст для поиска в Firestore
        val whereEmployee = intent.extras!!.getString("employee_edit_completed").toString().trim()
        val whereCarName = intent.extras!!.getString("carName_edit_completed").toString().trim()
        val whereClientName = intent.extras!!.getString("clientName_edit_completed").toString().trim()
        val wherePhoneNumberClient =
            intent.extras!!.getString("phoneNumber_edit_completed").toString().trim()
        val whereStateNumber = intent.extras!!.getString("stateNumber_edit_completed").toString().trim()
        val whereDate = intent.extras!!.getString("date_edit_completed").toString().trim()
        val whereSum = intent.extras!!.getString("sum_edit_completed").toString().trim()

        db.collection(dbName)
            .whereEqualTo("employee", whereEmployee)
            .whereEqualTo("carName", whereCarName)
            .whereEqualTo("clientName", whereClientName)
            .whereEqualTo("phoneNumberClient", wherePhoneNumberClient)
            .whereEqualTo("stateNumber", whereStateNumber)
            .whereEqualTo("date", whereDate)
            .whereEqualTo("sum", whereSum)
            .get()
            .addOnCompleteListener { p0 ->
                if (p0.isSuccessful && !p0.result!!.isEmpty) {

                    val documentSnapshot = p0.result!!.documents[0]

                    val documentId = documentSnapshot.id

                    val dbUpdate = db.collection(dbName)
                        .document(documentId)
                    dbUpdate.update("employee", eEmployee)
                    dbUpdate.update("carName", eCarName)
                    dbUpdate.update("clientName", eClientName)
                    dbUpdate.update("phoneNumberClient", ePhoneNumberClient)
                    dbUpdate.update("stateNumber", eStateNumber)
                    dbUpdate.update("sum", eSum)
                    dbUpdate.update("workType", eWorkType)
                    dbUpdate.update("date", eDate)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.successfullySaved),
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d(logFirestore, "$documentId successfully saved")
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(Intent(this, MainActivity::class.java))

                        }.addOnFailureListener {

                            Toast.makeText(
                                applicationContext,
                                getString(R.string.repeatSave),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(logFirestore, "$documentId not saved")

                        }
                }
            }
            .addOnFailureListener {
                Log.d(logFirestore, "Not saved")
                Toast.makeText(this, getString(R.string.error_server), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDateTimeDialog() {
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
                        binding.dateBtnEditComplete.text = simpleDateFormat.format(calendar.time)

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

    private fun autoCompleteCarBrand() {
        val cars = resources.getStringArray(R.array.carBrands)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cars)
        binding.carNameEditComplete.setAdapter(arrayAdapter)

    }
}