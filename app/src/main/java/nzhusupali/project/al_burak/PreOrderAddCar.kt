package nzhusupali.project.al_burak

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.databinding.ActivityPreOrderAddCarBinding
import nzhusupali.project.al_burak.fragments.preOrder.adapters.ClientParamPreOrder
import java.text.SimpleDateFormat
import java.util.*

class PreOrderAddCar : AppCompatActivity(), DateSelected {

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
            val date = _binding.dateBtn.text.toString()

            addClient(carName, clientName, phoneNumber, stateNumber, sum,notes,date)
        }
        _binding.dateBtn.setOnClickListener { showDatePicker() }

    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment(this)
        datePickerFragment.show(supportFragmentManager, "datePicker")
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

        val addClient = ClientParamPreOrder(carName, clientName, phoneNumber, stateNumber, sum, notes,date)

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

        db.collection("preOrder")
            .add(addClient)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, getString(R.string.successAdd), Toast.LENGTH_SHORT)
                    .show()
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

    class DatePickerFragment(private val dateSelected: DateSelected) : DialogFragment(),
        DatePickerDialog.OnDateSetListener{
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month  = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            Log.d("DatePicker: ", "Got the date ${calendar}}")

            return DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            dateSelected.receiveDate(year, month, dayOfMonth)
            Log.d("DatePicker: ", "Got the date ${dateSelected.receiveDate(year, month, dayOfMonth)}")
        }

    }

    override fun receiveDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)

//        val formatter = SimpleDateFormat("MMMM dd yyyy hh:mm", Locale.US)
        val viewFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("RU"))
        val viewFormattedDate = viewFormatter.format(calendar.time)
        _binding.dateBtn.text = viewFormattedDate
    }


}
interface DateSelected {
    fun receiveDate(year: Int,month: Int,dayOfMonth: Int)
}
