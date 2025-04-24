package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingLine
import jakarta.annotation.PostConstruct
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class EntryPermissionTask(
    private val waitingLine: WaitingLine,
    private val sessionStorage: SessionStorage,
    private val entry: Entry
) {
    @PostConstruct
    fun startEntryPermissionTask() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate({
            if (canEnterEntry()) {
                val firstWaitingUser = waitingLine.queue.poll()
                entry.enter(firstWaitingUser)
                sessionStorage.getSession(firstWaitingUser)?.send(
                    SseEmitter.event()
                    .name("ready"))
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun canEnterEntry(): Boolean = waitingLine.queue.isNotEmpty() && entry.isFull().not()
}