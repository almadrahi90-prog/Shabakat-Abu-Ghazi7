package ye.abughazi.network

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread

class MainActivity : Activity() {

    private lateinit var ipInput: EditText
    private lateinit var userInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var portInput: EditText
    private lateinit var statusText: TextView

    private val prefs by lazy {
        getSharedPreferences("router_settings", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scroll = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 24, 28, 24)
            setBackgroundColor(Color.rgb(245, 248, 252))
            layoutDirection = android.view.View.LAYOUT_DIRECTION_RTL
        }

        scroll.addView(layout)
        setContentView(scroll)

        fun addText(text: String, size: Float, color: Int = Color.DKGRAY) {
            layout.addView(TextView(this).apply {
                this.text = text
                textSize = size
                setTextColor(color)
                gravity = Gravity.RIGHT
                setPadding(4, 12, 4, 12)
            })
        }

        fun addInput(hint: String, value: String = ""): EditText {
            return EditText(this).apply {
                this.hint = hint
                setText(value)
                textSize = 16f
                setSingleLine(true)
                layout.addView(
                    this,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }

        fun addButton(label: String, action: () -> Unit) {
            layout.addView(Button(this).apply {
                text = label
                setOnClickListener { action() }
            })
        }

        addText("شبكة أبو غازي", 28f, Color.rgb(15, 90, 65))
        addText("إعداد الاتصال بالمايكروتك", 20f)

        addText("عنوان IP للراوتر", 15f)
        ipInput = addInput("مثال: 192.168.88.1",
            prefs.getString("ip", "") ?: "")

        addText("اسم المستخدم", 15f)
        userInput = addInput("اسم مستخدم الراوتر",
            prefs.getString("username", "") ?: "")

        addText("كلمة المرور", 15f)
        passwordInput = addInput("كلمة مرور الراوتر",
            prefs.getString("password", "") ?: "")
        passwordInput.inputType =
            android.text.InputType.TYPE_CLASS_TEXT or
            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        addText("منفذ API", 15f)
        portInput = addInput("8728",
            prefs.getString("port", "8728") ?: "8728")
        portInput.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        addButton("حفظ الإعدادات") {
            prefs.edit()
                .putString("ip", ipInput.text.toString().trim())
                .putString("username", userInput.text.toString().trim())
                .putString("password", passwordInput.text.toString())
                .putString("port", portInput.text.toString().trim())
                .apply()

            Toast.makeText(this, "تم حفظ الإعدادات على الهاتف", Toast.LENGTH_SHORT).show()
        }

        addButton("اختبار الوصول إلى الراوتر") {
            val ip = ipInput.text.toString().trim()
            val port = portInput.text.toString().trim().toIntOrNull()

            if (ip.isBlank() || port == null || port !in 1..65535) {
                statusText.text = "تحقق من عنوان IP والمنفذ"
                return@addButton
            }

            statusText.text = "جارٍ اختبار الوصول..."
            thread {
                val reachable = try {
                    Socket().use {
                        it.connect(InetSocketAddress(ip, port), 3000)
                    }
                    true
                } catch (_: Exception) {
                    false
                }

                runOnUiThread {
                    statusText.text = if (reachable) {
                        "يمكن الوصول إلى المنفذ. لم يتم التحقق من تسجيل الدخول بعد."
                    } else {
                        "تعذر الوصول إلى المنفذ. تحقق من الشبكة وإعدادات الراوتر."
                    }
                }
            }
        }

        statusText = TextView(this).apply {
            text = "لم يتم اختبار الاتصال بعد"
            textSize = 16f
            setTextColor(Color.rgb(40, 70, 90))
            gravity = Gravity.RIGHT
            setPadding(4, 18, 4, 18)
        }
        layout.addView(statusText)

        addText("ملاحظة: يلزم تفعيل API على الراوتر والسماح بالوصول من جهازك.", 13f)
    }
}
