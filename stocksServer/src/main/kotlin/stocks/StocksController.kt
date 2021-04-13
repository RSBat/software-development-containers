package stocks

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StocksController {
    private val stockPrices: Map<Long, Long> = mutableMapOf()

    @GetMapping("/info/stocks")
    fun getStocksInfo(): String {
        return ""
    }

    @GetMapping("/buy")
    fun buyStocks(): String {
        return ""
    }

    @GetMapping("/sell")
    fun sellStock(): String {
        return ""
    }

    @GetMapping("/set")
    fun setPrice(): String {
        return ""
    }
}
