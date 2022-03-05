package com.example.mysevenminuteapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mysevenminuteapp.databinding.ActivityBmiBinding
import java.math.RoundingMode


class BmiActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW="METRIC_UNITS_VIEW"
        private const val US_UNIT_VIEW="US_UNIT_VIEW"
    }

    private var currentVisibleView:String=METRIC_UNITS_VIEW

    private var binding: ActivityBmiBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBim)
        if (supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title="Calculate BMI"
        }
        binding?.toolbarBim?.setNavigationOnClickListener {
             onBackPressed()
        }

        makeVisibleMetricUnitsView()
        
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId:Int ->
            if (checkedId == binding?.rbMetricUnits?.id){
                makeVisibleMetricUnitsView()
        }else{
                makeVisibleUsUnitsView()
           }
        }

        binding?.btnCalculateBmi?.setOnClickListener {
            calculateUnits()
        }




    }
    //FUN
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView= METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility=View.VISIBLE
        binding?.tilMetricUnitHeight?.visibility=View.VISIBLE
        binding?.tilUsUnitWeight?.visibility=View.GONE
        binding?.tilFeetUnitHeight?.visibility=View.GONE
        binding?.tilInchUnitHeight?.visibility=View.GONE

        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.text!!.clear()

        binding?.liDisplayBmi?.visibility=View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView(){
        currentVisibleView= US_UNIT_VIEW
        binding?.tilMetricUnitWeight?.visibility=View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility=View.INVISIBLE
        binding?.tilUsUnitWeight?.visibility=View.VISIBLE
        binding?.tilFeetUnitHeight?.visibility=View.VISIBLE
        binding?.tilInchUnitHeight?.visibility=View.VISIBLE

        binding?.etFeetUnitHeight?.text!!.clear()
        binding?.etInchUnitHeight?.text!!.clear()
        binding?.etUsUnitWeight?.text!!.clear()

        binding?.liDisplayBmi?.visibility=View.INVISIBLE
    }

    private fun calculateBmi(result:Float){
        if (result <15f){
            binding?.tvBmiType?.text="very severaly under weight"
            binding?.tvBmiDescription?.text="you need to take care better of yourselt, Eat more"
        }else if (result >16f && result <18.5f){
            binding?.tvBmiType?.text="severly under weight"
            binding?.tvBmiDescription?.text="you need to take care better of yourselt, Eat more"
        }else if (result>18.5f && result<25f){
            binding?.tvBmiType?.text="normal"
            binding?.tvBmiDescription?.text="congradulations, you are in a good shape"
        }else if (result>25f && result<30f){
            binding?.tvBmiType?.text="over weight"
            binding?.tvBmiDescription?.text="you need to take better care of yourself ,workout more"
        }else if (result>30f && result<35f){
            binding?.tvBmiType?.text="obese"
            binding?.tvBmiDescription?.text="you need to take better care of yourself ,workout more"
        }else if (result>35f && result<40f){
            binding?.tvBmiType?.text="severely obese"
            binding?.tvBmiDescription?.text="omg you are in a very dangerous situation , Act now!"
        }else {
            binding?.tvBmiType?.text = "severely obese"
            binding?.tvBmiDescription?.text = "omg you are in a very dangerous situation , Act now!"
        }
        binding?.liDisplayBmi?.visibility=View.VISIBLE

        binding?.tvBmiValue?.text=java.math.BigDecimal(result.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
    }

    private fun validateMetricUnit():Boolean{
        var isValid=true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid=false
        }else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid=false
        }
        return isValid
    }

    private fun calculateUnits(){
        if (currentVisibleView==METRIC_UNITS_VIEW){
            if (validateMetricUnit()){
                val weightValue:Float=binding?.etMetricUnitWeight?.text.toString().toFloat()
                val heightValue:Float=binding?.etMetricUnitHeight?.text.toString().toFloat() /100
                val result= weightValue /(heightValue * heightValue)
                calculateBmi(result)
            }else{
                Toast.makeText(this,"please enter valid values",Toast.LENGTH_SHORT).show()
            }
        }else{
            if (validateUscUnit()){
                val weightPoundsValue:Float=binding?.etUsUnitWeight?.text.toString().toFloat()
                val heightFeetValue:Float=binding?.etFeetUnitHeight?.text.toString().toFloat()
                val heightInchValue:Float=binding?.etInchUnitHeight?.text.toString().toFloat()
                val height:Float=heightFeetValue*12 +heightInchValue
                val result= weightPoundsValue/(height*height)*703
                calculateBmi(result)
            }else{
                Toast.makeText(this,"please enter valid values",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUscUnit():Boolean{
        var isValid=true
        when{
            binding?.etUsUnitWeight?.text.toString().isEmpty() -> {isValid=false}
            binding?.etFeetUnitHeight?.text.toString().isEmpty() -> {isValid=false}
            binding?.etInchUnitHeight?.text.toString().isEmpty() -> {isValid=false}
        }
        return isValid
    }










    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}