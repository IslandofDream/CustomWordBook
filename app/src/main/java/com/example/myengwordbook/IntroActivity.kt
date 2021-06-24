package com.example.myengwordbook

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.myengwordbook.databinding.ActivityIntroBinding
import java.io.FileOutputStream

class IntroActivity : AppCompatActivity() {

    lateinit var binding: ActivityIntroBinding
    var mBackWait:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initdb() // 먼저 데이터베이스 읽기
        init()
    }

    private fun init() {
        //각 프래그먼트들의 전환과
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,WordFragment(),null)
                .addToBackStack(null)
                .commit()
        binding.title.text = "단어장"

        binding.button1.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,WordFragment(),null)
                    .commit()
            binding.title.text = "단어장"

        }

        binding.button2.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,MyBookmark(),null)
                    .commit()
            binding.title.text = "즐겨찾기"
        }

        binding.button3.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,Quizfragment(),null)
                    .commit()
            binding.title.text = "퀴즈"
        }

        binding.button4.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,DicParsing(),null)
                .commit()
            binding.title.text = "검색 및 저장"
        }
    }


    private fun initdb() {
        val dbfile = getDatabasePath("mydatabase.db") // 데이터베이스 있는지 알아본다.
        if (dbfile.parentFile.exists()) { // 존재한다면 디렉토리 만들어준다.
            dbfile.parentFile.mkdir()
        }
        if(!dbfile.exists()){ // 존재하지 않으면 새롭게 파일을 만들어준다.
            val file = resources.openRawResource(R.raw.mydatabase)
            val fileSize = file.available()
            val buffer = ByteArray(fileSize)
            file.read(buffer)
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()
        }

    }


    override fun onBackPressed() { 
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this,"'뒤로' 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show()
        } else {
            finish() //액티비티 종료
        }
    }



}