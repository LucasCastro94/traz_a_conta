package com.example.trazaconta.ui

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trazaconta.R
import com.example.trazaconta.adapter.HistoryItemViewAdapter
import com.example.trazaconta.adapter.ListItemViewAdapter
import com.example.trazaconta.data.GuestDataBase
import com.example.trazaconta.data.SecurityPreferences
import com.example.trazaconta.databinding.ActivityHistoryItemBinding
import com.example.trazaconta.entity.Item
import com.example.trazaconta.utils.Constants
import com.example.trazaconta.utils.CurrencyMaskTextWatcher
import com.example.trazaconta.viewModel.HistoryItemViewModel
import com.example.trazaconta.viewModel.HistoryItemViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryItemActivity : AppCompatActivity(), ListItemViewAdapter.onClick,HistoryItemViewAdapter.ItemListCallback {

    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val adapter by lazy { HistoryItemViewAdapter(this,this,this) }
    private val bind by lazy {  ActivityHistoryItemBinding.inflate(layoutInflater)}
    private val factory : HistoryItemViewModelFactory by lazy { HistoryItemViewModelFactory(GuestDataBase.getDatabase(this).guestDao()) }
    private val viewModel by lazy { ViewModelProvider(this,factory)[HistoryItemViewModel::class.java] }
    private val mShared by lazy { SecurityPreferences(this) }
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        toolbar()

        bind.recyclerItemHistory.layoutManager = layoutManager
        bind.recyclerItemHistory.adapter = adapter
        observers()
        attView()
    }


    private fun observers()
    {
        viewModel.itemList.observe(this) { itemList ->
            adapter.submitList(itemList)

            viewModel.sumResult.observe(this) { sumResult ->

                bind.resultIdItemHistory.text = if(itemList.isNotEmpty()) sumResult else ""
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
                attEstablishment()
                dialog.dismiss()
                attView()
            }
        }
        dialog.show()
    }


    @SuppressLint("SetTextI18n")
    private fun popupInsert()
    {
        val dialog = Dialog(this,R.style.customDialog)
        dialog.setContentView(R.layout.popup_edit_item)
        dialog.setCancelable(true)

        val name = dialog.findViewById<EditText>(R.id.edit_name_list)
        val price = dialog.findViewById<EditText>(R.id.edit_value_list)
        val btn = dialog.findViewById<Button>(R.id.btn_update_edit_list)
        btn.text = "Salvar"
        price.addTextChangedListener(CurrencyMaskTextWatcher(price))


        btn.setOnClickListener{
            if(!validSaveItemList(name,price))
            {

                viewModel.insertItem(
                    name.text.toString(),
                    CurrencyMaskTextWatcher(price).unmask(),
                    mShared.getFK(Constants.KEYS.toListItem))
                attEstablishment()
                dialog.dismiss()
                attView()
            }
        }
        dialog.show()
    }

    private fun attEstablishment(){
        val establishment = viewModel.getEstablishmentByID(mShared.getFK(Constants.KEYS.toListItem))
        viewModel.updateEstablishment(establishment.copy(
            date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            hour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm"))
        ))

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

    private fun toolbar()
    {
        toolbar = bind.toolbaItemHistory
        toolbar.title=getExtras()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun getExtras():String {
        val bundle = intent.extras
        val key = bundle?.getString("keyName")
        return key?:""
    }

    override fun delete(item: Item) {
       viewModel.deleteItem(item)
        attEstablishment()
        attView()
    }

    override fun update(item: Item) {
      popupEdit(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history,menu)
        return true
    }

   private fun attView()
    {
        viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.ins)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.toolbar_history_prec_desc -> viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.desc)

            R.id.toolbar_history_prec_cresc -> viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.asc)

            R.id.toolbar_history_ordem_alfabetica ->  viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.alf)

            R.id.toolbar_history_entrada ->  viewModel.getList(mShared.getFK(Constants.KEYS.toListItem),Constants.FILTER.ins)

            R.id.toolbar_history_add -> popupInsert()

            else -> return super.onOptionsItemSelected(item)

        }
        return false

    }


    private fun iconsEmptyList(isEmpty: List<Item>)
    {
        if(isEmpty.isEmpty())
        {
            bind.iconEmptyIdHistoryItem.visibility = View.VISIBLE
            bind.textEmptyIdHistoryItem.visibility = View.VISIBLE
            bind.textTotalId.visibility = View.INVISIBLE
        }else{
            bind.iconEmptyIdHistoryItem.visibility = View.INVISIBLE
            bind.textEmptyIdHistoryItem.visibility = View.INVISIBLE
            bind.textTotalId.visibility = View.VISIBLE
        }
    }

    override fun onItemListUpdated(list: List<Item>) {
       iconsEmptyList(list)
    }


}