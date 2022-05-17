package com.arian.gruppe4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class MensaActivity : RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "Fragment Activity"
    lateinit var mensaChatbot: QiChatbot
    lateinit var mensaChat: Chat
    lateinit var topic: Topic
    lateinit var bereich_mensa: ImageView
    lateinit var time_mensa: ImageView
    lateinit var btnWoMensa : Button
    lateinit var btnTimeMensa : Button
    lateinit var btnFood : Button
    lateinit var freak : ImageView
    lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensa)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val locale = Locale(Language.GERMAN, Region.GERMANY)
        btnWoMensa = findViewById(R.id.btn_wo_mensa)
        btnWoMensa.setOnClickListener {
            bereich_mensa = findViewById(R.id.iv_bereich_mensa)
            bereich_mensa.visibility = View.VISIBLE
        }
        btnTimeMensa = findViewById(R.id.btn_time_mensa)
        btnTimeMensa.setOnClickListener {
            time_mensa = findViewById(R.id.iv_time_mensa)
            time_mensa.visibility = View.VISIBLE
        }

        btnBack = findViewById(R.id.btn_back_mensa)
        btnBack.setOnClickListener {
            goToDecision()
        }


        topic = TopicBuilder.with(qiContext).withResource(R.raw.top_mensa).build()
        mensaChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topic).build()
        mensaChat = ChatBuilder.with(qiContext).withChatbot(mensaChatbot).withLocale(locale).build()
        mensaChatbot.addOnBookmarkReachedListener {
            when (it.name) {
                "SHOW_MENSA" -> setImage(R.id.iv_bereich_mensa)
                "HIDE_MENSA" -> clearImage()
                "SHOWFOOD_MENSA" -> setImageFreak(R.id.iv_time_mensa)
                "HIDEFOOD_MENSA" -> clearFoodImage()
            }
        }



        mensaChat.addOnStartedListener { goToBookmark("MENSA") }
        // RUN chat asynchronously
        val fchat = mensaChat.async().run()



        mensaChatbot.addOnEndedListener { endReason ->
            Log.i(TAG, "qichatbot end reason = $endReason")
            fchat.requestCancellation()
            goToDecision()
        }
    }

    private fun goToDecision() {
        val changeToDecision = Intent(this, DecisionActivity::class.java)
        startActivity(changeToDecision)
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    private fun goToBookmark(bookmarkName: String) {
        mensaChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    private fun setImage(resource: Int) {
        runOnUiThread {
            bereich_mensa = findViewById(resource)
            bereich_mensa.visibility = View.VISIBLE
        }
    }

    private fun clearImage() {
        runOnUiThread {
            bereich_mensa = findViewById(R.id.iv_bereich_mensa)
            bereich_mensa.visibility = View.INVISIBLE
        }
    }

    private fun clearFoodImage() {
        runOnUiThread {
            freak = findViewById(R.id.iv_time_mensa)
            freak.visibility = View.INVISIBLE
        }
    }
    private fun setImageFreak(resource: Int) {
        runOnUiThread {
            freak = findViewById(resource)
            freak.visibility = View.VISIBLE
        }
    }

}
