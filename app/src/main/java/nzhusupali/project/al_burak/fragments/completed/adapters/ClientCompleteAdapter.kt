package nzhusupali.project.al_burak.fragments.completed.adapters

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.R

class ClientCompleteAdapter(private val userList: ArrayList<ClientParamComplete>) :
    RecyclerView.Adapter<ClientCompleteAdapter.MyViewHolder>() {
    private lateinit var database: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_list_complete,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        database = FirebaseFirestore.getInstance()

        val user: ClientParamComplete = userList[position]
        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum

        holder.btn.setOnClickListener { v ->
            val context = v.rootView.context
            val ffe = "Firebase error"
            val db = FirebaseFirestore.getInstance()
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.informationCompleted))

            val view =
                LayoutInflater.from(context).inflate(R.layout.detail_client_list, null)

            val employee = view.findViewById<TextView>(R.id.employee_cng)
            val carName = view.findViewById<TextView>(R.id.carName_cng)
            val stateNumber = view.findViewById<TextView>(R.id.stateNumber_cng)
            val sum = view.findViewById<TextView>(R.id.sum_cng)
            val phoneNumberClient = view.findViewById<TextView>(R.id.phoneNumber_cng)
            val clientName = view.findViewById<TextView>(R.id.clientName_cng)
            val date = view.findViewById<TextView>(R.id.datePicker_cng)
            val detail = view.findViewById<TextView>(R.id.detail_cng)

            employee.text = user.employee
            carName.text = user.carName
            stateNumber.text = user.stateNumber
            sum.text = user.sum
            phoneNumberClient.text = user.phoneNumberClient
            clientName.text = user.clientName
            date.text = user.date
            detail.text = user.workType

            builder.setView(view)
                .setPositiveButton(context.getString(R.string.delete)) { _, _ ->
                    db.collection("completedWork")
                        .whereEqualTo("employee", user.employee)
                        .whereEqualTo("carName", user.carName)
                        .whereEqualTo("clientName", user.clientName)
                        .whereEqualTo("phoneNumberClient", user.phoneNumberClient)
                        .whereEqualTo("stateNumber",user.stateNumber)
                        .whereEqualTo("sum", user.sum)
                        .get().addOnCompleteListener { p0 ->
                            if (p0.isSuccessful && !p0.result!!.isEmpty){

                                val documentSnapshot = p0.result!!.documents[0]

                                val documentId = documentSnapshot.id

                                db.collection("completedWork")
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, context.getString(R.string.successDelete), Toast.LENGTH_SHORT).show()
                                        notifyDataSetChanged()
                                    }.addOnFailureListener { e ->
                                        Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show()
                                        Log.d(ffe, e.toString())
                                    }
                            }
                        }
                }
            builder.setNeutralButton(context.getString(R.string.close)) { _, _ ->
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
            }
            val alert = builder.create()
            alert.show()

        }
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carName: TextView = itemView.findViewById(R.id.carName_ClientList)
        val stateNumber: TextView = itemView.findViewById(R.id.stateNumber_ClientList)
        val sum: TextView = itemView.findViewById(R.id.sum_ClientList)
        val btn: Button = itemView.findViewById(R.id.detail_ClientList)
    }
}