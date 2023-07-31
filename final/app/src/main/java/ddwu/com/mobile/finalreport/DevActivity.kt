package ddwu.com.mobile.finalreport

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ddwu.com.mobile.finalreport.databinding.ActivityAddBinding
import ddwu.com.mobile.finalreport.databinding.ActivityDevBinding


class DevActivity : AppCompatActivity() {

    lateinit var devBinding : ActivityDevBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        devBinding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(devBinding.root)
        devBinding.imageView2.setImageResource(R.drawable.me)

    }
}