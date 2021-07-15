package nzhusupali.project.al_burak.fragments.completed.adapters

import com.google.firebase.Timestamp

data class ClientParamComplete(
    val employee: String = " ",
    val carName: String = " ",
    val stateNumber: String = " ",
    val sum: String =  " ",
    val phoneNumberClient: String = " ",
    val clientName: String = " ",
    val workType: String = " ",
    val date: Timestamp? = null,
)
