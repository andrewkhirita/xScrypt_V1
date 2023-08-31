package ro.ase.chirita.xscrypt.data.repository

import ro.ase.chirita.xscrypt.core.Constants.API_KEY
import ro.ase.chirita.xscrypt.core.Exceptions.FETCH_MEDICAL_ARTICLES_EXCEPTION
import ro.ase.chirita.xscrypt.data.source.remote.MedicalNewsApi
import ro.ase.chirita.xscrypt.domain.model.MedicalArticle
import ro.ase.chirita.xscrypt.domain.model.Response
import ro.ase.chirita.xscrypt.domain.repository.GetListOfArticles
import ro.ase.chirita.xscrypt.domain.repository.MedicalNewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalNewsRepositoryImpl @Inject constructor(
    private val medicalNewsApi: MedicalNewsApi
): MedicalNewsRepository {

    override suspend fun getListOfMedicalArticles(): GetListOfArticles {
        return try {
            val response = medicalNewsApi.getTopHeadlines(
                country = "us",
                category = "health",
                apiKey = API_KEY
            )
            if (response.status == "ok") {
                val medicalArticles = response.articles.map { articleResponse ->
                    if(articleResponse.urlToImage != null) {
                        MedicalArticle(
                            title = articleResponse.title,
                            urlToImage = articleResponse.urlToImage,
                            publishedAt = articleResponse.publishedAt,
                            url = articleResponse.url
                        )
                    } else {
                        MedicalArticle(
                            title = articleResponse.title,
                            urlToImage = "",
                            publishedAt = articleResponse.publishedAt,
                            url = articleResponse.url
                        )
                    }
                }
                Response.Success(medicalArticles)
            } else {
                throw Exception(FETCH_MEDICAL_ARTICLES_EXCEPTION)
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


}