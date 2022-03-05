package com.example.mysevenminuteapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysevenminuteapp.databinding.ActivityExerciseBinding
import com.example.mysevenminuteapp.databinding.ActivityMainBinding
import com.example.mysevenminuteapp.databinding.DialogCustomBackConfirmationBinding
import java.beans.IndexedPropertyChangeEvent
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding?=null

    private var restTimer:CountDownTimer?=null
    private var restProgress=0
    private var restTimerDuration:Long=1

    private var exerciseTimer:CountDownTimer?=null
    private var exerciseProgress=0
    private var exerciseTimerDuration:Long=2

    private var exerciseList:ArrayList<ExerciseModel>? =null
    private var currentExercisePosition= -1

    private var tts: TextToSpeech?=null

    private var player:MediaPlayer?=null

    private var exerciseAdapter:ExerciseStatusAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExrecise)
        if (supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExrecise?.setNavigationOnClickListener {
           // onBackPressed()
            CustomDialogForBackButtun()
        }

        exerciseList=Constants.defaultExerciseList()


        setRestView()

        tts= TextToSpeech(this,this)

        setupExerciseStatusRecyclerView()//before this, you had to adjust exersiceList

    }
    //FUN

    override fun onBackPressed() {
        CustomDialogForBackButtun()
    }

    //back Button dialog
    private fun CustomDialogForBackButtun(){
        val customDialog=Dialog(this,R.style.Theme_Dialog)
        val dialogBinding=DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.yes.setOnClickListener{
          //  this.finish()
            finish()
            customDialog.dismiss()
        }
        dialogBinding.no.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }
    //back Button dialog

    private fun setupExerciseStatusRecyclerView(){
    binding?.rvExerciseStatus?.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter= ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter=exerciseAdapter
    }

    private fun setRestView(){
        try {
            val soundURI=Uri.parse("android.resource://com.example.mysevenminuteapp/" + R.raw.press_start)
            player=MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping=false
            player?.start()
        }catch(e:Exception){
            e.printStackTrace()
        }
        binding?.flRest?.visibility= View.VISIBLE
        binding?.tvTitile?.visibility=View.VISIBLE
        binding?.tvExerciseName?.visibility=View.INVISIBLE
        binding?.flExercise?.visibility=View.INVISIBLE
        binding?.ivImage?.visibility=View.INVISIBLE
        binding?.tvUpComingLabel?.visibility=View.VISIBLE
        binding?.tvUpComingExerciseName?.visibility=View.VISIBLE

        binding?.tvUpComingExerciseName?.text=exerciseList!![currentExercisePosition + 1].getName()
      //  speakOut("next exercise is "+ binding?.tvUpComingExerciseName?.text.toString())

        if (restTimer!= null){
            restTimer?.cancel()
            restProgress=0
        }
        setRestProgressBar()
    }

    private fun setExerciseView(){
        binding?.flRest?.visibility= View.INVISIBLE
        binding?.tvTitile?.visibility=View.INVISIBLE
        binding?.tvExerciseName?.visibility=View.VISIBLE
        binding?.flExercise?.visibility=View.VISIBLE
        binding?.ivImage?.visibility=View.VISIBLE
        binding?.tvUpComingLabel?.visibility=View.INVISIBLE
        binding?.tvUpComingExerciseName?.visibility=View.INVISIBLE

        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text=exerciseList!![currentExercisePosition].getName()
        speakOut(exerciseList!![currentExercisePosition].getName())


        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress=restProgress
        restTimer = object : CountDownTimer (restTimerDuration*1000,1000 ){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress=10 - restProgress
                binding?.tvTimer?.text=(10 - restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setExerciseView()
            }
                  }.start()
        }

    private fun setExerciseProgressBar(){
        binding?.progressBarExersice?.progress=exerciseProgress
        exerciseTimer = object : CountDownTimer (exerciseTimerDuration*1000,1000 ){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExersice?.progress=30 - exerciseProgress
                binding?.tvTimerExercise?.text=(30 - exerciseProgress).toString()
            }
            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                    setRestView()
                }else{
                    finish()
                    val intent=Intent(this@ExerciseActivity,Finish::class.java)
                    startActivity(intent)

                }
            }
        }.start()
    }
    //text to speech
    private fun speakOut(text:String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH, null,"")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result== TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("tts","failed")
            }else{
                Log.e("tts","failed")
            }
        }
    }
    //text to speech


























    override fun onDestroy() {
        super.onDestroy()
        if (restTimer!= null){
            restTimer?.cancel()
            restProgress=0
        }
        if (exerciseTimer!=null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }
        if (player != null){
            player!!.stop()
        }
        binding = null
    }


}