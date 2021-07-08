package nzhusupali.project.al_burak.fragments.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddCarViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is addCarViewModel Fragment"
    }
    val text: LiveData<String> = _text
}