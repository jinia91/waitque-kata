package deepdive.jpa.waiting_kaka.app.outbound

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.stereotype.Component

@Component
class EntryQueue(
    val queue: Queue<String> = ConcurrentLinkedQueue(),
    val maxEntrySize: Int = 1,
) {
    fun enterQueue(token: String) {
        queue.add(token)
    }
}