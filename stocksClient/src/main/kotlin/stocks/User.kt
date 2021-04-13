package stocks

import java.lang.IllegalStateException

class User {
    private val purchasedShares = mutableMapOf<String, Long>()
    private var balance = 0L

    fun getPurchasedShares(): Map<String, Long> = purchasedShares

    fun getBalance() = balance

    fun addMoney(amount: Long) {
        if (balance + amount < 0) {
            throw IllegalStateException("Resulting balance cannot be negative")
        }
        balance += amount
    }

    fun addShares(name: String, amount: Long) {
        if (purchasedShares.getOrDefault(name, 0L) + amount < 0) {
            throw IllegalStateException("Cannot have negative shares")
        }
        purchasedShares.merge(name, amount, Long::plus)
    }
}
