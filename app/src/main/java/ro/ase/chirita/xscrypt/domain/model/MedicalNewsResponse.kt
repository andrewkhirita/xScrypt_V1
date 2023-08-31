package ro.ase.chirita.xscrypt.domain.model

data class MedicalNewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<MedicalArticle>
)
