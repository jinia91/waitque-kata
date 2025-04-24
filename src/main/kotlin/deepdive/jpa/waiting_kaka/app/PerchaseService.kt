package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import org.springframework.stereotype.Service

@Service
class PerchaseService(
    private val entry: Entry,
) {
    fun purchase(userId: String): String {
        try {
            check(entry.isIn(userId)) {
                "엔트리에 등록되지 않은 사용자입니다"
            }
            return "구매 성공"
        } catch (e: Exception) {
            return "구매 실패" + e.message
        } finally {
            entry.out(userId)
        }
    }
}