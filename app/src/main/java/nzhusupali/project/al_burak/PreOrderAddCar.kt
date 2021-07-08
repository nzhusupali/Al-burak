package nzhusupali.project.al_burak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.databinding.ActivityCompleteAddCarBinding
import nzhusupali.project.al_burak.databinding.ActivityPreOrderAddCarBinding
import nzhusupali.project.al_burak.utils.ClientParam

class PreOrderAddCar : AppCompatActivity() {
    private lateinit var _binding: ActivityPreOrderAddCarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreOrderAddCarBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        _binding.addClient.setOnClickListener {
            val carName = _binding.carNameComplete.text.toString()
            val clientName = _binding.clientNameComplete.text.toString()
            val phoneNumber = _binding.numberComplete.text.toString()
            val stateNumber = _binding.stateNumberComplete.text.toString()
            val sum = _binding.sumComplete.text.toString()
            val notes = _binding.notesComplete.text.toString()

            addClient(carName, clientName, phoneNumber, stateNumber, sum, notes)
        }
    }

    private fun addClient(
        carName: String,
        clientName: String,
        phoneNumber: String,
        stateNumber: String,
        sum: String,
        notes: String
    ) {
        val db = FirebaseFirestore.getInstance()
        val addClient = ClientParam(carName, clientName, phoneNumber, stateNumber, sum, notes)

        if (carName.isEmpty()) {
            _binding.carNameComplete.error = getString(R.string.enter_car_name)
            _binding.carNameComplete.requestFocus()
            return
        }
        if (clientName.isEmpty()) {
            _binding.clientNameComplete.error = getString(R.string.enterClientName)
            _binding.clientNameComplete.requestFocus()
            return
        }
        if (phoneNumber.isEmpty()) {
            _binding.numberComplete.error = getString(R.string.enterClientPhoneNumber)
            _binding.numberComplete.requestFocus()
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
        if (notes.isEmpty()) {
            _binding.notesComplete.error = getString(R.string.enterWorkList)
            _binding.notesComplete.requestFocus()
            return
        }

        db.collection("Предзаказы")
            .add(addClient)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Машина успешно добавлена", Toast.LENGTH_SHORT)
                    .show()
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Повторите попытку заново ",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}