package ro.ase.chirita.xscrypt.domain.model

data class User(
    var name: String,
    var email: String,
    var wallet: String,
    var status: String,
    var photoUrl: String
)
