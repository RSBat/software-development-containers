package stocks

import kotlinx.serialization.Serializable

@Serializable
data class StocksItem(val name: String, val price: Long, val amountAvailable: Long)
