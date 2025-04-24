package deepdive.jpa.waiting_kaka.app.outbound

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component

@Component
class Entry(
    private val map: MutableMap<String, Long> = ConcurrentHashMap(),
    private val maxEntrySize: Int = 1,
) {
    fun enter(token: String, expiredTimeInSecond: Int) {
        if (isFull()) {
            throw IllegalStateException("엔트리 최대 수용 인원 초과")
        }
        map[token] = System.currentTimeMillis() + expiredTimeInSecond * 1000L

    }

    fun isFull(): Boolean {
        return map.size >= maxEntrySize
    }

    fun out(token: String) {
        map.remove(token)
    }

    fun isIn(token: String): Boolean {
        return map.contains(token)
    }

    fun clearExpiredEntries() {
        val currentTime = System.currentTimeMillis()
        map.entries.removeIf { entry ->
            entry.value < currentTime
        }
    }
}