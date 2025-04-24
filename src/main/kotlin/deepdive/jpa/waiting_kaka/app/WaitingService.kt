package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.WaitingLine
import org.springframework.stereotype.Service

data class WaitingQueueInfo(
    val waitingSequenceNumber: Int
) {
    companion object {
        fun from(position: Int): WaitingQueueInfo {
            return WaitingQueueInfo(position)
        }
    }
}

@Service
class WaitingService(
    private val waitLine: WaitingLine
) {
    fun wait(userId: String): WaitingQueueInfo {
        val position = waitLine.wait(userId)
        return WaitingQueueInfo.from(
            position = position
        )
    }
}
