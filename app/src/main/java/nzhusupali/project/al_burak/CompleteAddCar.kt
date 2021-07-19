package nzhusupali.project.al_burak.activity.completedWork

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ActivityCompleteAddCarBinding
import nzhusupali.project.al_burak.fragments.completed.adapters.ClientParamComplete
import java.text.SimpleDateFormat
import java.util.*

class CompleteAddCar : AppCompatActivity(), DateSelected {

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
            val date = _binding.dateBtn.text.toString()
            val workType = _binding.workType.text.toString()

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
        _binding.dateBtn.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragmentCompleted(this)
        datePickerFragment.show(supportFragmentManager, "datePicker")
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
        val addClient= ClientParamComplete(employee,carName,stateNumber,sum,phoneNumberClient,clientName,date,workType)

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
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.repeatAdd), Toast.LENGTH_SHORT).show()
            }
    }

    class DatePickerFragmentCompleted(private val dateSelected: DateSelected ) : DialogFragment(),
        DatePickerDialog.OnDateSetListener{
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(requireContext(),this,year,month, dayOfMonth)
        }
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            dateSelected.receiveDate(year, month, dayOfMonth)
        }
    }

    override fun receiveDate(year: Int,month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

        val viewFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("RU"))
        val viewFormattedDate = viewFormatter.format(calendar.time)
        _binding.dateBtn.text = viewFormattedDate
    }
}
interface DateSelected{
    fun receiveDate(year: Int,month: Int,dayOfMonth: Int)
}
