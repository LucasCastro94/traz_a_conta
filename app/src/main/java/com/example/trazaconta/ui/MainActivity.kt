package com.example.trazaconta.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.trazaconta.R
import com.example.trazaconta.databinding.ActivityMainBinding
import com.example.trazaconta.databinding.PersonPopupBinding
import com.example.trazaconta.databinding.PopupListNameBinding
import com.example.trazaconta.entity.Establishment
import com.example.trazaconta.viewModel.MainViewModel
import com.example.trazaconta.viewModel.MainViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val factory : MainViewModelFactory by lazy {MainViewModelFactory(application)}
    private val viewModel by lazy { ViewModelProvider(this,factory)[MainViewModel::class.java] }
    private val bind by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private var backPressedTimestamp = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.genBillId.setOnClickListener(this)
        bind.historyId.setOnClickListener(this)
        bind.myPersonId.setOnClickListener(this)

    }



private fun popupSave()
{
    val dialog = Dialog(this,R.style.customDialog)
    val dialogBinding = PopupListNameBinding
        .inflate(LayoutInflater.from(this))
    dialog.setCancelable(true)
    dialog.setContentView(dialogBinding.root)
    dialog.show()
    val name = dialogBinding.inputPopupNameId

    dialogBinding.btnPopup.setOnClickListener{
        if(validateField(name)){

            val now = LocalDateTime.now()
            val day = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val hour = now.format(DateTimeFormatter.ofPattern("HH:mm"))

            viewModel.insert(Establishment(name=name.text.toString(), date = day.toString(), hour = hour.toString()))
            dialog.dismiss()
            startActivity(name.text.toString())

        }
    }

}


    private fun startActivity(name:String)
    {
        val bundle = Bundle()
        bundle.putString("key", name)
        val i = Intent(this, ListItemActivity::class.java)
        i.putExtras(bundle)
        startActivity(i)
    }


    private fun validateField(text:EditText) : Boolean
    {
        if(text.text.trim().isEmpty())
        {
            text.error = "Informe o nome do local"
            return false
        }
        return true
    }

    override fun onBackPressed() {
        val currentTimestamp = System.currentTimeMillis()

        when{ (currentTimestamp - backPressedTimestamp <= 2000) -> finishAndRemoveTask()
            else ->{
                backPressedTimestamp = currentTimestamp
                Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show() }
        }}

    override fun onClick(p0: View) {
        when(p0.id)
        {
            R.id.history_id ->  startActivity(Intent(this,HistoryActivity::class.java))
            R.id.gen_bill_id -> popupSave()
            R.id.myPerson_id -> myPerson()
        }
    }

    private fun myPerson()
    {
        val dialog = Dialog(this,R.style.customDialog)
        val dialogBinding = PersonPopupBinding
            .inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
       dialog.setCancelable(false)

        dialogBinding.personClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.linkedinId.setOnClickListener {
            val url = "https://www.linkedin.com/in/lucas-castro-578692238/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        dialogBinding.githubId.setOnClickListener {
            val url = "https://github.com/LucasCastro94"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        dialog.show()
    }

}