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

class BibActivity : RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "Fragment Activity"
    lateinit var BibChatbot: QiChatbot
    lateinit var BibChat: Chat
    lateinit var topic: Topic
    lateinit var bereich_bib: ImageView
    lateinit var btnWoBib : Button
    lateinit var time_bib: ImageView
    lateinit var btnTimeBib : Button
    lateinit var btnBib : Button
    lateinit var freak : ImageView
    lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bibb)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val locale = Locale(Language.GERMAN, Region.GERMANY)
        btnWoBib = findViewById(R.id.btn_wo_bib)
        btnWoBib.setOnClickListener {
            bereich_bib = findViewById(R.id.iv_bereich_bib)
            bereich_bib.visibility = View.VISIBLE
        }
        btnTimeBib = findViewById(R.id.btn_time_bib)
        btnTimeBib.setOnClickListener {
            time_bib = findViewById(R.id.iv_time_bib)
            time_bib.visibility = View.VISIBLE
        }
        btnBack = findViewById(R.id.btn_back_bib)
        btnBack.setOnClickListener {
            goToDecision()
        }


        topic = TopicBuilder.with(qiContext).withResource(R.raw.top_bib).build()
        BibChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topic).build()
        BibChat = ChatBuilder.with(qiContext).withChatbot(BibChatbot).withLocale(locale).build()
        BibChatbot.addOnBookmarkReachedListener {
            when (it.name) {
                "SHOW_BIB" -> setImage(R.id.iv_bereich_bib)
                "HIDE_BIB" -> clearImage()
                "SHOWTIME_BIB" -> setImageFreak(R.id.iv_time_bib)
                "HIDETIME_BIB" -> clearFoodImage()
            }
        }



        BibChat.addOnStartedListener { goToBookmark("BIB") }
        // RUN chat asynchronously
        val fchat = BibChat.async().run()



        BibChatbot.addOnEndedListener { endReason ->
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
        BibChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    private fun setImage(resource: Int) {
        runOnUiThread {
            bereich_bib = findViewById(resource)
            bereich_bib.visibility = View.VISIBLE
        }
    }

    private fun clearImage() {
        runOnUiThread {
            bereich_bib = findViewById(R.id.iv_bereich_bib)
            bereich_bib.visibility = View.INVISIBLE
        }
    }

    private fun clearFoodImage() {
        runOnUiThread {
            freak = findViewById(R.id.iv_time_bib)
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
