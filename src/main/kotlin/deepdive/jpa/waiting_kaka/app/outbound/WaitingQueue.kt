package deepdive.jpa.waiting_kaka.app.outbound

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.stereotype.Component

@Component
class WaitingQueue(
    val queue: Queue<String> = ConcurrentLinkedQueue(),
) {

    fun enterQueue(token: String) {
        queue.add(token)
    }
}