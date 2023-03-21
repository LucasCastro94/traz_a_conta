package com.example.trazaconta.ui

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trazaconta.R
import com.example.trazaconta.adapter.ListItemViewAdapter
import com.example.trazaconta.data.GuestDataBase
import com.example.trazaconta.data.SecurityPreferences
import com.example.trazaconta.databinding.ActivityListItemBinding
import com.example.trazaconta.entity.Item
import com.example.trazaconta.utils.Constants
import com.example.trazaconta.utils.CurrencyMaskTextWatcher
import com.example.trazaconta.viewModel.ListItemViewModel
import com.example.trazaconta.viewModel.ListViewModelFactory

class ListItemActivity : AppCompatActivity(), View.OnClickListener, ListItemViewAdapter.onClick {

    private val layoutManager by lazy {LinearLayoutManager(this)}
    private val bind by lazy { ActivityListItemBinding.inflate(layoutInflater)}
    private val factory : ListViewModelFactory by lazy {ListViewModelFactory(GuestDataBase.getDatabase(this).guestDao())}
    private val adapter by lazy {ListItemViewAdapter(this,this)}
    private val viewModel by lazy { ViewModelProvider(this,factory)[ListItemViewModel::class.java] }
    private val mShared by lazy {SecurityPreferences(this)}
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        toolbar()

        applyMask()

        bind.btnListId.setOnClickListener(this)

        bind.recyclerListview.layoutManager = layoutManager
        bind.recyclerListview.adapter = adapter

        observers()
    }

    private fun toolbar()
    {
        toolbar = bind.toolbar
        toolbar.title=getExtras()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun getExtras():String {
        val bundle = intent.extras
        val key = bundle?.getString("key")
        return key?:""
    }



    private fun observers()
    {
        viewModel.itemList.observe(this) { itemList ->
            adapter.submitList(itemList)

            viewModel.sumResult.observe(this) { sumResult ->

                bind.resultId.text = if(itemList.isNotEmpty()) sumResult else ""
            }
        }
    }

    private fun popupEdit(item: Item)
    {
        val dialog = Dialog(this,R.style.customDialog)
        dialog.setContentView(R.layout.popup_edit_item)
        dialog.setCancelable(true)

        val name = dialog.findViewById<EditText>(R.id.edit_name_list)
        val price = dialog.findViewById<EditText>(R.id.edit_value_list)
        val btn = dialog.findViewById<Button>(R.id.btn_update_edit_list)
        price.addTextChangedListener(CurrencyMaskTextWatcher(price))

        name.setText(item.nameitem)
        price.setText(String.format("%.2f",item.price))


        btn.setOnClickListener{
            if(!validSaveItemList(name,price))
            {
                item.price = CurrencyMaskTextWatcher(price).unmask()
                item.nameitem = name.text.toString()
                viewModel.updateItem(item)
                dialog.dismiss()
                attView()
            }
        }
        dialog.show()
    }

    private fun saveItemList()
    {
            val name:String = bind.inputTextNameList.text.toString().trim()
            val value:Float = CurrencyMaskTextWatcher(bind.inputTextValueList).unmask()

            viewModel.insertItem(name,value,mShared.getFK(Constants.KEYS.toListItem))

            clearFields()
    }

    private fun clearFields()
    {
        bind.inputTextNameList.setText("")
        bind.inputTextValueList.setText("0")
        bind.inputTextNameList.requestFocus()

    }

   private fun validSaveItemList(name: EditText, valueIn: EditText) : Boolean
    {
        val value = if (valueIn.text.trim().isNotEmpty()) CurrencyMaskTextWatcher(valueIn).unmask() else 0.0f

        if(name.text.trim().isEmpty())
        {
            name.error = "Informe o nome"
            return true
        }
        if (value==0.0f)
        {
            valueIn.error = "Informe o valor"
            return true
        }
            return false
    }

   private fun applyMask()
    {
        val editText = bind.inputTextValueList
        editText.addTextChangedListener(CurrencyMaskTextWatcher(editText))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = mShared.getFK(Constants.KEYS.toListItem)
       when(item.itemId)
       {
           R.id.tollbar_prec_desc -> viewModel.getList(id,Constants.FILTER.desc)

           R.id.tollbar_prec_cresc -> viewModel.getList(id,Constants.FILTER.asc)

           R.id.tollbar_ordem_alfabetica -> viewModel.getList(id,Constants.FILTER.alf)

           R.id.tollbar_entrada -> viewModel.getList(id,Constants.FILTER.ins)

           else -> return super.onOptionsItemSelected(item)

       }
        return false

    }

    override fun onClick(view: View) {
      when(view.id)
      {
          R.id.btn_list_id -> if(!validSaveItemList(bind.inputTextNameList,bind.inputTextValueList)) saveItemList()

      }
    }

    private fun attView() = viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.ins)

    override fun delete(item: Item) {
    viewModel.deleteItem(item)
        attView()
    }

    override fun update(item: Item) {
        popupEdit(item)

    }

}