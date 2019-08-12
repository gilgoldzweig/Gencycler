package goldzweigapps.com.sample.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import goldzweigapps.com.sample.R
import goldzweigapps.com.sample.activities.adapters.RestaurantAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
