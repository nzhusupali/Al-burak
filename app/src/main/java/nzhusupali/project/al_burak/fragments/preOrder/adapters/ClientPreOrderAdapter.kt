package nzhusupali.project.al_burak.fragments.preOrder.adapters

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import nzhusupali.project.al_burak.R
import java.util.*
import kotlin.collections.ArrayList

class ClientPreOrderAdapter(private var userList: ArrayList<ClientParamPreOrder>) :
    RecyclerView.Adapter<ClientPreOrderAdapter.MyViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_client_list_pre_order, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: ClientParamPreOrder = userList[position]
        val ffe = "Firestore error"

        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum
        holder.detail.setOnClickListener { v ->
            val context = v.rootView.context
            val db = FirebaseFirestore.getInstance()
            val builder = AlertDialog.Builder(context)

            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.alert_detail_client_list_pre_order, null)

            val carName = view.findViewById<TextView>(R.id.carName_cng2)
            val stateNumber = view.findViewById<TextView>(R.id.stateNumber_cng2)
            val sum = view.findViewById<TextView>(R.id.sum_cng2)
            val phoneNumberClient = view.findViewById<TextView>(R.id.phoneNumber_cng2)
            val clientName = view.findViewById<TextView>(R.id.clientName_cng2)
            val date = view.findViewById<TextView>(R.id.datePicker_cng2)
            val detail = view.findViewById<TextView>(R.id.detail_cng2)

            carName.text = user.carName
            stateNumber.text = user.stateNumber
            sum.text = user.sum
            phoneNumberClient.text = user.phoneNumberClient
            clientName.text = user.clientName
            date.text = user.date
            detail.text = user.workType

            phoneNumberClient.setOnClickListener {
                val phoneNumber = "tel: " + user.phoneNumberClient
                val startIntent = Intent(Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber)))
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(startIntent)
            }
            builder.setTitle(context.getString(R.string.informationPreOrder))
            builder.setView(view)
            builder.setPositiveButton(
                context.getString(R.string.deletePreOrder)
            ) { _, _ ->
                db.collection("preOrder")
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

                            db.collection("preOrder")
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.successDelete),
                                        Toast.LENGTH_SHORT
                                    ).show()
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
        val carName: TextView = itemView.findViewById(R.id.carName_ClientListP)
        val stateNumber: TextView = itemView.findViewById(R.id.stateNumber_ClientListP)
        val sum: TextView = itemView.findViewById(R.id.sum_ClientListP)
        val detail: Button = itemView.findViewById(R.id.detail_ClientListP)
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
        db.collection("preOrder")
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