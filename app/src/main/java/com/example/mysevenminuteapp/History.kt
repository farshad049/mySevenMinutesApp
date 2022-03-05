package com.example.mysevenminuteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysevenminuteapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class History : AppCompatActivity() {
    private var binding: ActivityHistoryBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val historyDao= (application as HistoryApp).db.historyDao()

        setSupportActionBar(binding?.toolbarHistory)
        if (supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title="History"
        }
        binding?.toolbarHistory?.setNavigationOnClickListener {
             onBackPressed()
        }

        setUpDataToRecyclerView(historyDao)







    }//FUN
    private fun setUpDataToRecyclerView(historyDao:HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllHistories().collect { allCompletedDateList ->
                if (allCompletedDateList.isNotEmpty()) {
                    binding?.exerciseCompleted?.visibility = View.VISIBLE
                    binding?.recycleView?.layoutManager = LinearLayoutManager(this@History)
                    binding?.recycleView?.visibility = View.VISIBLE
                    val dates=ArrayList<String>()
                    for (date in allCompletedDateList){
                        dates.add(date.date)
                    }
                    binding?.recycleView?.adapter = HistoryAdapter(dates)
                } else {
                    binding?.noHistoryFound?.visibility = View.VISIBLE
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}