package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.PerchaseService
import deepdive.jpa.waiting_kaka.app.outbound.PerchaseTokenStore
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/ticket")
@CrossOrigin(origins = ["*"])
class WaitingController(
    private val sessionStorage: SessionStorage,
    private val waitingService: WaitingService,
    private val perchaseService: PerchaseService
) {
    data class EnterQueueRequest(
        val userId: String,
    )
    data class QueueResponse(
        val queueToken: String,
        val position: Int
    )

    @PostMapping("/enter")
    fun enterQueue(
        @RequestBody request: EnterQueueRequest
    ): QueueResponse {
        val info = waitingService.wait(request.userId)
        return QueueResponse(info.queueToken, info.queueSize)
    }

    @GetMapping("/queue-status/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribeWaitingStatus(@RequestParam queueToken: String): SseEmitter {
        val emitter = SseEmitter(60000)
        sessionStorage.addSession(queueToken, emitter)
        emitter.onCompletion { sessionStorage.removeSession(queueToken) }
        emitter.onTimeout { sessionStorage.removeSession(queueToken) }
        return emitter
    }

    data class PurchaseRequest(
        val userId: String,
        val purchaseToken: String
    )

    @PostMapping("/purchase")
    fun purchase(@RequestBody request: PurchaseRequest): String {
        Thread.sleep(5000)
        val userId = request.userId
        val purchaseToken = request.purchaseToken
        val result = perchaseService.purchase(userId, purchaseToken)
        return result
    }
}
