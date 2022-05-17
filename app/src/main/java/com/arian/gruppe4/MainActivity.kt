package com.arian.gruppe4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        QiSDK.register(this, this)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        val btnStart = findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            goToDecision()
        }
    }

    override fun onRobotFocusLost() {
        TODO("Not yet implemented")
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }

    private fun goToDecision() {
        val changeToDecision = Intent(this, DecisionActivity::class.java)
        startActivity(changeToDecision)
    }
}