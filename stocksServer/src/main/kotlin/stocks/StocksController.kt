package stocks

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class StocksController {
    private val stockPrices = mutableMapOf<String, Long>()
    private val stockAmount = mutableMapOf<String, Long>()

    @GetMapping("/info/stocks")
    fun getStocksInfo(): List<StocksItem> {
        return stockPrices.map { StocksItem(it.key, it.value, stockAmount[it.key]!!) }
    }

    @GetMapping("/buy")
    fun buyShares(
        @RequestParam("name")
        name: String,
        @RequestParam("amount")
        amount: Long,
        @RequestParam("maxPrice")
        maxPrice: Long
    ): String {
        if (stockAmount.getOrDefault(name, 0) < amount) {
            return "Not enough shares available"
        }
        val totalPrice = stockPrices.getOrDefault(name, 0) * amount
        if (totalPrice > maxPrice) {
            return "Max price exceeded"
        }
        stockAmount.compute(name) { _, oldAmount -> oldAmount!! - amount }
        return totalPrice.toString()
    }

    @GetMapping("/sell")
    fun sellShares(
        @RequestParam("name")
        name: String,
        @RequestParam("amount")
        amount: Long
    ): String {
        val totalPrice = stockPrices.getOrDefault(name, 0) * amount
        stockAmount.compute(name) { _, oldAmount -> (oldAmount ?: 0) + amount }
        return totalPrice.toString()
    }

    @GetMapping("/set")
    fun setStock(
        @RequestParam("name")
        name: String,
        @RequestParam("price")
        price: Long,
        @RequestParam("amount")
        amount: Long
    ): String {
        stockPrices[name] = price
        stockAmount[name] = amount
        return "ok"
    }
}
