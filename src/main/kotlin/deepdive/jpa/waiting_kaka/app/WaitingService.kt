package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.WaitingQueue
import org.springframework.stereotype.Service

@Service
class WaitingService(
    private val waitingQueue: WaitingQueue
) {
    fun wait(userId: String): WaitingQueueInfo {
        val queueToken = userId + "-" + System.currentTimeMillis()
        waitingQueue.enterQueue(queueToken)
        return WaitingQueueInfo.from(
            queueToken = queueToken,
            queueSize = waitingQueue.queue.size
        )
    }
}
data class WaitingQueueInfo(
    val queueToken: String,
    val queueSize: Int
) {
    companion object {
        fun from(queueToken: String, queueSize: Int): WaitingQueueInfo {
            return WaitingQueueInfo(queueToken, queueSize)
        }
    }
}