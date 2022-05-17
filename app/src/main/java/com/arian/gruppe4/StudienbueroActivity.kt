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

class StudienbueroActivity : RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "Fragment Activity"
    lateinit var StudienbueroChatbot: QiChatbot
    lateinit var StudienbueroChat: Chat
    lateinit var topic: Topic
    lateinit var bereich_Studienbuero: ImageView
    lateinit var btnWoStudienbuero : Button
    lateinit var time_Studienbuero: ImageView
    lateinit var btnTimeStudienbuero : Button
    lateinit var btnFood : Button
    lateinit var freak : ImageView
    lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studienburo)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val locale = Locale(Language.GERMAN, Region.GERMANY)
        btnWoStudienbuero = findViewById(R.id.btn_wo_std)
        btnWoStudienbuero.setOnClickListener {
            bereich_Studienbuero = findViewById(R.id.iv_bereich_std)
            bereich_Studienbuero.visibility = View.VISIBLE
        }
        btnTimeStudienbuero = findViewById(R.id.btn_time_std)
        btnTimeStudienbuero.setOnClickListener {
            time_Studienbuero = findViewById(R.id.iv_time_std)
            time_Studienbuero.visibility = View.VISIBLE
        }
        btnBack = findViewById(R.id.btn_back_std)
        btnBack.setOnClickListener {
            goToDecision()
        }


        topic = TopicBuilder.with(qiContext).withResource(R.raw.top_studienbuero).build()
        StudienbueroChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topic).build()
        StudienbueroChat = ChatBuilder.with(qiContext).withChatbot(StudienbueroChatbot).withLocale(locale).build()
        StudienbueroChatbot.addOnBookmarkReachedListener {
            when (it.name) {
                "SHOW_STB" -> setImage(R.id.iv_bereich_std)
                "HIDE_STB" -> clearImage()
                "SHOWTIME_STB" -> setImageFreak(R.id.iv_time_std)
                "HIDETIME_STB" -> clearFoodImage()
            }
        }



        StudienbueroChat.addOnStartedListener { goToBookmark("STB") }
        // RUN chat asynchronously
        val fchat = StudienbueroChat.async().run()



        StudienbueroChatbot.addOnEndedListener { endReason ->
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
        StudienbueroChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    private fun setImage(resource: Int) {
        runOnUiThread {
            bereich_Studienbuero = findViewById(resource)
            bereich_Studienbuero.visibility = View.VISIBLE
        }
    }

    private fun clearImage() {
        runOnUiThread {
            bereich_Studienbuero = findViewById(R.id.iv_bereich_std)
            bereich_Studienbuero.visibility = View.INVISIBLE
        }
    }

    private fun clearFoodImage() {
        runOnUiThread {
            freak = findViewById(R.id.iv_time_std)
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
