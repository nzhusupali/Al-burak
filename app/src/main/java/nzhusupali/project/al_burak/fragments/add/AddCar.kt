package nzhusupali.project.al_burak.fragments.add

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import nzhusupali.project.al_burak.activity.PreOrderAddCar
import nzhusupali.project.al_burak.activity.CompleteAddCar
import nzhusupali.project.al_burak.databinding.FragmentAddCarBinding
import java.util.*

class AddCar : Fragment() {
    private var _binding: FragmentAddCarBinding? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater,container,false)
        val root: View = binding.root

        binding.carAdd.setOnClickListener {
            startActivity(Intent(context, PreOrderAddCar::class.java))
        }
        binding.clientAdd.setOnClickListener {
            startActivity(Intent(context, CompleteAddCar::class.java))
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}