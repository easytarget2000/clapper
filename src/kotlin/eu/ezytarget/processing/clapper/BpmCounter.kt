package eu.ezytarget.clapper

class BpmCounter {

    var numberOfTapsForResult = 5
        set(value) {
            field = value
            clearLastTapMillis()
        }

    var MAX_TAP_INTERVAL_MILLIS = 1_000L

    private val lastTapMillis = mutableListOf<Long>()

    fun tap(tapMillis: Long = System.currentTimeMillis()): Float? {
        clearLastTapMillisIfExpired(tapMillis)
        lastTapMillis.add(tapMillis)
        val bpmFromTaps = bpmFromLastTapMillis() ?: return null

        return bpmFromTaps
    }

    private fun clearLastTapMillis() = lastTapMillis.clear()

    private fun clearLastTapMillisIfExpired(tapMillis: Long) {
        val veryLastTapMillis = lastTapMillis.max() ?: return
        if ((tapMillis - veryLastTapMillis) > MAX_TAP_INTERVAL_MILLIS) {
            clearLastTapMillis()
        }
    }

    // TODO: Unit test bpmFromTaps()!
    private fun bpmFromLastTapMillis(): Float? {
        if (lastTapMillis.size < numberOfTapsForResult) {
            return null
        }

        var lastTapMillisValue: Long? = null
        val tapMillisDeltas = lastTapMillis.mapNotNull { previousTapMillis ->
            val tapMillisDelta = lastTapMillisValue?.let { it -> previousTapMillis.minus(it) }

            lastTapMillisValue = previousTapMillis
            tapMillisDelta
        }

        return MILLIS_IN_MINUTE / tapMillisDeltas.average().toFloat()
    }

    companion object {
        private const val MILLIS_IN_MINUTE = 60_000f
    }
}