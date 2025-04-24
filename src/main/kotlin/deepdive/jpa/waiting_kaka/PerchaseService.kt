package deepdive.jpa.waiting_kaka

import deepdive.jpa.waiting_kaka.app.outbound.EntryQueue
import deepdive.jpa.waiting_kaka.app.outbound.PerchaseTokenStore
import org.springframework.stereotype.Service

@Service
class PerchaseService(
    private val entryQueue: EntryQueue,
    private val perchaseTokenStore: PerchaseTokenStore,
) {
    fun purchase(userId: String, purchaseToken: String): String {
        try {
            check(perchaseTokenStore.getToken(userId) != null) {
                "토큰이 이미 사용되었거나 존재하지 않습니다."
            }
            check(perchaseTokenStore.getToken(userId) == purchaseToken) {
                "토큰이 일치하지 않습니다."
            }
            return "구매 성공"
        } catch (e: Exception) {
            return e.message ?: "구매 실패"
        } finally {
            perchaseTokenStore.removeToken(userId)
            entryQueue.queue.remove(purchaseToken)
        }
    }
}