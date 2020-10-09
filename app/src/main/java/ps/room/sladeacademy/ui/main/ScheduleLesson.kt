package ps.room.sladeacademy.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_schedule_lesson.*
import ps.room.sladeacademy.R
import timber.log.Timber


class ScheduleLesson : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var currentUser : FirebaseUser? = null
    private var database = FirebaseDatabase.getInstance()
    var userRef = database.getReference("user")
    
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
        userRef.child(currentUser.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.hasChildren()){
                    Timber.d("Data has been updated%s", dataSnapshot)
                    teacher_profile_name.text = dataSnapshot.child("name").toString()
                    teacher_email.text = dataSnapshot.child("email").toString()
                    teacher_profession.text = dataSnapshot.child("subject").toString()
                }else{
                    Timber.d("No data found")
                }

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.w("Failed to read value.%s", error.toException())
            }
        })
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