package nzhusupali.project.al_burak.fragments.preOrder.adapters

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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import nzhusupali.project.al_burak.ActivityEndPreOrder
import nzhusupali.project.al_burak.EditPreOrderItem
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import java.util.*
import kotlin.collections.ArrayList


class ClientPreOrderAdapter(private var userList: ArrayList<ClientParamPreOrder>) :
    RecyclerView.Adapter<ClientPreOrderAdapter.MyViewHolder>(), Filterable {

    private var dbName = "preOrder"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_client_list_pre_order, parent, false
        )
        return MyViewHolder(itemView)
    }

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: ClientParamPreOrder = userList[position]
        val ffs = "Firestore successfully preOrder"
        val ffe = "Firestore error preOrder"

        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum
        holder.date.text = user.date

        val checkAuth = Firebase.auth.currentUser
//        if (checkAuth != null) holder.sum.visibility = View.VISIBLE
//        else holder.sum.visibility = View.GONE

        holder.detail.setOnClickListener { v ->
            val context = v.rootView.context
            val db = FirebaseFirestore.getInstance()
//            val activity = v!!.context as AppCompatActivity

            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTheme)
            val sheetView = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_client_list_pre_order, null)

            val carName = sheetView.findViewById<TextView>(R.id.ET_carName_alert_item_preOrder)
            val stateNumber = sheetView.findViewById<TextView>(R.id.ET_stateNumber_alert_item_preOrder)
            val sum = sheetView.findViewById<TextView>(R.id.ET_sum_alert_item_preOrder)
            val phoneNumberClient = sheetView.findViewById<TextView>(R.id.ET_number_alert_item_preOrder)
            val clientName = sheetView.findViewById<TextView>(R.id.ET_clientName_alert_item_preOrder)
            val date = sheetView.findViewById<TextView>(R.id.ET_date_alert_item_preOrder)
            val workType = sheetView.findViewById<TextView>(R.id.ET_workType_alert_item_preOrder)

            val close = sheetView.findViewById<Button>(R.id.btn_close_alert_item_preOrder)
            val edit = sheetView.findViewById<Button>(R.id.btn_change_alert_item_preOrder)
            val end = sheetView.findViewById<Button>(R.id.btn_end_alert_item_preOrder)
            val delete = sheetView.findViewById<Button>(R.id.btn_delete_alert_item_preOrder)

            carName.text = user.carName
            stateNumber.text = user.stateNumber
            sum.text = user.sum
            phoneNumberClient.text = user.phoneNumberClient
            clientName.text = user.clientName
            date.text = user.date
            workType.text = user.workType

            // if you are admin, u can see this
            if (checkAuth != null) {
                // If you are login, u can see
                sum.visibility = View.VISIBLE
                edit.visibility = View.VISIBLE
                end.visibility = View.VISIBLE
                delete.visibility = View.VISIBLE
            } else {
                // If you log out, u can't see
                sum.visibility = View.GONE
                edit.visibility = View.INVISIBLE
                end.visibility = View.INVISIBLE
                delete.visibility = View.INVISIBLE
            }

            phoneNumberClient.setOnClickListener {
                val phoneNumber = "tel: " + user.phoneNumberClient
                val intent = Intent(Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber) ) )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            close.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            edit.setOnClickListener {
                val startIntent = Intent(context, EditPreOrderItem::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                startIntent.putExtra("carName_edit_preOrder", user.carName)
                startIntent.putExtra("stateNumber_edit_preOrder", user.stateNumber)
                startIntent.putExtra("sum_edit_preOrder", user.sum)
                startIntent.putExtra("phoneNumberClient_edit_preOrder", user.phoneNumberClient)
                startIntent.putExtra("clientName_edit_preOrder", user.clientName)
                startIntent.putExtra("date_edit_preOrder", user.date)
                startIntent.putExtra("workType_edit_preOrder", user.workType)

                context.startActivity(startIntent)
            }
            end.setOnClickListener {

                val send = Intent(context, ActivityEndPreOrder::class.java)
                send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                send.putExtra("carName_end_preOrder", user.carName)
                send.putExtra("stateNumber_end_preOrder", user.stateNumber)
                send.putExtra("sum_end_preOrder", user.sum)
                send.putExtra("phoneNumberClient_end_preOrder", user.phoneNumberClient)
                send.putExtra("clientName_end_preOrder", user.clientName)
                send.putExtra("date_end_preOrder", user.date)
                send.putExtra("workType_end_preOrder", user.workType)


                context.startActivity(send)

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
                        if (p0.isSuccessful && !p0.result!!.isEmpty) {

                            val documentSnapshot = p0.result!!.documents[0]

                            val documentId = documentSnapshot.id

                            db.collection(dbName)
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.successDelete),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(ffs, "$documentId successfully deleted")
                                    val newActivity = Intent(context, MainActivity::class.java)
                                    newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(newActivity)
                                    notifyDataSetChanged()
                                }.addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_server),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(ffe, e.toString())
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
        val carName: TextView = itemView.findViewById(R.id.carName_ClientListP)
        val stateNumber: TextView = itemView.findViewById(R.id.stateNumber_ClientListP)
        val sum: TextView = itemView.findViewById(R.id.sum_ClientListP)
        val detail: Button = itemView.findViewById(R.id.detail_ClientListP)
        val date: TextView = itemView.findViewById(R.id.date_clientListP)
    }

    // Search view
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()

                val filteredData: ArrayList<ClientParamPreOrder> = ArrayList()
                if (constraint.toString().isEmpty()) filteredData.addAll(userList)
                else {

                    for (obj in userList) {

                        if (obj.stateNumber.lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) {

                            filteredData.add(obj)

                        }

                    }
                    results.values = filteredData
                    results.count = filteredData.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                try {
                    userList = results.values as ArrayList<ClientParamPreOrder>
                    notifyDataSetChanged()
                } catch (e: NullPointerException) {
                    userList.clear()
                    eventChangeListener()
                }

            }
        }
    }
    private fun eventChangeListener() {
        val db = FirebaseFirestore.getInstance()
        db.collection(dbName)
            .addSnapshotListener { value, _ ->
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d("Firestore preOrder", dc.document.id)
                        userList.add(dc.document.toObject(ClientParamPreOrder::class.java))
                    }
                }
                notifyDataSetChanged()
            }
    }
}