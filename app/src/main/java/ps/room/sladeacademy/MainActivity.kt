package ps.room.sladeacademy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login_layout.view.*
import kotlinx.android.synthetic.main.registration_layout.view.*
import ps.room.sladeacademy.ui.main.ScheduleLesson
import ps.room.sladeacademy.ui.main.SectionsPagerAdapter
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var database = FirebaseDatabase.getInstance()
    var userRef = database.getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("in the onCreate of the activity")
        //firebase authentication
        mAuth = FirebaseAuth.getInstance()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart of the activity")
        // Getting current user. if null make the send to schedule menu item disappear
        currentUser = mAuth?.currentUser
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

    private fun sendToScheduleActivity() {
        if (currentUser != null) {
            Timber.d("sending to schedule activity")
            // send to schedule activity.
            val scheduleActivityIntent = Intent(applicationContext, ScheduleLesson::class.java)
            startActivity(scheduleActivityIntent)
        } else {
            Toast.makeText(applicationContext, "You need to log in first!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun loginDialog() {
        Timber.d("authenticating user")
        // login button
        val dialogBuilder = AlertDialog.Builder(applicationContext).create()
        val dialogView =
            LayoutInflater.from(applicationContext).inflate(R.layout.login_layout, null)

        val emailInput = dialogView.email_input
        val passwordInput = dialogView.password_input
        val loginBtn = dialogView.login_btn

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

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
        val register_here = dialogView.register_txt
        register_here.setOnClickListener(View.OnClickListener {
            dialogBuilder.dismiss() // Dismiss the dialog then show the register dialog
            registerDialog()

        })

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();

    }

    private fun registerDialog() {
        Timber.d("Register user")
        // login button
        val regDialogBuilder = AlertDialog.Builder(applicationContext).create()
        val dialogView =
            LayoutInflater.from(applicationContext).inflate(R.layout.registration_layout, null)

        val regEmailInput = dialogView.register_email
        val regNameInput = dialogView.register_name
        val regSubjectInput = dialogView.register_subject
        val regPasswordInput1 = dialogView.password_input_1
        val regPasswordInput2 = dialogView.password_input_2
        val registerBtn = dialogView.register_btn

        registerBtn.setOnClickListener {
            val regEmail = regEmailInput.text.toString().trim()
            val regName = regNameInput.text.toString().trim()
            val regSubject = regSubjectInput.text.toString().trim()
            val regPassword1 = regPasswordInput1.text.toString().trim()
            val regPassword2 = regPasswordInput2.text.toString().trim()

            when {
                TextUtils.isEmpty(regEmail) -> {
                    regEmailInput.error = "Required!";
                }
                TextUtils.isEmpty(regName) -> {
                    regNameInput.error = "Required!";
                }
                TextUtils.isEmpty(regSubject) -> {
                    regSubjectInput.error = "Required!";
                }
                TextUtils.isEmpty(regPassword1) -> {
                    regPasswordInput1.error = "Required!";
                }
                TextUtils.isEmpty(regPassword2) -> {
                    regPasswordInput2.error = "Required!";
                }
                regPassword1 != regPassword2 -> {
                    regPasswordInput2.error = "Passwords must be matching!";
                }
                else -> {
                    signUpWithEmailAndPassword(regEmail, regPassword2, regName, regSubject)
                }
            }
        }
        val loginHere = dialogView.login_txt
        loginHere.setOnClickListener(View.OnClickListener {
            regDialogBuilder.dismiss() // Dismiss the dialog then show the Login dialog
            loginDialog()

        })

        regDialogBuilder.setView(dialogView)
        regDialogBuilder.setCancelable(true)
        regDialogBuilder.show()
    }

    private fun signUpWithEmailAndPassword(
        regEmail: String,
        regPassword2: String,
        regName: String,
        regSubject: String
    ) {
        mAuth!!.createUserWithEmailAndPassword(regEmail, regPassword2)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success")
                    currentUser = mAuth!!.currentUser
                    sendToRealtimeDatabase(regEmail, regName, regSubject)
                    Toast.makeText(
                        applicationContext,
                        "Registration successful.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("createUserWithEmail:failure%s", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }

            }

    }

    private fun sendToRealtimeDatabase(regEmail: String, regName: String, regSubject: String) {
        Timber.d("storing data to firebase realtime")
        var userObj = HashMap<String, String>().apply {
            put("email",regEmail)
            put("name",regName)
            put("subject",regSubject)
        }
        userRef.child(currentUser.toString()).setValue(userObj).addOnCompleteListener(this,
            OnCompleteListener {
                if (it.isSuccessful) {
                    Timber.i("Data has been stored successfully")
                    Toast.makeText(applicationContext, "User has been stored successfully", Toast.LENGTH_SHORT).show()
                    //Todo update the menu item from log in to logout
                } else {
                    Timber.i("An error occurred while registering user%s", it.exception)
                    Toast.makeText(applicationContext, "An error occurred while registering user", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this,
            OnCompleteListener {
                if (it.isSuccessful) {
                    // sign is is successful
                    Timber.d("signInWithEmail successfully")
                    currentUser = mAuth?.currentUser
                    Toast.makeText(applicationContext, "signed in successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.d("signInWithEmail failed%s", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            })


    }

}


