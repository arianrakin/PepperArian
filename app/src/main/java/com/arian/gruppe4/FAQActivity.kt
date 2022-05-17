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

class FAQActivity : RobotActivity(), RobotLifecycleCallbacks {
    val TAG = "Fragment Activity"
    lateinit var FAQChatbot: QiChatbot
    lateinit var FAQChat: Chat
    lateinit var topic: Topic
    lateinit var int_FAQ: ImageView
    lateinit var btnIntFAQ : Button
    lateinit var wc_FAQ: ImageView
    lateinit var btnWcFAQ : Button
    lateinit var multica_FAQ: ImageView
    lateinit var btnMulticaFAQ : Button
    lateinit var btnFAQ : Button
    lateinit var freak : ImageView
    lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqq)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val locale = Locale(Language.GERMAN, Region.GERMANY)
        btnIntFAQ = findViewById(R.id.btn_wo_inter)
        btnIntFAQ.setOnClickListener {
            int_FAQ = findViewById(R.id.iv_inter)
            int_FAQ.visibility = View.VISIBLE
            //clearWcImage()
            //clearMulticaImage()
        }
        btnWcFAQ = findViewById(R.id.btn_wo_wc)
        btnWcFAQ.setOnClickListener {
            wc_FAQ = findViewById(R.id.iv_wc)
            wc_FAQ.visibility = View.VISIBLE
            //clearMulticaImage()
            //clearInterImage()
        }
        btnMulticaFAQ = findViewById(R.id.btn_wo_multicard)
        btnMulticaFAQ.setOnClickListener {
            multica_FAQ = findViewById(R.id.iv_card)
            multica_FAQ.visibility = View.VISIBLE
            //if(wc_FAQ.visibility == 0) {
            //    clearWcImage()
            //}
            /*else if(int_FAQ.visibility == 1) {
                clearInterImage()
            }*/
        }
        btnBack = findViewById(R.id.btn_back_faq)
        btnBack.setOnClickListener {
            goToDecision()
        }


        topic = TopicBuilder.with(qiContext).withResource(R.raw.top_faq).build()
        FAQChatbot = QiChatbotBuilder.with(qiContext).withLocale(locale).withTopic(topic).build()
        FAQChat = ChatBuilder.with(qiContext).withChatbot(FAQChatbot).withLocale(locale).build()
        FAQChatbot.addOnBookmarkReachedListener {
            when (it.name) {
                "SHOW_TOI" -> setWcImage(R.id.iv_wc)
                "HIDE_TOI" -> clearWcImage()
                "SHOW_INTER" -> setInterImage(R.id.iv_inter)
                "HIDE_INTER" -> clearInterImage()
                "SHOW_MULTI" -> setImageMuiltca(R.id.iv_card)
                "HIDE_MULIT" -> clearMulticaImage()
            }
        }



        FAQChat.addOnStartedListener { goToBookmark("FAQ") }
        // RUN chat asynchronously
        val fchat = FAQChat.async().run()



        FAQChatbot.addOnEndedListener { endReason ->
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
        FAQChatbot.goToBookmark(
            topic.bookmarks[bookmarkName],
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE)
    }

    private fun setInterImage(resource: Int) {
        runOnUiThread {
            int_FAQ = findViewById(resource)
            int_FAQ.visibility = View.VISIBLE
        }
    }

    private fun clearInterImage() {
        runOnUiThread {
            int_FAQ = findViewById(R.id.iv_inter)
            int_FAQ.visibility = View.INVISIBLE
        }
    }

    private fun clearMulticaImage() {
        runOnUiThread {
            multica_FAQ = findViewById(R.id.iv_card)
            multica_FAQ.visibility = View.INVISIBLE
        }
    }
    private fun setImageMuiltca(resource: Int) {
        runOnUiThread {
            multica_FAQ = findViewById(resource)
            multica_FAQ.visibility = View.VISIBLE
        }
    }

    private fun setWcImage(resource: Int) {
        runOnUiThread {
            wc_FAQ = findViewById(resource)
            wc_FAQ.visibility = View.VISIBLE
        }
    }
    private fun clearWcImage() {
        runOnUiThread {
            wc_FAQ = findViewById(R.id.iv_wc)
            wc_FAQ.visibility = View.INVISIBLE
        }
    }

}
