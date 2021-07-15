package nzhusupali.project.al_burak.fragments.preOrder.adapters

import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings.Global.getString
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

class ClientPreOrderAdapter(private val userList: ArrayList<ClientParamPreOrder>) :
    RecyclerView.Adapter<ClientPreOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_list_pre_order, parent, false
        )
        itemView.setOnClickListener(View.OnClickListener { v ->
        })
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: ClientParamPreOrder = userList[position]
        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum

        holder.detail.setOnClickListener { v ->
            val context = v.rootView.context
            val db = FirebaseFirestore.getInstance()
            val builder = AlertDialog.Builder(context)

            val view =
                LayoutInflater.from(context)
                    .inflate(R.layout.detail_client_list_pre_order, null)

            val carName = view.findViewById<TextView>(R.id.carName_cng2)
            val stateNumber = view.findViewById<TextView>(R.id.stateNumber_cng2)
            val sum = view.findViewById<TextView>(R.id.sum_cng2)
            val phoneNumberClient = view.findViewById<TextView>(R.id.phoneNumber_cng2)
            val clientName = view.findViewById<TextView>(R.id.clientName_cng2)
            val detail = view.findViewById<TextView>(R.id.detail_cng2)

            carName.text = user.carName
            stateNumber.text = user.stateNumber
            sum.text = user.sum
            phoneNumberClient.text = user.phoneNumberClient
            clientName.text = user.clientName
            detail.text = user.workType

            builder.setTitle(context.getString(R.string.informationPreOrder))
            builder.setView(view)
            builder.setPositiveButton(
                context.getString(R.string.deletePreOrder)
            ) { _, _ ->
                db.collection("preOrder")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d("Firebase Firestore", "${document.id} => ${document.data}")
                            db.collection("preOrder").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, context.getString(R.string.successDelete), Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show()
                                }
                            notifyDataSetChanged()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show()
                        Log.d("Firebase Firestore", "Error getting documents: ", exception)
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
}