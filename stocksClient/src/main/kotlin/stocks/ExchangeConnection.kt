package stocks

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ExchangeConnection(exchangeUrl: String) {
    private val client = HttpClient.newHttpClient()
    private val uriBuilderFactory = DefaultUriBuilderFactory(exchangeUrl)

    fun buyShares(stockName: String, amount: Long, maxPrice: Long): Long {
        val uri = uriBuilderFactory.builder()
            .path("buy")
            .queryParam("name", stockName)
            .queryParam("amount", amount)
            .queryParam("maxPrice", maxPrice)
            .build()
        val response = sendRequest(uri)
        return response.toLong()
    }

    fun sellShares(stockName: String, amount: Long): Long {
        val uri = uriBuilderFactory.builder()
            .path("sell")
            .queryParam("name", stockName)
            .queryParam("amount", amount)
            .build()
        val response = sendRequest(uri)
        return response.toLong()
    }

    fun getInfo(): List<StocksItem> {
        val uri = uriBuilderFactory.builder()
            .path("info")
            .build()
        val response = sendRequest(uri)
        return Json.decodeFromString(response)
    }

    private fun sendRequest(uri: URI): String {
        val request = HttpRequest.newBuilder(uri).GET().build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}
