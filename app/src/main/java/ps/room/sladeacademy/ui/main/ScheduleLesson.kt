package ps.room.sladeacademy.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_schedule_lesson.*
import ps.room.sladeacademy.R
import timber.log.Timber


class ScheduleLesson : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var currentUser : FirebaseUser? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_lesson)

        Timber.d("On creation of the activity")
        // initialize the auth
        mAuth = FirebaseAuth.getInstance()
        
        val toolbar = schedule_toolbar
        toolbar.title = "Schedule Lesson"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onStart() {
        super.onStart()
        Timber.d("onStart of the activity")
        // Getting current user. if null make the send to schedule menu item disappear
        currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}