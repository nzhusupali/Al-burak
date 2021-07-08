package nzhusupali.project.al_burak.fragments.preOrder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import nzhusupali.project.al_burak.databinding.FragmentPreOrderBinding
import nzhusupali.project.al_burak.utils.ClientAdapter
import nzhusupali.project.al_burak.utils.ClientParam

class PreOrder : Fragment() {
    private lateinit var userArrayList: ArrayList<ClientParam>
    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var preOrderViewModel: PreOrderViewModel

    private var _binding: FragmentPreOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preOrderViewModel =
            ViewModelProvider(this).get(PreOrderViewModel::class.java)

        _binding = FragmentPreOrderBinding.inflate(inflater, container,false)
        val root: View = binding.root

        recyclerView = _binding!!.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        clientAdapter = ClientAdapter(userArrayList)

        recyclerView.adapter = clientAdapter

        eventChangeListener()

        return root
    }

    // Читаем данные с Firestore
    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Предзаказы")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Toast.makeText(context, "not worked", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}