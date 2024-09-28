package br.com.pedro.cronometro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.pedro.cronometro.databinding.ActivityMainBinding
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dataHelper:DataHelper

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)


        binding.startButton.setOnClickListener{startStopAction()}
        binding.resetButton.setOnClickListener{resetAction()}

        if(dataHelper.timerCouting()){
            startTimer()
        }else{
            stopTimer()
            if(dataHelper.startTime() != null &&  dataHelper.stopTime() != null){
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private inner class TimeTask: TimerTask(){
        override fun run() {

            if(dataHelper.timerCouting()){
                val time = Date().time - dataHelper.startTime()!!.time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

    }


    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }



    private fun stopTimer() {

        dataHelper.setTimerCounting(false)
        binding.startButton.text = getString(R.string.start)

    }
    private fun startTimer() {

        dataHelper.setTimerCounting(true)
        binding.startButton.text = getString(R.string.stop)

    }

    private fun startStopAction() {

        if(dataHelper.timerCouting()){
            dataHelper.setStopTime(Date())
            stopTimer()
        }else{

            if(dataHelper.stopTime() != null){
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }

            else{
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date {

        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes  = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)

        return makeTimeString(hours,minutes,seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours,minutes,seconds)
    }
}