package nzhusupali.project.al_burak.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import nzhusupali.project.al_burak.MainActivity
import nzhusupali.project.al_burak.R
import nzhusupali.project.al_burak.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val etLogin = binding.ETLogin
        val etPassword = binding.ETPassword
        val enterLogin = getString(R.string.enterLogin)
        val enterPassword = getString(R.string.enterPassword)


        binding.loginBtn.setOnClickListener {

            when {
                TextUtils.isEmpty(etLogin.text.toString().trim{it <= ' '}) ->{
                    etLogin.error = enterLogin
                    etLogin.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(etPassword.text.toString().trim()) -> {
                    etPassword.error = enterPassword
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                else -> {
                    val login: String = etLogin.text.toString().trim()
                    val password : String = etPassword.text.toString().trim()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(login, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(applicationContext, "Куда лезешь, не твой уровень дорогой", Toast.LENGTH_SHORT).show()
                            }
                        }

                }

            }

        }

    }
}