package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingLine
import jakarta.annotation.PostConstruct
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class WaitingPositionUpdateTask(
    private val sessionStorage: SessionStorage,
    private val waitingLine: WaitingLine,
) {
    @PostConstruct
    fun start() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate(
            { broadcastCurrentPosition() },
            0,
            1,
            TimeUnit.SECONDS
        )
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