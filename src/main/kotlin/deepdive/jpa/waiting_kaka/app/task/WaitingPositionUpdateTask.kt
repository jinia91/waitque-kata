package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingLine
import jakarta.annotation.PostConstruct
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class WaitingPositionUpdateTask(
    private val sessionStorage: SessionStorage,
    private val waitingLine: WaitingLine,
) {
    @Scheduled(fixedRate = 1000)
    fun start() {
        broadcastCurrentPosition()
    }

    private fun broadcastCurrentPosition() {
        waitingLine.queue.forEachIndexed { index, userId ->
            sessionStorage.getSession(userId)?.send(
                SseEmitter.event()
                    .name("position")
                    .data(mapOf("position" to index + 1))
            )
        }
    }
}