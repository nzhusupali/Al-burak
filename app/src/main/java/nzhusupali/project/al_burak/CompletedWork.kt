package nzhusupali.project.al_burak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nzhusupali.project.al_burak.databinding.ActivityCompletedWorkBinding

class CompletedWork : AppCompatActivity() {
    private lateinit var _binding : ActivityCompletedWorkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompletedWorkBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.back.setOnClickListener { Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP }

    }
}