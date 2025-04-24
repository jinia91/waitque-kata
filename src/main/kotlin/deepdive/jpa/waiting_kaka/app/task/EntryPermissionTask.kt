package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingLine
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class EntryPermissionTask(
    private val waitingLine: WaitingLine,
    private val sessionStorage: SessionStorage,
    private val entry: Entry,
) {
    @Scheduled(fixedRate = 1000)
    fun start() {
        if (canEnterEntry()) {
            val firstWaitingUser = waitingLine.queue.poll()
            entry.enter(firstWaitingUser, 30)
            sessionStorage.getSession(firstWaitingUser)?.send(
                SseEmitter.event()
                    .name("ready").data("")
            )
        }
    }

    private fun canEnterEntry(): Boolean = waitingLine.queue.isNotEmpty() && entry.isFull().not()
}