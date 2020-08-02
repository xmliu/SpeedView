package cn.xmliu.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var handler: Handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 1001){
                val speed = (0..60).random()
                speedView.setCurSpeed(speed)
                speedTV.text = speed.toString()
                sendEmptyMessageDelayed(1001,1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler.sendEmptyMessageDelayed(1001,2000)

        speedView.setTextColor(Color.parseColor("#FFFF00"))
        speedView.setPointerColor(Color.parseColor("#BBBBBB"))
        speedView.setMaxSpeed(240)

    }
}