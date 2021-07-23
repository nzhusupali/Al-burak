package nzhusupali.project.al_burak.fragments.preOrder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.FragmentPreOrderBinding
import nzhusupali.project.al_burak.fragments.preOrder.adapters.ClientParamPreOrder
import nzhusupali.project.al_burak.fragments.preOrder.adapters.ClientPreOrderAdapter

class PreOrder : Fragment() {
    private lateinit var userArrayList: ArrayList<ClientParamPreOrder>
    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientPreOrderAdapter
    private lateinit var db: FirebaseFirestore

    private var _binding: FragmentPreOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPreOrderBinding.inflate(inflater, container,false)
        val root: View = binding.root

        recyclerView = _binding!!.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()

        clientAdapter = ClientPreOrderAdapter(userArrayList)

        recyclerView.adapter = clientAdapter

        eventChangeListener()

        return root
    }

    // Читаем данные с Firestore
    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("preOrder")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Toast.makeText(context, getString(R.string.error_server), Toast.LENGTH_SHORT).show()
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){

                        if (dc.type == DocumentChange.Type.ADDED) {
                            Log.d("Firebase Firestore", dc.document.id)
                            userArrayList.add(dc.document.toObject(ClientParamPreOrder::class.java))
                        }
                    }
                    clientAdapter.notifyDataSetChanged()
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}