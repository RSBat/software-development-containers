package stocks

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import java.lang.IllegalArgumentException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

internal class ExchangeClientTest {
    @Container
    private var simpleWebServer = KContainer("sd-container-testing:1.0-SNAPSHOT")
                .withFixedExposedPort(8080, 8080)
                .withExposedPorts(8080)

    private var client = ExchangeClient(ExchangeConnection("http://localhost:8080")).apply {
        addUser()
    }

    @Test
    fun canBuyShares() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 500)
        client.buyShares(0, "ITMO", 1)
    }

    @Test
    fun cannotBuySharesIfNotEnoughMoney() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 5)
        assertThrows(IllegalArgumentException::class.java) {
            client.buyShares(0, "ITMO", 1)
        }
    }

    @Test
    fun cannotBuySharesIfNotEnoughAvailable() {
        setStock("ITMO", 100, 1)
        client.depositMoney(0, 5)
        assertThrows(IllegalArgumentException::class.java) {
            client.buyShares(0, "ITMO", 10)
        }
    }

    @Test
    fun canSellShares() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 500)
        client.buyShares(0, "ITMO", 5)
        client.sellShares(0, "ITMO", 2)
    }

    @Test
    fun cannotSellMoreSharesThanOwned() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 500)
        client.buyShares(0, "ITMO", 5)
        assertThrows(IllegalArgumentException::class.java) {
            client.sellShares(0, "ITMO", 10)
        }
    }

    @Test
    fun boughtSharesInfluenceTotalBalance() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 500)
        client.buyShares(0, "ITMO", 5)
        assertEquals(500, client.getUserTotalBalance(0))
    }

    @Test
    fun totalBalanceChangesIfSharePriceChanges() {
        setStock("ITMO", 100, 10)
        client.depositMoney(0, 500)
        client.buyShares(0, "ITMO", 5)
        setStock("ITMO", 200, 5)

        assertEquals(1000, client.getUserTotalBalance(0))
    }

    private fun setStock(name: String, price: Long, amount: Long) {
        val uri = URI.create("http://localhost:8080/set?name=$name&price=$price&amount=$amount")
        val request = HttpRequest.newBuilder(uri).GET().build()
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
    }

    class KContainer(dockerImageName: String): FixedHostPortGenericContainer<KContainer>(dockerImageName)
}