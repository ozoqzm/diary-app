package ddwu.com.mobile.finalreport

import java.io.Serializable

data class DiaryDto (var id : Int, var date : String, var title : String,
                var content : String, var weather : String, var photo : Int)
    : Serializable {
}
