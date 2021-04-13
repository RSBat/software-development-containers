# StocksServer
* `/info` - возвращает json с информацией о всех доступных акциях.
* `/buy?name=name&amount=amount&maxPrice=maxPrice` - пытается купить `amount` акций `name`. Возвращает суммарную стоимость при успехе. Если доступных акций недостаточно, или их суммарная стоимость больше `maxPrice`, возвращает причину ошибки.
* `/sell?name=name&amount=amount` - продает `amount` акций `name`. Возвращает суммарную стоимость.
* `/set?name=name&amount=amount&price=price` - устанавливает для акций `name` цену `price` и количество `amount`.

# StocksClient
* `addUser(): Int` - добавляет нового пользователя. Возвращает его `id`
* `depositMoney(userId: Int, amount: Long)` - добавляет на счет пользователя `id` `amount` денег. `amount` может быть отрицательным. Кидает `IllegalArgumentException` если итоговый баланс будет отрицательным.
* `buyShares(userId: Int, name: String, amount: Long)` - пытается купить `amount` акций `name` пользователем `id`. Кидает `IllegalArgumentException` если пытаемся купить слишком много акций.
* `sellShares(userId: Int, name: String, amount: Long)` - продает `amount` акций `name` пользователем `id`. Кидает `IllegalArgumentException` если пытаемся продать слишком много акций.
* `getUserTotalBalance(userId: Int): Long` - возвращает баланс пользователя `id` с учетом купленных акций.
* `getUserShares(userId: Int): List<OwnedShareInfo>` - возвращает информацию об акциях владеемых пользователем `id`.

# Запуск
`./gradlew :stocksServer:docker`
`./gradlew :stocksClient:test`