package nzhusupali.project.al_burak.fragments.completed.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import nzhusupali.project.al_burak.EditCompleteItem
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import java.util.*
import kotlin.collections.ArrayList

open class ClientCompleteAdapter(private var userList: ArrayList<ClientParamComplete>) :
    RecyclerView.Adapter<ClientCompleteAdapter.MyViewHolder>(), Filterable {
    private lateinit var database: FirebaseFirestore

    private var dbName = "completedWork"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_client_list_complete,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        database = FirebaseFirestore.getInstance()
        val ffs = "Firestore successfully completed"
        val ffe = "Firestore error complete"

        val user: ClientParamComplete = userList[position]
        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum
        holder.date.text = user.date

        val checkAuth = Firebase.auth.currentUser
//        if (checkAuth != null) holder.sum.visibility = View.VISIBLE
//        else holder.sum.visibility = View.GONE

        holder.btn.setOnClickListener { v ->
            val context = v.rootView.context
            val db = FirebaseFirestore.getInstance()

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTheme)
            val sheetView = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_client_list_completed, null)

            val employee = sheetView.findViewById<TextView>(R.id.ET_employee_alert_item_completed)
            val carName = sheetView.findViewById<TextView>(R.id.ET_carName_alert_item_completed)
            val stateNumber = sheetView.findViewById<TextView>(R.id.ET_stateNumber_alert_item_completed)
            val sum = sheetView.findViewById<TextView>(R.id.ET_sum_alert_item_completed)
            val phoneNumberClient = sheetView.findViewById<TextView>(R.id.ET_number_alert_item_completed)
            val clientName = sheetView.findViewById<TextView>(R.id.ET_clientName_alert_item_completed)
            val date = sheetView.findViewById<TextView>(R.id.ET_date_alert_item_completed)
            val workType = sheetView.findViewById<TextView>(R.id.ET_workType_alert_item_completed)

            val close = sheetView.findViewById<Button>(R.id.btn_close_alert_item_completed)
            val edit = sheetView.findViewById<Button>(R.id.btn_edit_alert_item_completed)
            val end = sheetView.findViewById<Button>(R.id.btn_end_alert_item_completed)
            val delete = sheetView.findViewById<Button>(R.id.btn_delete_alert_item_completed)

            employee.text = user.employee
            carName.text = user.carName
            stateNumber.text = user.stateNumber
            sum.text = user.sum
            phoneNumberClient.text = user.phoneNumberClient
            clientName.text = user.clientName
            date.text = user.date
            workType.text = user.workType

            // If you are admin, u can see this
            if(checkAuth != null) {
                sum.visibility = View.VISIBLE
                edit.visibility = View.VISIBLE
                end.visibility = View.INVISIBLE
                delete.visibility = View.VISIBLE
            } else {
                // If you log out, u can;t see this
                sum.visibility = View.GONE
                edit.visibility = View.INVISIBLE
                end.visibility = View.INVISIBLE
                delete.visibility = View.INVISIBLE
            }

            phoneNumberClient.setOnClickListener {
                val phoneNumber = "tel: " + user.phoneNumberClient
                val startIntent = Intent(Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber)))
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(startIntent)
            }
            close.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            edit.setOnClickListener {
                val startIntent = Intent(context, EditCompleteItem::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startIntent.putExtra("employee_edit_completed", user.employee)
                startIntent.putExtra("carName_edit_completed", user.carName)
                startIntent.putExtra("stateNumber_edit_completed", user.stateNumber)
                startIntent.putExtra("sum_edit_completed", user.sum)
                startIntent.putExtra("phoneNumber_edit_completed", user.phoneNumberClient)
                startIntent.putExtra("clientName_edit_completed", user.clientName)
                startIntent.putExtra("date_edit_completed", user.date)
                startIntent.putExtra("workType_edit_completed", user.workType)

                context.startActivity(startIntent)

            }
            delete.setOnClickListener {
                db.collection(dbName)
                    .whereEqualTo("carName", user.carName)
                    .whereEqualTo("clientName", user.clientName)
                    .whereEqualTo("phoneNumberClient", user.phoneNumberClient)
                    .whereEqualTo("stateNumber", user.stateNumber)
                    .whereEqualTo("date", user.date)
                    .whereEqualTo("sum", user.sum)
                    .get().addOnCompleteListener { p0 ->
                        if(p0.isSuccessful && !p0.result!!.isEmpty) {

                            val documentSnapshot = p0.result!!.documents[0]

                            val documentId = documentSnapshot.id

                            db.collection(dbName)
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, context.getString(R.string.successDelete), Toast.LENGTH_SHORT).show()
                                    Log.d(ffs, "$documentId, successfully deleted")
                                    val newActivity = Intent(context, MainActivity::class.java)
                                    newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(newActivity)
                                    notifyDataSetChanged()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(context,context.getString(R.string.error_server), Toast.LENGTH_SHORT).show()
                                    Log.d(ffe,e.toString())

                                }

                        }
                    }
            }

            bottomSheetDialog.setContentView(sheetView)
            bottomSheetDialog.show()
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
        val date: TextView = itemView.findViewById(R.id.date_clientList)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val result = FilterResults()

                val filteredData: ArrayList<ClientParamComplete> = ArrayList()
                if (constraint.toString().isEmpty()) filteredData.addAll(userList)
                else {

                    for (obj in userList) {

                        if (obj.stateNumber.lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) {

                            filteredData.add(obj)

                        }

                    }

                    result.values = filteredData
                    result.count = filteredData.size
                }
                return result
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                try {
                    userList = results.values as ArrayList<ClientParamComplete>
                    notifyDataSetChanged()
                } catch (e: NullPointerException) {
                    userList.clear()
                    eventChangeListener()
                }
            }
        }
    }

    private fun eventChangeListener() {
        database.collection("completedWork")
            .addSnapshotListener { value, _ ->
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d("Firestore completedWork", dc.document.id)
                        userList.add(dc.document.toObject(ClientParamComplete::class.java))
                    }
                }
                notifyDataSetChanged()
            }
    }
}