package api

import api.dto.ClassesRawDTO
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.internal.closeQuietly

val format = Json { prettyPrint = true }

class HttpApi {
    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(UserAgent) {
            agent = "Mozilla/5.0 (X11; Linux x86_64; rv:84.0) Gecko/20100101 Firefox/84.0"
        }
    }

    suspend fun search(filter: SearchFilter): List<Course> {
        println(format.encodeToString(filter.toDTO()))
        val dto = client.post<ClassesRawDTO> {
            url("https://coursesearch01.fcu.edu.tw/Service/Search.asmx/GetType2Result")
            contentType(ContentType.parse("application/json"))
            body = filter.toDTO()
        }
        println(format.encodeToString(dto))
        return fromClassesRawDTO(dto)
    }

    fun close() {
        client.closeQuietly()
    }
}