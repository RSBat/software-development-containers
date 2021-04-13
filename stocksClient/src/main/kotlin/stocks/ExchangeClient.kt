package stocks

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class ExchangeClient(private val connection: ExchangeConnection) {
    private val users = mutableListOf<User>()

    fun addUser(): Int {
        val id = users.size
        users.add(User())
        return id
    }

    fun depositMoney(userId: Int, amount: Long) {
        getUser(userId).addMoney(amount)
    }

    fun buyShares(userId: Int, name: String, amount: Long) {
        val user = getUser(userId)

        val result = connection.buyShares(name, amount, user.getBalance())
        if (result.isSuccessful) {
            user.addMoney(-result.totalPrice)
            user.addShares(name, amount)
        } else {
            throw IllegalStateException(result.errorReason)
        }
    }

    fun sellShares(userId: Int, name: String, amount: Long) {
        val user = getUser(userId)
        if (amount > user.getPurchasedShares().getOrDefault(name, 0L)) {
            throw IllegalArgumentException("Cannot sell more shares than user owns")
        }

        val price = connection.sellShares(name, amount)
        user.addMoney(price)
        user.addShares(name, -amount)
    }

    fun getUserTotalBalance(userId: Int): Long {
        val userBalance = getUser(userId).getBalance()
        val userShares = getUserShares(userId)

        return userShares.sumOf { it.amount * it.pricePerShare } + userBalance
    }

    fun getUserShares(userId: Int): List<OwnedShareInfo> {
        val userShares = getUser(userId).getPurchasedShares()
        val sharePrices = connection.getInfo().associate { Pair(it.name, it.price) }
        return userShares.map { OwnedShareInfo(it.key, it.value, sharePrices.getOrDefault(it.key, 0L)) }
    }

    private fun getUser(userId: Int): User {
        return users.getOrNull(userId) ?: throw IllegalArgumentException("User does not exist")
    }

    data class OwnedShareInfo(val name: String, val amount: Long, val pricePerShare: Long)
}
