package nzhusupali.project.al_burak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nzhusupali.project.al_burak.databinding.ActivityMainBinding
import nzhusupali.project.al_burak.databinding.LayoutAddClientBinding


class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var bind: LayoutAddClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = LayoutAddClientBinding.inflate(layoutInflater)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.addCar.setOnClickListener { startActivity(Intent(this, PreOrderAddCar::class.java)) }
        _binding.competedWork.setOnClickListener { startActivity(Intent(this,CompletedWork::class.java)) }
        _binding.preOrder.setOnClickListener { startActivity(Intent(this, PreOrder::class.java)) }

    }
}

