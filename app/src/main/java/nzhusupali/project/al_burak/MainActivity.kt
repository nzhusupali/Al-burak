package nzhusupali.project.al_burak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tomlonghurst.expandablehinttext.ExpandableHintText
import nzhusupali.project.al_burak.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val send = _binding.button
        val complete = _binding.competedWork

        send.setOnClickListener { addClient() }
        complete.setOnClickListener { startActivity(Intent(this, CompletedWork::class.java)) }
    }

    private fun addClient() {
        val alertDialog = AlertDialog.Builder(this)
        val db = FirebaseFirestore.getInstance()

        alertDialog.setTitle("Добавить клиента")

        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_add_client,null)

        val employee = view.findViewById<ExpandableHintText>(R.id.employee)
        val carName = view.findViewById<ExpandableHintText>(R.id.carName)
        val stateNumber = view.findViewById<ExpandableHintText>(R.id.stateNumber)
        val workType = view.findViewById<ExpandableHintText>(R.id.workType)

        val employee1 = employee.text.toString()
        val carName1 = carName.text.toString()
        val stateNumber1 = stateNumber.text.toString()
        val workType1 = workType.text.toString()

        val user: MutableMap<String,Any> = HashMap()
        user["Employee"] = employee1
        user["Machine brand"] = carName1
        user["State number"] = stateNumber1
        user["Work Type"] = workType1

        alertDialog.setView(view)
            .setPositiveButton("Yes") {_,_ ->
//                val clientAdd = ClientParam(employee1,carName1,stateNumber1,workType1)
                db.collection("client")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.i("Firebase1","Document Snapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.i("Firebase1", "Error adding document", e)
                    }
        }
            .setNegativeButton("No") {_,_ ->
                Log.i("test second", carName1)
                println(carName1)
                println(Log.i("Third Test", carName1))
            }
        val alert = alertDialog.create()
        alert.show()
    }
}
