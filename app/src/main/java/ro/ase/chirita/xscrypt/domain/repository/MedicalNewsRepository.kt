package ro.ase.chirita.xscrypt.domain.repository

import ro.ase.chirita.xscrypt.domain.model.MedicalArticle
import ro.ase.chirita.xscrypt.domain.model.Response

typealias GetListOfArticles = Response<List<MedicalArticle>>

interface MedicalNewsRepository {
    suspend fun getListOfMedicalArticles(): GetListOfArticles
}