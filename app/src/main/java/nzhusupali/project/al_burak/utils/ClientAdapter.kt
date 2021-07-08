package nzhusupali.project.al_burak.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ClientListBinding
import nzhusupali.project.al_burak.databinding.DetailClientListBinding

class ClientAdapter(private val userList: ArrayList<ClientParam>):
    RecyclerView.Adapter<ClientAdapter.MyViewHolder>() {


    private lateinit var clientList: ArrayList<ClientParam>
    private lateinit var mCtx: Context


    private var binding: ClientListBinding? = null
    private val _binding get() = binding!!
    private val bind: DetailClientListBinding? = null
    private val _bind get() = bind!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_list,
            parent, false
        )
        return MyViewHolder(itemView)


    }




    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: ClientParam = userList[position]
        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum.toString()

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carName: TextView = itemView.findViewById(R.id.carName_ClientList)
        val stateNumber: TextView = itemView.findViewById(R.id.stateNumber_ClientList)
        val sum: TextView = itemView.findViewById(R.id.sum_ClientList)
    }
}