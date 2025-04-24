package deepdive.jpa.waiting_kaka.app.outbound

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import org.springframework.stereotype.Component

@Component
class WaitingLine(
    val queue: Queue<String> = ConcurrentLinkedQueue(),
) {

    fun wait(token: String) : Int {
        queue.add(token)
        return queue.size
    }
}