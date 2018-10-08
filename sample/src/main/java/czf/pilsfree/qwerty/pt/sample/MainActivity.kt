package czf.pilsfree.qwerty.pt.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import czf.pilsfree.qwerty.library.PlayTone
import czf.pilsfree.qwerty.library.Tone
import czf.pilsfree.qwerty.library.WaveType
import kotlinx.coroutines.experimental.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val si = Tone(Tone.A4,400)
        val t = Tone(Tone.A4,400,WaveType.TRIANGLE)
        val s = Tone(Tone.A4,400,WaveType.SQUARE)
        val st = Tone(Tone.A4,400,WaveType.SAWTOOTH)
        val p_si = PlayTone(si)
        val p_t = PlayTone(t)
        val p_s = PlayTone(s)
        val p_st = PlayTone(st)

        verticalLayout{
            button("PLAY SINE") {
                setOnClickListener {
                    p_si.play()
                }
            }
            button("PLAY TRIANGLE") {
                setOnClickListener {
                    p_t.play()
                }
            }
            button("PLAY SQUARE") {
                setOnClickListener {
                    p_s.play()
                }
            }
            button("PLAY SAWTOOTH") {
                setOnClickListener {
                    p_st.play()
                }
            }
            linearLayout {
                button("T+SQ") {
                    setOnClickListener {
                        p_s.play()
                        p_t.play()
                    }
                }.lparams(width= 0,weight = 1f)
                button("SQ+ST") {
                    setOnClickListener {
                        p_s.play()
                        p_st.play()
                    }
                }.lparams(width= 0,weight = 1f)
                button("T+ST") {
                    setOnClickListener {
                        p_t.play()
                        p_st.play()
                    }
                }.lparams(width= 0,weight = 1f)
                button("T+SQ+ST") {
                    setOnClickListener {
                        p_t.play()
                        p_s.play()
                        p_st.play()
                    }
                }.lparams(width= 0,weight = 1f)
            }.lparams(width= matchParent)
            button("DYNAMIC") {
                setOnClickListener {
                    PlayTone(Tone(1700f,500,WaveType.TRIANGLE)).play{ playTone -> playTone.release() }
                }
            }
            textView() {
                textSize = 40f
                gravity = Gravity.CENTER

                GlobalScope.launch {
                    val ui = CoroutineScope(Dispatchers.Main)
                    while (true) {
                        delay(20)
                        CoroutineScope(Dispatchers.Main).launch {
                            this@textView.text = "instances: ${PlayTone.unreleasedInstancesCount}"
                        }
                    }
                }
            }.lparams(width= matchParent)
        }
    }
}
