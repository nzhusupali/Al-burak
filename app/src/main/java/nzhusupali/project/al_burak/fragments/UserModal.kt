package nzhusupali.project.al_burak.fragments

class UserModal {
    var firstName: String? = null
    var lastName: String? = null
    var dob: String? = null
    var email: String? = null
    var address: String? = null
    var number: String? = null
    var profileUrl: String? = null

    constructor() {}
    constructor(
        firstName: String?,
        lastName: String?,
        dob: String?,
        email: String?,
        address: String?,
        number: String?,
        profileUrl: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.dob = dob
        this.email = email
        this.address = address
        this.number = number
        this.profileUrl = profileUrl
    }
}