package nzhusupali.project.al_burak.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ClientListBinding

class ClientAdapter(private val userList: ArrayList<ClientParam>) :
    RecyclerView.Adapter<ClientAdapter.MyViewHolder>() {

    private var binding: ClientListBinding? = null
    private val _binding get() = binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.client_list,
            parent, false
        )
        return MyViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: ClientAdapter.MyViewHolder, position: Int) {
        val user: ClientParam = userList[position]
        holder.carName.text = user.carName
        holder.stateNumber.text = user.stateNumber
        holder.sum.text = user.sum.toString()
//        holder.detail.text = user.det
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