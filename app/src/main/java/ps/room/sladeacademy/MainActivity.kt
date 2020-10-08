package ps.room.sladeacademy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.login_layout.view.*
import ps.room.sladeacademy.ui.main.ScheduleLesson
import ps.room.sladeacademy.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.log -> {
                // Display an alert dialog for login
                loginDialog()
            }
            R.id.schedule_lesson -> {
                // send to schedule activity
                sendToScheduleActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loginDialog() {
        // login button

        val dialogBuilder = AlertDialog.Builder(applicationContext).create()
        val dialogView =
            LayoutInflater.from(applicationContext).inflate(R.layout.login_layout, null)

        val emailInput = dialogView.email_input
        val passwordInput = dialogView.password_input
        val loginBtn = dialogView.login_btn

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim();
            val password = passwordInput.text.toString().trim();

            when {
                TextUtils.isEmpty(email) -> {
                    emailInput.error = "Required!";
                }
                TextUtils.isEmpty(password) -> {
                    passwordInput.error = "Required!";
                }
                else -> {
                    signInWithEmailAndPassword(email, password);
                }
            }
        }

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();

    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
//Todo
    }

}

private fun sendToScheduleActivity() {
    val scheduleActivityIntent = Intent(this, ScheduleLesson::class.java)
    startActivity(this, scheduleActivityIntent)
}
}