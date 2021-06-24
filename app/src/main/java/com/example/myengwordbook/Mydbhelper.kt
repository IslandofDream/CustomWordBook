package com.example.myengwordbook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.util.ArrayList

class Mydbhelper(val context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME = "mydatabase.db" //데베 이름
        val DB_VERSION = 1 // 버전정보
        val TABLE_NAME = "Word" //테이블 이름
        val ID = "id" // 테이블 컬럼들
        val WORD = "word"// 테이블 컬럼들
        val MEANING = "meaning"// 테이블 컬럼들
        val CHECKED = "checked"// 테이블 컬럼들
    }

    fun getCount() : Int{ // 테이블의 총 튜플수 반환 
        val strsql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        return cursor.count
    }
    
    
    fun getALLRecord() : ArrayList<MyWordData>{ // 모든 데이터 반환 단어장에 출력하기 위함
        var data: ArrayList<MyWordData> = ArrayList()
        val strsql = "select * from $TABLE_NAME order by LOWER($WORD) ASC;"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        cursor.moveToFirst()
        do {
            data.add(MyWordData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)))
        } while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
    }

    fun getBookMark() : ArrayList<MyWordData>{ // checked = 1 즉, 북마크가 적용된것만 반환
        var data: ArrayList<MyWordData> = ArrayList()
        val strsql = "select * from $TABLE_NAME where $CHECKED = 1 order by LOWER($WORD) ASC;"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        cursor.moveToFirst()
        if(cursor.count == 0){ // 처음에 북마크된 단어가 없는 경우
            data.add(MyWordData(0,"","즐겨찾기된 단어가 없습니다.",1))
            cursor.close()
            db.close()
            return data
        }
        else {
            cursor.moveToFirst()
            do {
                data.add(MyWordData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)))
            } while (cursor.moveToNext())
            cursor.close()
            db.close()
            return data
        }

    }

    fun findword(word: String): ArrayList<MyWordData>{ // 특정 단어 검색, 이때, like %을 통해서 유사 단어들 까지 모두 검색
        var data: ArrayList<MyWordData> = ArrayList()
        val strsql = "select * from $TABLE_NAME where  $WORD like '%$word%' or $MEANING like '%$word%' order by LOWER($WORD);"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        cursor.moveToFirst()
        if(cursor.count == 0){ // 처음에 북마크된 단어가 없는 경우
            data.add(MyWordData(0,"","검색결과가 없습니다.",1))
            cursor.close()
            db.close()
            return data
        }
        else{
        do {
            data.add(MyWordData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)))
        } while (cursor.moveToNext())
        cursor.close()
        db.close()
        return data
        }
    }


    fun findbookword(word: String): ArrayList<MyWordData>{ /// 즐겨찾기에서 검색용으로 쓰는거 cheked1 이외 달라진거 없음
        var data: ArrayList<MyWordData> = ArrayList()
        val strsql = "select * from $TABLE_NAME where $CHECKED = 1 and ($WORD like '%$word%' or $MEANING like '%$word%') order by LOWER($WORD);"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        cursor.moveToFirst()
        if(cursor.count == 0){ // 처음에 북마크된 단어가 없는 경우
            data.add(MyWordData(0,"","검색결과가 없습니다.",1))
            cursor.close()
            db.close()
            return data
        }
        else{
            do {
                data.add(MyWordData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)))
            } while (cursor.moveToNext())
            cursor.close()
            db.close()
            return data
        }
    }

    fun checkoverlap(word: String):Boolean{ // 중복 확인, 데이터 삽입인지 수정인지 판단
        val strsql = "select * from $TABLE_NAME where  $WORD ='$word';"
        val db = readableDatabase
        val cursor =db.rawQuery(strsql,null)
        cursor.moveToFirst()
        return cursor.count == 0
    }

    fun insertword(word:String, meaning: String) :Boolean{ // 단어 삽입
        val values = ContentValues()
        values.put(WORD, word)
        values.put(MEANING, meaning)
        values.put(CHECKED, 0)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null , values)>0
        db.close()
        return flag
    }

    fun upadate(word:String, meaning:String) :Boolean{ // 단어의 뜻을 업데이트 시킴
        val strsql = "update $TABLE_NAME set $MEANING = '$meaning' where $WORD = '$word';"
        val db = writableDatabase  //변경시킬꺼니까
        val cursor = db.rawQuery(strsql,null)

        if(cursor.count > 0){
            cursor.close()
            db.close()
            return true
        }
        else{
            cursor.close()
            db.close()
            return false
        }
    }

    fun updatebookmark(wordData: MyWordData):Boolean{ // 북마크 등록시키는 함수 wordfragment에서만 쓰임
        val id = wordData.id
        val strsql = "update $TABLE_NAME set $CHECKED = 1 where $ID = '$id';"
        val db = writableDatabase  //변경시킬꺼니까
        val cursor =db.rawQuery(strsql,null)
        val flag = cursor.count != 0
        if(flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(ID, wordData.id)
            values.put(WORD, wordData.word)
            values.put(MEANING, wordData.meaning)
            values.put(CHECKED, wordData.checked)
            db.update(TABLE_NAME, values, "$ID =?", arrayOf(id.toString()))
            Log.d("asdf","dkssusd")
        }
        else{
        }
        cursor.close()
        db.close()

        return true
    }

    fun uncheckedbookmark(wordData: MyWordData):Boolean{ // 북마크 해제 시키는 함수 mybookmark에서만 쓰임
        val id = wordData.id
        val strsql = "update $TABLE_NAME set $CHECKED = 0 where $ID = '$id';"
        val db = writableDatabase  //변경시킬꺼니까
        val cursor =db.rawQuery(strsql,null)
        val flag = cursor.count != 0
        if(flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(ID, wordData.id)
            values.put(WORD, wordData.word)
            values.put(MEANING, wordData.meaning)
            values.put(CHECKED, wordData.checked)
            db.update(TABLE_NAME, values, "$ID =?", arrayOf(id.toString()))
        }
        cursor.close()
        db.close()
        return true
    }

    fun deletword (word : String): Boolean{ // 단어 삭제 함수
        val strsql = "select * from $TABLE_NAME where $WORD = '$word';"
        val db = writableDatabase  //삭제할꺼니까
        val cursor =db.rawQuery(strsql,null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME, "$WORD =?", arrayOf(word))
        }
        cursor.close()
        db.close()
        return flag
    }


    override fun onCreate(db: SQLiteDatabase?) { // 데이터베이스 처음 생성될때
        val create_table = "create table if not exists $TABLE_NAME("+
                "$ID integer primary key autoincrement, "+
                "$WORD text, "+
                "$MEANING text, "+
                "$CHECKED integer);"

        db!!.execSQL(create_table)
        // 테이블이 존재하지 않으면 생성 할거다
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { // 데이터베이스 버전 바뀔때
        val drop_tabe = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_tabe)
        onCreate(db)
    }



}