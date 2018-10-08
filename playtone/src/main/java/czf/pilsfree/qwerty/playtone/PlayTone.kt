package czf.pilsfree.qwerty.library

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.experimental.*

class PlayTone(vararg tones:Tone) {

    // public
    fun play(onfinish: (PlayTone) -> Unit = {}) { _play(onfinish) }

    fun stop() { _stop() }

    fun release() { _release() }

    @Volatile var isRunning = false
        private set

    //private
    private val _tones = listOf(*tones)
    private lateinit var remainingTones : MutableList<Tone>


    private var audioTrack = AudioTrack(AA, NEW_AUDIO_FORMAT, BUFFER_SIZE,MODE,AudioManager.AUDIO_SESSION_ID_GENERATE)
    // google pls this new style of constructor sux, stop doing this, it sux more than old one
    @Volatile private var released = false

    init {
        if (++unreleasedInstancesCount>=32) {
            throw java.lang.IllegalStateException("Too many instances of PlayTone, maximum is 32")
        }

    }

    private var myCoroutine : Job? =null

    private fun _play(onfinish: (PlayTone) -> Unit) {
        if (isRunning||_tones.isEmpty()) return
        isRunning = true
        runBlocking {
            myCoroutine?.cancelAndJoin()
        }
        if (released) {
            throw java.lang.IllegalAccessException("This instance was already released")
        }

        myCoroutine = bg.launch {
            var finnished = true
            try {
                remainingTones = _tones.toMutableList()
                val buffer = ShortArray(BUFFER_SIZE / 2)
                var currentTone = remainingTones.removeAt(0)
                var currentToneRemainingSamples = (SAMPLE_RATE * currentTone.duration) / 1000
                Log.d(TAG,"REMAINING: $currentToneRemainingSamples")
                var currentToneSample = 0
                var currentSample = 0

                fun generateWave(offset: Int, count: Int) {
                    for (i in 0..count-1) {
                        when (currentTone.type) {
                            WaveType.SINE -> {
                                val norm = Math.sin(2.0 * Math.PI * currentToneSample.toDouble() / (SAMPLE_RATE / currentTone.freq))
                                buffer[i + offset] = (norm * Short.MAX_VALUE).toShort()
                            }
                            WaveType.SQUARE -> {
                                val norm = Math.sin(2.0 * Math.PI * currentToneSample.toDouble() / (SAMPLE_RATE / currentTone.freq))
                                buffer[i + offset] = if (norm >= 0.0) Short.MAX_VALUE else Short.MIN_VALUE
                            }
                            WaveType.TRIANGLE -> {
                                val x = currentToneSample.toDouble() / (SAMPLE_RATE / currentTone.freq)
                                val y = Math.abs(((x - Math.floor(x)) * 2.0 - 1)) * 2.0 - 1
                                buffer[i+offset] = (y * Short.MAX_VALUE).toShort()
                            }
                            WaveType.SAWTOOTH -> {
                                val x = currentToneSample.toDouble() / (SAMPLE_RATE / currentTone.freq)
                                val y = (x - Math.floor(x)) * 2.0 - 1
                                buffer[i+offset] = (y * Short.MAX_VALUE).toShort()
                            }
                            WaveType.NONE -> {
                                buffer[i + offset] = 0
                            }
                        }
                        currentSample++
                        currentToneSample++
                    }
                }

                fun fillBuffer(): Int {
                    var free = buffer.size
                    while (free != 0) {
                        if (currentToneRemainingSamples > free) {
                            generateWave(buffer.size - free, free)
                            currentToneRemainingSamples -= free
                            free = 0
                        } else {
                            generateWave(buffer.size - free, currentToneRemainingSamples)
                            free -= currentToneRemainingSamples
                            if (remainingTones.isNotEmpty()) {
                                currentTone = remainingTones.removeAt(0)
                                currentToneRemainingSamples = (SAMPLE_RATE * currentTone.duration) / 1000
                                currentToneSample = 0
                            } else {
                                currentToneRemainingSamples = 0
                                return buffer.size - free
                            }
                        }
                    }
                    return buffer.size
                }
                var fill = fillBuffer()
                audioTrack.write(buffer, 0, fill)
                audioTrack.play()
                while (remainingTones.isNotEmpty() || currentToneRemainingSamples > 0) {
                    fill = fillBuffer()
                    audioTrack.write(buffer, 0, fill)
                    yield()
                    Log.d(TAG,"FEED remaining : ${remainingTones.size} rem_samples : $currentToneRemainingSamples")
                }
                audioTrack.notificationMarkerPosition = currentSample
                audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                    override fun onMarkerReached(track: AudioTrack?) {
                        ui.launch { onfinish(this@PlayTone) }
                    }

                    override fun onPeriodicNotification(track: AudioTrack?) { }
                })
                finnished = true

            } finally {
                if (!finnished) {
                    audioTrack.stop()
                }
                isRunning = false
            }
        }

    }

    private fun _stop() {
        if (!isRunning) return
        runBlocking {
            myCoroutine?.cancelAndJoin()
        }
    }

    private fun _release() {
        _stop()
        audioTrack.release()
        unreleasedInstancesCount--
    }

    companion object {

        // constructor constants, google pls slow down a bit
        private const val STREAM_TYPE = AudioManager.STREAM_MUSIC
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val MODE = AudioTrack.MODE_STREAM
        private val SAMPLE_RATE = AudioTrack.getNativeOutputSampleRate(STREAM_TYPE)
        private val BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)
        private val NEW_AUDIO_FORMAT = AudioFormat.Builder()
                .setEncoding(AUDIO_FORMAT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .build()!!
        private val AA = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()!!

        private const val TAG = "PlayTone"


        // coroutines dispatchers
        private val bg = CoroutineScope(Dispatchers.Default)
        private val ui = CoroutineScope(Dispatchers.Main)

        @Volatile var unreleasedInstancesCount = 0
            private set

    }

}
