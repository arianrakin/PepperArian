package com.arian.gruppe4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.Phrase
import com.aldebaran.qi.sdk.`object`.conversation.Say
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class DecisionActivity : RobotActivity(), RobotLifecycleCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragen_nach_start)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val phrase: Phrase = Phrase("Hallo - wie kann ich dir helfen? Bitte drücke einen Button")
        val say : Say = SayBuilder.with(qiContext).withPhrase(phrase).build()
        say.run()

        val btnBib = findViewById<Button>(R.id.btn_start_bib)
        btnBib.setOnClickListener {
            gotoBib()

        }
        val btnMensa = findViewById<Button>(R.id.btn_start_mensa)
        btnMensa.setOnClickListener {
            gotoMensa()

        }
        val btnStb = findViewById<Button>(R.id.btn_start_stb)
        btnStb.setOnClickListener {
            gotoStb()

        }
        val btnFAQ = findViewById<Button>(R.id.btn_start_faq)
        btnFAQ.setOnClickListener {
            gotoFAQ()

        }



    }


    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    private fun gotoMensa() {
        val changeToMensa = Intent(this, MensaActivity::class.java)
        startActivity(changeToMensa)


    }
    private fun gotoBib() {
        val changeToBib = Intent(this, BibActivity::class.java)
        startActivity(changeToBib)


    }
    private fun gotoStb() {
        val changeToStd = Intent(this, StudienbueroActivity::class.java)
        startActivity(changeToStd)


    }
    private fun gotoFAQ() {
        val changeToFAQ = Intent(this, FAQActivity::class.java)
        startActivity(changeToFAQ)


    }
}