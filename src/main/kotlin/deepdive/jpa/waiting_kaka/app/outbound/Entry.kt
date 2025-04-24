package deepdive.jpa.waiting_kaka.app.outbound

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.stereotype.Component

@Component
class Entry(
    private val set: MutableSet<String> = mutableSetOf(),
    private val maxEntrySize: Int = 1,
) {
    fun enter(token: String) {
        set.add(token)
    }

    fun isFull(): Boolean {
        return set.size >= maxEntrySize
    }

    fun out(token: String) {
        set.remove(token)
    }
}