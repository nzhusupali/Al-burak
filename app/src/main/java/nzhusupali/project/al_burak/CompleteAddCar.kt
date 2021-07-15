package nzhusupali.project.al_burak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.databinding.ActivityCompleteAddCarBinding
import nzhusupali.project.al_burak.fragments.completed.adapters.ClientParamComplete

class CompleteAddCar : AppCompatActivity() {
    private lateinit var _binding: ActivityCompleteAddCarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompleteAddCarBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.addClient.setOnClickListener {
            val employee = _binding.employee.text.toString()
            val carName = _binding.carName.text.toString()
            val stateNumber = _binding.stateNumber.text.toString()
            val sum = _binding.sum.text.toString()
            val phoneNumberClient = _binding.number.text.toString()
            val clientName = _binding.clientName.text.toString()
            val workType = _binding.workType.text.toString()

            addClientCar(
                employee,
                carName,
                stateNumber,
                sum,
                phoneNumberClient,
                clientName,
                workType
            )
        }
    }

    private fun addClientCar(
        employee: String,
        carName: String,
        stateNumber: String,
        sum: String,
        phoneNumberClient: String,
        clientName: String,
        workType: String

    ) {
        val db = FirebaseFirestore.getInstance()
        val addClient= ClientParamComplete(employee,carName,stateNumber,sum,phoneNumberClient,clientName,workType)

        if (employee.isEmpty()){
            _binding.employee.error = getString(R.string.enter_your_name)
            _binding.employee.requestFocus()
            return
        }
        if(carName.isEmpty()){
            _binding.carName.error = getString(R.string.enter_car_name)
            _binding.carName.requestFocus()
            return
        }
        if (stateNumber.isEmpty()){
            _binding.stateNumber.error = getString(R.string.enter_state_number)
            _binding.stateNumber.requestFocus()
            return
        }
        if(sum.isEmpty()){
            _binding.sum.error = getString(R.string.enterPayment)
            _binding.sum.requestFocus()
            return
        }
        if(phoneNumberClient.isEmpty()) {
            _binding.number.error = getString(R.string.enterClientPhoneNumber)
            _binding.number.requestFocus()
            return
        }
        if (clientName.isEmpty()){
            _binding.clientName.error = getString(R.string.enterClientName)
            _binding.clientName.requestFocus()
            return
        }
        if(workType.isEmpty()){
            _binding.workType.error = getString(R.string.enterWorkList)
            _binding.workType.requestFocus()
            return
        }

        db.collection("completedWork")
            .add(addClient)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, getString(R.string.successAdd),Toast.LENGTH_SHORT).show()
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(Intent(this,MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.repeatAdd), Toast.LENGTH_SHORT).show()
            }
    }
}