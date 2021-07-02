package nzhusupali.project.al_burak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nzhusupali.project.al_burak.databinding.ActivityPreOrderBinding

class PreOrder : AppCompatActivity() {
    private lateinit var _binding: ActivityPreOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPreOrderBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.back.setOnClickListener { Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP }

    }
}