package com.shiftkey.codingchallenge.livedata

/**
 * A event that can be handled only once.
 * As [LiveData] objects hold the last emitted items and redispatch them on subscription
 * they can be directly used as one-time events dispatchers. This class mitigates this issue
 * by providing an event that can redispatched many times but can be handled only once.
 */
class HandleableEvent<T> (private val payload: T) {
    /**
     * States whether the event has been handled already.
     */
    var isHandled = false
        private set

    /**
     * Handles the event if not handled yet.
     * @param handler   Event handler that will be invoked if the event has not been handled yet.
     *                  The provided handler must return [true] if it handled the event or [false] otherwise.
     */
    @Synchronized
    fun handle(handler: (T) -> Boolean): Boolean {
        if (isHandled) return false
        isHandled = handler(payload)
        return isHandled
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HandleableEvent<*>

        if (payload != other.payload) return false
        if (isHandled != other.isHandled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = payload?.hashCode() ?: 0
        result = 31 * result + isHandled.hashCode()
        return result
    }


}