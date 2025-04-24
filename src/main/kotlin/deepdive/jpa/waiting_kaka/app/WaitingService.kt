package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.WaitingQueue
import org.springframework.stereotype.Service

data class WaitingQueueInfo(
    val waitingToken: String,
    val waitingSequenceNumber: Int
) {
    companion object {
        fun from(queueToken: String, queueSize: Int): WaitingQueueInfo {
            return WaitingQueueInfo(queueToken, queueSize)
        }
    }
}

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
