package deepdive.jpa.waiting_kaka.app.outbound

import org.springframework.stereotype.Repository

@Repository
class PerchaseTokenStore(
    private val purchaseTokens: MutableMap<String, String> = mutableMapOf()
) {
    fun addToken(token: String, value: String) {
        purchaseTokens[token] = value
    }

    fun getToken(token: String): String? {
        return purchaseTokens[token]
    }

    fun removeToken(token: String) {
        purchaseTokens.remove(token)
    }
}