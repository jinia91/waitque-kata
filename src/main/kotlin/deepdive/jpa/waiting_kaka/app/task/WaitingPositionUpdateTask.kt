package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingQueue
import jakarta.annotation.PostConstruct
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class WaitingPositionUpdateTask(
    private val sessionStorage: SessionStorage,
    private val waitingQueue: WaitingQueue,
) {
    @PostConstruct
    fun start() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate(
            {
                waitingQueue.queue.forEachIndexed { index, token ->
                    sessionStorage.getSession(token)?.send(
                        SseEmitter.event()
                        .name("position")
                        .data(mapOf("position" to index + 1)))
                }
            },
            0,
            1,
            TimeUnit.SECONDS
        )
    }
}