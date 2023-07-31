package ddwu.com.mobile.finalreport

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DiaryDBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1){

    val TAG = "DiaryDBHelper"

    companion object {
        const val DB_NAME = "days_db" // 파일 이름
        const val TABLE_NAME = "days_table" // 테이블 이름

        const val COL_DATE = "date"
        const val COL_WEATHER = "weather"
        const val COL_TITLE = "title"
        const val COL_CONTENT = "content"
        const val COL_PHOTO = "photo"
    }

    // SQL에서는-> create 테이블명 (컬럼명 컬럼자료형, [컬럼명 컬럼자료형 ...] )
    // 공통 정보 id로 바꾸기...
    override fun onCreate(db: SQLiteDatabase?) { //
        val CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME} ( ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COL_DATE} TEXT, "+
                    " ${COL_TITLE} TEXT, ${COL_CONTENT} TEXT, ${COL_WEATHER} TEXT, ${COL_PHOTO} INTEGER)"
        Log.d(TAG, CREATE_TABLE)
        Log.d(TAG, BaseColumns._ID) // 추가
        db?.execSQL(CREATE_TABLE)

        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/1', '6월의 시작', '내용','맑음', 1)")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/2', '오늘의 일기', '열심히 살아보자', '흐림', 2)")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/3', '세번 째 일기!', '야호', '비옴', 3)")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/4', '오늘 하루 기록', '오늘은 소풍가는 날', '맑음',  4)")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/5', '목표 일기', '여행가고 싶다', '화창', 5)")
        db?.execSQL("INSERT INTO $TABLE_NAME VALUES (NULL, '6/6', '오늘의 일기', '현충일', '맑음', 6)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

}