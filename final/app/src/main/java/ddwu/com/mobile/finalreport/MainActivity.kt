// 과제명: 다이어리 앱
// 분반: 01 분반
// 학번: 20210788 성명: 박소정
// 제출일: 2023년 6월 23일
package ddwu.com.mobile.finalreport

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import ddwu.com.mobile.finalreport.DiaryDBHelper
import ddwu.com.mobile.finalreport.DiaryDto
import ddwu.com.mobile.finalreport.databinding.ActivityMainBinding
import ddwu.com.mobile.finalreport.databinding.DialogInterfaceBinding

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    val REQ_ADD = 100
    val REQ_UPDATE = 200


    lateinit var binding : ActivityMainBinding
    lateinit var adapter : DiaryAdapter
    lateinit var days : ArrayList<DiaryDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*RecyclerView 의 layoutManager 지정*/
        binding.recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        // 어댑터 연결 코드!!
        days = getAllDays()               // DB 에서 모든 food를 가져옴, food에 dto리스트가 들어감
        adapter = DiaryAdapter(days)        // adapter 에 데이터 설정
        binding.recyclerView.adapter = adapter   // RecylcerView 에 adapter 설정


        // 어댑터에 연결됨
        /*RecyclerView 항목 클릭 시 실행할 객체*/
        val onClickListener = object : DiaryAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                /*클릭 항목의 dto 를 intent에 저장 후 UpdateActivity 실행*/
                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("dto", days.get(position) ) // 몇번째 항목을 클릭했는지 알도록 인텐트에 보관
                startActivityForResult(intent, REQ_UPDATE)
            }
        }
        adapter.setOnItemClickListener(onClickListener)

        // 롱클릭 시 삭제
        /*RecyclerView 항목 롱클릭 시 실행할 객체*/
        val onLongClickListener = object: DiaryAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {

                val dialogBinding = DialogInterfaceBinding.inflate(layoutInflater)
                AlertDialog.Builder(binding.root.context)
                    .setTitle("${days.get(position).date}에 작성한 일기를 정말 삭제하시겠습니까?")
                    .setView(dialogBinding.root)
                    .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            if ( deleteDays(days.get(position).id) > 0) {
                                refreshList(RESULT_OK)
                                Toast.makeText(this@MainActivity, "삭제 완료", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show()
                /*롱클릭 항목의 dto 에서 id 확인 후 함수에 전달*/

            }
        }
        adapter.setOnItemLongClickListener(onLongClickListener)
    }


    // 옵션 메뉴 -- 수정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.devitem -> {
                val intent = Intent(this@MainActivity, DevActivity::class.java)  // 띄우고자 하는 액티비티의 클래스명을 명시
                startActivity(intent)
            }
            R.id.additem -> {
                // btnAdd를 클릭하면 AddFoodActivity 실행
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                startActivityForResult(intent, REQ_ADD)
            }
            R.id.exititem -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /*화면이 보일 때마다 화면을 갱신하고자 할 경우에는 onResume()에 갱신작업 추가*/
//    override fun onResume() {
//        super.onResume()
//        adapter.notifyDataSetChanged()
//    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_UPDATE -> {
                refreshList(resultCode)
            }
            REQ_ADD -> {
                refreshList(resultCode)
            }
        }
    }

    /*다른 액티비티에서 DB 변경 시 DB 내용을 읽어와 Adapter 의 list 에 반영하고 RecyclerView 갱신*/
    private fun refreshList(resultCode: Int) {
        if (resultCode == RESULT_OK) { // 변경 됐다면
            days.clear() // 어댑터에 넣어준 foods 속을 비움 내용물 없앰
            days.addAll(getAllDays()) // 몽땅 다시 읽어옴
            adapter.notifyDataSetChanged() // 리사이클러뷰 갱신
        } else {
            Toast.makeText(this@MainActivity, "취소됨", Toast.LENGTH_SHORT).show()
        }
    }


    /*전체 레코드를 가져와 dto 로 저장한 후 dto를 저장한 list 반환*/
    // DB에서 데이터 가져옴
    @SuppressLint("Range")
    fun getAllDays() : ArrayList<DiaryDto> {
        val helper = DiaryDBHelper(this)
        val db = helper.readableDatabase

        // 모든 데이터 몽땅 가져와라
//        val cursor = db.rawQuery("SELECT * FROM ${FoodDBHelper.TABLE_NAME}", null)
        val cursor = db.query(DiaryDBHelper.TABLE_NAME, null, null, null, null, null, null)
        // 커서는 레코드의 맨 앞을 가리키는 상태임

        val days = arrayListOf<DiaryDto>()

        with (cursor) {
            while (moveToNext()) {
                val id = getInt( getColumnIndex(BaseColumns._ID) )
                val date = getString ( getColumnIndex(DiaryDBHelper.COL_DATE) )
                val weather = getString ( getColumnIndex(DiaryDBHelper.COL_WEATHER) )
                val title = getString ( getColumnIndex(DiaryDBHelper.COL_TITLE) )
                val content = getString ( getColumnIndex(DiaryDBHelper.COL_CONTENT) )
                val photo = getInt ( getColumnIndex(DiaryDBHelper.COL_PHOTO) )

                val dto = DiaryDto(id, date, title, content, weather, photo)
                days.add(dto) // 레코드를 하나하나 디티오로 만들어서 리스트에 집어넣음
            }
        }

        cursor.close()      // cursor 사용을 종료했으므로 close()
        helper.close()      // DB 사용이 끝났으므로 close()

        return days // 리스트 리턴
    }


    /*ID 에 해당하는 레코드를 삭제 후 삭제된 레코드 개수 반환*/
    fun deleteDays(id: Int) : Int {
        val helper = DiaryDBHelper(this)
        val db = helper.writableDatabase

        val whereClause = "${BaseColumns._ID}=?"
        val whereArgs = arrayOf(id.toString())

        val result = db.delete(DiaryDBHelper.TABLE_NAME, whereClause, whereArgs)

        helper.close()
        return result
    }


}