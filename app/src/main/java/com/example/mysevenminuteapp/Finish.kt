package com.example.mysevenminuteapp


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.mysevenminuteapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
class Finish : AppCompatActivity() {
    private var binding: ActivityFinishBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val historyDao= (application as HistoryApp).db.historyDao()

        setSupportActionBar(binding?.toolbarExrecise)
        if (supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExrecise?.setNavigationOnClickListener {
            onBackPressed()
        }



        binding?.BtnFinish?.setOnClickListener {
            finish()
        }

        addRecord(historyDao)


    }
    //FUN
    fun addRecord(historyDao: HistoryDao){
        val myCalendar=Calendar.getInstance()
        val dateTime=myCalendar.time
        Log.e("Date: ",""+dateTime)
        val sdf= SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
        val date=sdf.format(dateTime)

            lifecycleScope.launch {
                historyDao.insert(HistoryEntity(date))
            }
    }








    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}