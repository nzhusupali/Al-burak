package nzhusupali.project.al_burak.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ActivityEndPreOrderBinding
import nzhusupali.project.al_burak.fragments.completed.adapters.ClientParamComplete

class ActivityEndPreOrder : AppCompatActivity() {
    private lateinit var binding: ActivityEndPreOrderBinding

    private var dbName = "preOrder"
    private var dbName1 = "completedWork"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndPreOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.endPreOrder)



        val employee: TextView = findViewById(R.id.ET_employee_end_preOrder)
        val carName: TextView = findViewById(R.id.ET_carName_end_preOrder)
        val stateNumber: TextView = findViewById(R.id.ET_stateNumber_end_preOrder)
        val sum: TextView = findViewById(R.id.ET_sum_end_preOrder)
        val phoneNumberClient: TextView = findViewById(R.id.ET_number_end_preOrder)
        val clientName: TextView = findViewById(R.id.ET_clientName_end_preOrder)
        val date: TextView = findViewById(R.id.ET_date_end_preOrder)
        val workType: TextView = findViewById(R.id.ET_workType_end_preOrder)

        carName.text = intent.extras!!.getString("carName_end_preOrder").toString().trim()
        clientName.text = intent.extras!!.getString("clientName_end_preOrder").toString().trim()
        phoneNumberClient.text = intent.extras!!.getString("phoneNumberClient_end_preOrder").toString().trim()
        stateNumber.text = intent.extras!!.getString("stateNumber_end_preOrder").toString().trim()
        sum.text = intent.extras!!.getString("sum_end_preOrder").toString().trim()
        date.text = intent.extras!!.getString("date_end_preOrder").toString().trim()
        workType.text = intent.extras!!.getString("workType_end_preOrder").toString().trim()

        binding.btnSendEndPreOrder.setOnClickListener {
            sendComplete(employee,carName, clientName, phoneNumberClient, stateNumber, sum, date, workType)
        }



    }

    private fun sendComplete(
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
        val eEmployee = employee.text.toString()
        val eCarName = carName.text.toString().trim()
        val eClientName = clientName.text.toString().trim()
        val ePhoneNumberClient = phoneNumberClient.text.toString().trim()
        val eStateNumber = stateNumber.text.toString().trim()
        val eSum = sum.text.toString().trim()
        val eWorkType = workType.text.toString().trim()
        val eDate = date.text.toString().trim()

        // Предыдущий текст для  Firestore
        val whereCarName = intent.extras!!.getString("carName_end_preOrder").toString().trim()
        val whereClientName = intent.extras!!.getString("clientName_end_preOrder").toString().trim()
        val wherePhoneNumberClient = intent.extras!!.getString("phoneNumberClient_end_preOrder").toString().trim()
        val whereStateNumber = intent.extras!!.getString("stateNumber_end_preOrder").toString().trim()
        val whereDate = intent.extras!!.getString("date_end_preOrder").toString().trim()
        val whereSum = intent.extras!!.getString("sum_end_preOrder").toString().trim()

        val addClient = ClientParamComplete(eCarName, eClientName, ePhoneNumberClient, eStateNumber, eSum, eWorkType, eDate, eEmployee)

        db.collection(dbName1)
            .add(addClient)
            .addOnSuccessListener {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                db.collection(dbName)
                    .whereEqualTo("carName", whereCarName)
                    .whereEqualTo("clientName", whereClientName)
                    .whereEqualTo("phoneNumberClient", wherePhoneNumberClient)
                    .whereEqualTo("stateNumber", whereStateNumber)
                    .whereEqualTo("date", whereDate)
                    .whereEqualTo("sum", whereSum)
                    .get()
                    .addOnCompleteListener { p0 ->

                        if(p0.isSuccessful && !p0.result!!.isEmpty) {

                            val documentSnapshot = p0.result!!.documents[0]

                            val documentId = documentSnapshot.id

                            db.collection(dbName)
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, getString(R.string.congratulationsNurislam), Toast.LENGTH_LONG).show()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(applicationContext, getString(R.string.repeatAdd), Toast.LENGTH_LONG).show()
                                }


                        }


                    }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, getString(R.string.repeatAdd), Toast.LENGTH_SHORT).show()
            }

    }
}