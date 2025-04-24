package deepdive.jpa.waiting_kaka.app

import deepdive.jpa.waiting_kaka.app.outbound.SessionStorage
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

    data class WaitingRequest(
        val userId: String,
    )
    data class WaitingResponse(
        val userId: String,
        val position: Int
    )

    @PostMapping("/enter")
    fun wait(
        @RequestBody request: WaitingRequest
    ): WaitingResponse {
        val info = waitingService.wait(request.userId)
        return WaitingResponse(request.userId, info.waitingSequenceNumber)
    }

    @GetMapping("/queue-status/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribeWaitingStatus(@RequestParam userId: String): SseEmitter {
        val emitter = SseEmitter(60000)
        sessionStorage.addSession(userId, emitter)
        emitter.onCompletion { sessionStorage.removeSession(userId) }
        emitter.onTimeout { sessionStorage.removeSession(userId) }
        return emitter
    }

    data class PurchaseRequest(
        val userId: String
    )

    @PostMapping("/purchase")
    fun purchase(@RequestBody request: PurchaseRequest): String {
        Thread.sleep(5000)
        val userId = request.userId
        val result = perchaseService.purchase(userId)
        return result
    }
}
