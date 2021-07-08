package nzhusupali.project.al_burak.fragments.completed

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
import nzhusupali.project.al_burak.databinding.FragmentCompletedWorkBinding
import nzhusupali.project.al_burak.utils.ClientAdapter
import nzhusupali.project.al_burak.utils.ClientParam

class CompletedWork : Fragment() {
    private lateinit var userArrayList: ArrayList<ClientParam>
    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var completedWorkViewModel: CompletedWorkViewModel

    private var _binding: FragmentCompletedWorkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        completedWorkViewModel =
            ViewModelProvider(this).get(CompletedWorkViewModel::class.java)

        _binding = FragmentCompletedWorkBinding.inflate(inflater, container, false)
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
        db.collection("Выполненные работы")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Toast.makeText(context, "not worked", Toast.LENGTH_SHORT).show()
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {

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