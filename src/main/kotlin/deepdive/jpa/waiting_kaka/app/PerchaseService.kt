package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import deepdive.jpa.waiting_kaka.app.outbound.PerchaseTokenStore
import org.springframework.stereotype.Service

@Service
class PerchaseService(
    private val entry: Entry,
    private val perchaseTokenStore: PerchaseTokenStore,
) {
    fun purchase(userId: String, purchaseToken: String): String {
        try {
            val foundToken = perchaseTokenStore.getToken(userId)
            check(foundToken != null) {
                "토큰이 이미 사용되었거나 존재하지 않습니다."
            }
            check(foundToken == purchaseToken) {
                "토큰이 일치하지 않습니다."
            }
            return "구매 성공"
        } catch (e: Exception) {
            return "구매 실패" + e.message
        } finally {
            perchaseTokenStore.removeToken(userId)
            entry.out(purchaseToken)
        }
    }
}