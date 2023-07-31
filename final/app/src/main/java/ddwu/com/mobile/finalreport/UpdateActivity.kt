package ddwu.com.mobile.finalreport

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import ddwu.com.mobile.finalreport.DiaryDBHelper
import ddwu.com.mobile.finalreport.DiaryDto
import ddwu.com.mobile.finalreport.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    lateinit var updateBinding : ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        /*RecyclerView 에서 선택하여 전달한 dto 를 확인*/
        // 클릭 위치에 해당하는 dto
        val dto = intent.getSerializableExtra("dto") as DiaryDto

        /*전달받은 값으로 화면에 표시*/
        //id는 업데이트에서 안쓰니까 냅둠?
        updateBinding.etUpdateId.setText(dto.id.toString())
        updateBinding.etDate.setText(dto.date)
        updateBinding.etTitle.setText(dto.title)
        updateBinding.etContent.setText(dto.content)
        updateBinding.etWeather.setText(dto.weather)
        if (dto.photo == 1)
            updateBinding.ivPhoto.setImageResource(R.drawable.day01)
        if (dto.photo == 2)
            updateBinding.ivPhoto.setImageResource(R.drawable.day02)
        if (dto.photo == 3)
            updateBinding.ivPhoto.setImageResource(R.drawable.day03)
        if (dto.photo == 4)
            updateBinding.ivPhoto.setImageResource(R.drawable.day04)
        if (dto.photo == 5)
            updateBinding.ivPhoto.setImageResource(R.drawable.day05)
        if (dto.photo == 6)
            updateBinding.ivPhoto.setImageResource(R.drawable.day06)
        if (dto.photo == 7)
            updateBinding.ivPhoto.setImageResource(R.drawable.day07)


        updateBinding.btnUpdate.setOnClickListener{
            /*dto 는 아래와 같이 기존의 dto 를 재사용할 수도 있음*/
            dto.date = updateBinding.etDate.text.toString()
            dto.title = updateBinding.etTitle.text.toString()
            dto.content = updateBinding.etContent.text.toString()
            dto.weather = updateBinding.etWeather.text.toString()


            if (updateDays(dto) > 0) { // 바뀐 게 있다면 (수정됐다면)
                setResult(RESULT_OK)      // update 를 수행했으므로 RESULT_OK 를 반환
            } else {
                setResult(RESULT_CANCELED)
            }
            finish() // 메인으로 돌아감
        }

        updateBinding.btnCancel.setOnClickListener{
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    /*update 정보를 담고 있는 dto 를 전달 받아 dto 의 id 를 기준으로 수정*/
    fun updateDays(dto: DiaryDto): Int {
        val helper = DiaryDBHelper(this)
        val db = helper.writableDatabase
        val updateValue = ContentValues()
        updateValue.put(DiaryDBHelper.COL_DATE, dto.date)
        updateValue.put(DiaryDBHelper.COL_TITLE, dto.title)
        updateValue.put(DiaryDBHelper.COL_CONTENT, dto.content)
        updateValue.put(DiaryDBHelper.COL_WEATHER, dto.weather)

        val whereCaluse = "${BaseColumns._ID}=?" // == "_id=?"
        val whereArgs = arrayOf(dto.id.toString())

        /*upate 가 적용된 레코드의 개수 반환*/
        val result =  db.update(DiaryDBHelper.TABLE_NAME,
            updateValue, whereCaluse, whereArgs)

        helper.close()      // DB 작업 후에는 close()
        return result
    }

}