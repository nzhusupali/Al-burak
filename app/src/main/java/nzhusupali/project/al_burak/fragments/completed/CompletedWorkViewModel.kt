package nzhusupali.project.al_burak.fragments.completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompletedWorkViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "Fragment completed work workeeeed"
    }
    val text: LiveData<String> = _text
}