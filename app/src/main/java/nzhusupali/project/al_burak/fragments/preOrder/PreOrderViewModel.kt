package nzhusupali.project.al_burak.fragments.preOrder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreOrderViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is PreOrder Fragment"
    }
    val text: LiveData<String> = _text
}