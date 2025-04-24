package deepdive.jpa.waiting_kaka.app

import java.util.concurrent.ConcurrentHashMap
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
class SessionStorage(
    private val sseEmitters: MutableMap<String, SseEmitter> = ConcurrentHashMap()
) {
    fun addSession(sessionId: String, sseEmitter: SseEmitter) {
        sseEmitters[sessionId] = sseEmitter
    }

    fun getSession(sessionId: String): SseEmitter? {
        return sseEmitters[sessionId]
    }

    fun removeSession(sessionId: String) {
        sseEmitters.remove(sessionId)
    }
}