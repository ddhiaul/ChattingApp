package com.aulia.idn.chattingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth
    private lateinit var refUsers : DatabaseReference
    private var firebaseUserID : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar : Toolbar = findViewById(R.id.toolbar_reg)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.register)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()
        btn_reg.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username : String = et_user_name_reg.text.toString()
        val email : String = et_email_reg.text.toString()
        val password : String = et_password_reg.text.toString()

        if(username == ""){
            Toast.makeText(this, getString(R.string.text_message_username),
                Toast.LENGTH_LONG).show()
        }else if(email == ""){
            Toast.makeText(this, getString(R.string.text_message_email),
                Toast.LENGTH_LONG).show()
        }else if (password == ""){
            Toast.makeText(this, getString(R.string.text_message_password),
                Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance()
                        .reference.child(getString(
                        R.string.text_users)).child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/chattingapp-b924c.appspot.com/o/profile.png?alt=media&token=28fef12c-55b7-4703-942d-4502831f448f"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/chattingapp-b924c.appspot.com/o/cover.jpeg?alt=media&token=92b44898-b269-4546-92da-5931bb488bc0"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap).addOnCompleteListener {
                        task ->
                        if(task.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this, getString(R.string.text_error_message)+
                            task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}