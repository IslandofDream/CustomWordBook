package com.example.myengwordbook


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myengwordbook.databinding.WordfragmentItemListBinding

class MyBookmark: Fragment() {
    lateinit var binding: WordfragmentItemListBinding
    lateinit var mydbhelper: Mydbhelper
    lateinit var data: ArrayList<MyWordData>
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = WordfragmentItemListBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.wordfragment_item_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager  =LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        mydbhelper = Mydbhelper(context)
        data = mydbhelper.getBookMark()
        recyclerView.adapter = MyItemRecyclerViewAdapter(data)
        (recyclerView.adapter as MyItemRecyclerViewAdapter).notifyDataSetChanged()


        val simpleCallBack = object : ItemTouchHelper.SimpleCallback( //스와이프 사용을 위함
                ItemTouchHelper.DOWN or ItemTouchHelper.UP
                , ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder= AlertDialog.Builder(context) // 단어 삭제전 dialog로 확인 절차
                builder.setTitle("단어 삭제")
                builder.setMessage("단어를 삭제하시겠습니까?")
                builder.setPositiveButton("예") { dialog, id ->
                    mydbhelper.deletword(data[viewHolder.adapterPosition].word)
                    (recyclerView.adapter as MyItemRecyclerViewAdapter).removeItem(viewHolder.adapterPosition) // 리싸이클러뷰 갱신
                    Toast.makeText(context,"단어 삭제 성공", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("아니오") { dialog, id -> dialog.dismiss()
                    Toast.makeText(context,"단어 삭제가 취소 되었습니다.", Toast.LENGTH_SHORT).show()
                    recyclerView.adapter = MyItemRecyclerViewAdapter(mydbhelper.getBookMark()) // 스와이프 사용 취소가 되었으므로 목록을 복구 시켜놔야 한다.
                    (recyclerView.adapter as MyItemRecyclerViewAdapter).refresh()
                }
                builder.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val findbtn = view.findViewById<Button>(R.id.findbtn)
        val totalbtn = view.findViewById<Button>(R.id.totalbtn)
        val findedit = view.findViewById<EditText>(R.id.findedit)
        findbtn.setOnClickListener {
            val find = findedit.text.toString().trim() // 공백제거
            if(find.isNotEmpty()) {
                recyclerView.adapter = MyItemRecyclerViewAdapter(mydbhelper.findbookword(find))
                (recyclerView.adapter as MyItemRecyclerViewAdapter).notifyDataSetChanged()
                (recyclerView.adapter as MyItemRecyclerViewAdapter).refresh()
                findedit.text.clear()
            }
            else{
                Toast.makeText(context,"공백을 제외한 검색어를 입력해주세요.",Toast.LENGTH_SHORT).show()
                findedit.text.clear()
            }
        }

        totalbtn.setOnClickListener { //
            recyclerView.adapter = MyItemRecyclerViewAdapter(mydbhelper.getBookMark())//단어 목록을 새로 받아서 리싸이클러뷰에 달아준다.
            (recyclerView.adapter as MyItemRecyclerViewAdapter).notifyDataSetChanged()
            (recyclerView.adapter as MyItemRecyclerViewAdapter).refresh()
            findedit.text.clear()
        }

        return view
    }




}
