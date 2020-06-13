package eu.ezytarget.clapper

enum class BeatInterval(val numberOfTicks: Int) {
    Sixteenth(numberOfTicks = 1),
    Eigth(numberOfTicks = 2),
    Quarter(numberOfTicks = 4),
    Half(numberOfTicks = 8),
    Whole(numberOfTicks = 16),
    TwoWhole(numberOfTicks = 32),
    FourWhole(numberOfTicks = 64),
    EightWhole(numberOfTicks = 128),
    SixteenWhole(numberOfTicks = 256),
    ThirstyTwoWhole(numberOfTicks = 512)
}