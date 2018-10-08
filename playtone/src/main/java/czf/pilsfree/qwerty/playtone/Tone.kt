package czf.pilsfree.qwerty.library

import android.os.storage.StorageVolume


class Tone (
        val freq: Float,
        val duration: Int,
        val type : WaveType = WaveType.SINE,
        val volume: Int = 100
) {
    companion object {
        const val C0=16.35f
        const val CIS0=17.32f
        const val D0=18.35f
        const val DIS0=19.45f
        const val E0=20.6f
        const val F0=21.83f
        const val FIS0=23.12f
        const val G0=24.5f
        const val GIS0=25.96f
        const val A0=27.5f
        const val AIS0=29.14f
        const val B0=30.87f
        const val C1=32.7f
        const val CIS1=34.65f
        const val D1=36.71f
        const val DIS1=38.89f
        const val E1=41.2f
        const val F1=43.65f
        const val FIS1=46.25f
        const val G1=49f
        const val GIS1=51.91f
        const val A1=55f
        const val AIS1=58.27f
        const val B1=61.74f
        const val C2=65.41f
        const val CIS2=69.3f
        const val D2=73.42f
        const val DIS2=77.78f
        const val E2=82.41f
        const val F2=87.31f
        const val FIS2=92.5f
        const val G2=98f
        const val GIS2=103.83f
        const val A2=110f
        const val AIS2=116.54f
        const val B2=123.47f
        const val C3=130.81f
        const val CIS3=138.59f
        const val D3=146.83f
        const val DIS3=155.56f
        const val E3=164.81f
        const val F3=174.61f
        const val FIS3=185f
        const val G3=196f
        const val GIS3=207.65f
        const val A3=220f
        const val AIS3=233.08f
        const val B3=246.94f
        const val C4=261.63f
        const val CIS4=277.18f
        const val D4=293.66f
        const val DIS4=311.13f
        const val E4=329.63f
        const val F4=349.23f
        const val FIS4=369.99f
        const val G4=392f
        const val GIS4=415.3f
        const val A4=440f
        const val AIS4=466.16f
        const val B4=493.88f
        const val C5=523.25f
        const val CIS5=554.37f
        const val D5=587.33f
        const val DIS5=622.25f
        const val E5=659.25f
        const val F5=698.46f
        const val FIS5=739.99f
        const val G5=783.99f
        const val GIS5=830.61f
        const val A5=880f
        const val AIS5=932.33f
        const val B5=987.77f
        const val C6=1046.5f
        const val CIS6=1108.73f
        const val D6=1174.66f
        const val DIS6=1244.51f
        const val E6=1318.51f
        const val F6=1396.91f
        const val FIS6=1479.98f
        const val G6=1567.98f
        const val GIS6=1661.22f
        const val A6=1760f
        const val AIS6=1864.66f
        const val B6=1975.53f
        const val C7=2093f
        const val CIS7=2217.46f
        const val D7=2349.32f
        const val DIS7=2489.02f
        const val E7=2637.02f
        const val F7=2793.83f
        const val FIS7=2959.96f
        const val G7=3135.96f
        const val GIS7=3322.44f
        const val A7=3520f
        const val AIS7=3729.31f
        const val B7=3951.07f
        const val C8=4186.01f
        const val CIS8=4434.92f
        const val D8=4698.63f
        const val DIS8=4978.03f
        const val E8=5274.04f
        const val F8=5587.65f
        const val FIS8=5919.91f
        const val G8=6271.93f
        const val GIS8=6644.88f
        const val A8=7040f
        const val AIS8=7458.62f
        const val B8=7902.13f
    }
}

enum class WaveType {
    SINE,TRIANGLE,SQUARE,SAWTOOTH,NONE
}