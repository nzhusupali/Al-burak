package nzhusupali.project.al_burak

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
import nzhusupali.project.al_burak.R.id.carName_edit_preOrder
import nzhusupali.project.al_burak.databinding.ActivityEditItemPreOrderBinding
import java.text.SimpleDateFormat
import java.util.*


class EditPreOrderItem : AppCompatActivity() {
    private lateinit var binding: ActivityEditItemPreOrderBinding
    private var dbName = "preOrder"
    private var logFirestore = "Firestore save preOrder"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemPreOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        autoCompleteCarBrand()
        title = getString(R.string.editOrder)


        val carName: TextView = findViewById(carName_edit_preOrder)
        val stateNumber: TextView = findViewById(R.id.stateNumber_edit_preOrder)
        val sum: TextView = findViewById(R.id.sum_edit_preOrder)
        val phoneNumberClient: TextView = findViewById(R.id.number_edit_preOrder)
        val clientName: TextView = findViewById(R.id.clientName_edit_preOrder)
        val date: TextView = findViewById(R.id.dateBtn_edit_preOrder)
        val workType: TextView = findViewById(R.id.workType_edit_preOrder)

        carName.text = intent.extras!!.getString("carName_edit_preOrder").toString().trim()
        clientName.text = intent.extras!!.getString("clientName_edit_preOrder").toString().trim()
        phoneNumberClient.text = intent.extras!!.getString("phoneNumberClient_edit_preOrder").toString().trim()
        stateNumber.text = intent.extras!!.getString("stateNumber_edit_preOrder").toString().trim()
        sum.text = intent.extras!!.getString("sum_edit_preOrder").toString().trim()
        date.text = intent.extras!!.getString("date_edit_preOrder").toString().trim()
        workType.text = intent.extras!!.getString("workType_edit_preOrder").toString().trim()

        binding.saveEditPreOrder.setOnClickListener {
            saveEdit(carName, clientName, phoneNumberClient, stateNumber, sum, date, workType)
        }
        binding.dateBtnEditPreOrder.setOnClickListener { showDateTimeDialog() }

    }


    private fun saveEdit(
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
        val eCarName = carName.text.toString().trim()
        val eClientName = clientName.text.toString().trim()
        val ePhoneNumberClient = phoneNumberClient.text.toString().trim()
        val eStateNumber = stateNumber.text.toString().trim()
        val eSum = sum.text.toString().trim()
        val eWorkType = workType.text.toString().trim()
        val eDate = date.text.toString().trim()

        // Предыдущий текст для  Firestore
        val whereCarName = intent.extras!!.getString("carName_edit_preOrder").toString().trim()
        val whereClientName = intent.extras!!.getString("clientName_edit_preOrder").toString().trim()
        val wherePhoneNumberClient = intent.extras!!.getString("phoneNumberClient_edit_preOrder").toString().trim()
        val whereStateNumber = intent.extras!!.getString("stateNumber_edit_preOrder").toString().trim()
        val whereDate = intent.extras!!.getString("date_edit_preOrder").toString().trim()
        val whereSum = intent.extras!!.getString("sum_edit_preOrder").toString().trim()

        db.collection(dbName)
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
                        binding.dateBtnEditPreOrder.text = simpleDateFormat.format(calendar.time)

                    }
                TimePickerDialog(
                    this,
                    timeSetListener, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE],
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
        binding.carNameEditPreOrder.setAdapter(arrayAdapter)
    }
}