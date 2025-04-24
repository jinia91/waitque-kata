package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import deepdive.jpa.waiting_kaka.app.outbound.PerchaseTokenStore
import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
import deepdive.jpa.waiting_kaka.app.outbound.WaitingQueue
import jakarta.annotation.PostConstruct
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class EntryPermissionTask(
    private val waitingQueue: WaitingQueue,
    private val sessionStorage: SessionStorage,
    private val perchaseTokenStore: PerchaseTokenStore,
    private val entry: Entry
) {
    data class PurchaseToken(val token: String, val expiresIn: Long)

    @PostConstruct
    fun startEntryPermissionTask() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleAtFixedRate({
            if (canEnterEntry()) {
                val purchaseToken = UUID.randomUUID().toString()
                val waitingToken = waitingQueue.queue.poll()
                entry.enter(purchaseToken)
                val userId = waitingToken.split("-").first()
                perchaseTokenStore.addToken(userId, purchaseToken)
                sessionStorage.getSession(waitingToken)?.send(
                    SseEmitter.event()
                    .name("ready")
                    .data(PurchaseToken(purchaseToken, 30)))
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun canEnterEntry(): Boolean = waitingQueue.queue.isNotEmpty() && entry.isFull().not()
}