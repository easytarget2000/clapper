package eu.ezytarget.clapper

import kotlin.math.floor

public class Clapper {

    private val bpmCounter = BpmCounter()

    var bpm = 150f
        set(value) {
            field = value
            initTickLengthMillis()
            if (VERBOSE) {
                println("DEBUG: BeatMetronome: bpm: $bpm")
            }
        }

    private var startMillis = nowMillis

    private val nowMillis
        get() = System.currentTimeMillis()

    private var numberOfAcknowledgedTicks = 0

    private var tickLengthMillis = 0L

    private lateinit var intervalNumbers: Map<BeatInterval, Int>

    public fun start() {
        startMillis = nowMillis
        initTickLengthMillis()
    }

    public fun update(): Map<BeatInterval, BeatIntervalUpdate> {
        val nowMillis = nowMillis
        val numberOfTicks = ((nowMillis - startMillis) / tickLengthMillis).toInt()
        val changes = if (numberOfTicks == numberOfAcknowledgedTicks) {
            intervalNumbers.map {
                it.key to false
            }.toMap()
        } else {
            numberOfAcknowledgedTicks = numberOfTicks
            val newIntervalNumbers = intervals.map {
                it to floor(x = numberOfAcknowledgedTicks.toDouble() / it.numberOfTicks.toDouble()).toInt()
            }.toMap()

            val changes = newIntervalNumbers.map {
                it.key to (intervalNumbers[it.key] != it.value)
            }.toMap()

            intervalNumbers = newIntervalNumbers
            changes
        }

        return intervalNumbers.map {
            val changed = changes.getValue(it.key)
            it.key to BeatIntervalUpdate(it.value, changed)
        }.toMap()
    }

    public fun tapBpm() {
        val tappedBpm = bpmCounter.tap(nowMillis)
        if (tappedBpm != null) {
            bpm = tappedBpm
        }
    }

    private fun initTickLengthMillis() {
        tickLengthMillis = ((MILLIS_PER_MINUTE / bpm) / BeatInterval.Whole.numberOfTicks.toFloat()).toLong()
    }

    companion object {
        private const val VERBOSE = true
        private const val MILLIS_PER_MINUTE = 60_000f
        private val intervals = listOf(
                BeatInterval.Sixteenth,
                BeatInterval.Eigth,
                BeatInterval.Half,
                BeatInterval.Quarter,
                BeatInterval.Whole,
                BeatInterval.TwoWhole,
                BeatInterval.FourWhole,
                BeatInterval.EightWhole,
                BeatInterval.SixteenWhole,
                BeatInterval.ThirstyTwoWhole
        )

        private val smallestBeatDuration = intervals.first()
        private val smallestNoteToWholeNoteRatio = smallestBeatDuration.numberOfTicks
    }
}