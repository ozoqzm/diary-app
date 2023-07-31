package ddwu.com.mobile.finalreport

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.finalreport.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    //val TAG = "AddFoodActivity"
    lateinit var binding : ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivPhoto.setImageResource(R.drawable.day07)

        binding.btnSave.setOnClickListener {
            val date = binding.etDate.text.toString()
            val weather = binding.etWeather.text.toString()
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            val newDto = DiaryDto(0, date, title, content, weather, 7)      // 화면 값으로 dto 생성, id 는 임의의 값 0

            if ( addDays(newDto) > 0) {
                setResult(RESULT_OK)
            } else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

        binding.btnCancel.setOnClickListener{
            setResult(AppCompatActivity.RESULT_CANCELED)
            finish()
        }
    }

    /*추가할 정보를 담고 있는 dto 를 전달받아 DB에 추가, id 는 autoincrement 이므로 제외
   * DB추가 시 추가한 항목의 ID 값 반환, 추가 실패 시 -1 반환 */
    fun addDays(newDto : DiaryDto) : Long  {
        val helper = DiaryDBHelper(this)
        val db = helper.writableDatabase

        val newValues = ContentValues()
        newValues.put(DiaryDBHelper.COL_DATE, newDto.date)
        newValues.put(DiaryDBHelper.COL_TITLE, newDto.title)
        newValues.put(DiaryDBHelper.COL_CONTENT, newDto.content)
        newValues.put(DiaryDBHelper.COL_WEATHER, newDto.weather)
        newValues.put(DiaryDBHelper.COL_PHOTO, newDto.photo)

        /*insert 한 항목의 id 를 반환*/
        val result = db.insert(DiaryDBHelper.TABLE_NAME, null, newValues)

        helper.close()      // DB 작업 후 close() 수행

        return result
    }


}