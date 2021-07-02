package nzhusupali.project.al_burak

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import nzhusupali.project.al_burak.databinding.ActivityPreOrderBinding
import nzhusupali.project.al_burak.utils.ClientAdapter
import nzhusupali.project.al_burak.utils.ClientParam

class PreOrder : AppCompatActivity() {
    private lateinit var _binding: ActivityPreOrderBinding
    private lateinit var userArrayList : ArrayList<ClientParam>
    private lateinit var recyclerView : RecyclerView
    private lateinit var clientAdapter : ClientAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreOrderBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        recyclerView = _binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        clientAdapter = ClientAdapter(userArrayList)

        recyclerView.adapter = clientAdapter

        eventChangeListener()

    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Предзаказы")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error != null){
                        Toast.makeText(applicationContext, "not worked", Toast.LENGTH_SHORT).show()
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){

                            userArrayList.add(dc.document.toObject(ClientParam::class.java))
                        }
                    }
                    clientAdapter.notifyDataSetChanged()
                }

            })
    }
}