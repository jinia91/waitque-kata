package deepdive.jpa.waiting_kaka.app.task

import deepdive.jpa.waiting_kaka.app.outbound.Entry
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ExpireTask(
    private val entry : Entry,
) {

    @Scheduled(fixedDelay = 1000)
    fun start() {
        entry.clearExpiredEntries()
    }
}