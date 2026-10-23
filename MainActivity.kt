package ye.abughazi.network

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val navy = Color.rgb(18, 42, 66)
    private val teal = Color.rgb(0, 137, 123)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scroll = ScrollView(this)
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(Color.rgb(245, 247, 250))
        }
        scroll.addView(page)
        page.addView(TextView(this).apply {
            text = "شبكة أبو غازي"
            textSize = 28f
            setTextColor(navy)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 8)
        })
        page.addView(TextView(this).apply {
            text = "لوحة إدارة الشبكة"
            textSize = 16f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 22)
        })
        addCard(page, "حالة الإنترنت", "لم يتم الاتصال بالمايكروتك بعد")
        addCard(page, "المشتركون", "إدارة المشتركين بعد ربط قاعدة البيانات والراوتر")
        addCard(page, "كروت الهوتسبوت", "عرض وإنشاء الكروت — غير مفعّل في النسخة الأولية")
        addCard(page, "الموزعون والأرصدة", "تسجيل الموزعين ومتابعة أرصدتهم")
        addCard(page, "الحسابات", "المبيعات والمصروفات والأرباح")
        addCard(page, "اتصالا الإنترنت", "مراقبة الاتصالين بعد إعداد الربط")
        addCard(page, "إعدادات المايكروتك", "إعداد الاتصال سيضاف لاحقًا بشكل آمن")
        page.addView(TextView(this).apply {
            text = "نسخة أولية تجريبية — لا تتصل بالراوتر حاليًا"
            textSize = 13f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 10)
        })
        setContentView(scroll)
    }

    private fun addCard(parent: LinearLayout, title: String, description: String) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)
            setBackgroundColor(Color.WHITE)
        }
        card.addView(TextView(this).apply {
            text = title
            textSize = 19f
            setTextColor(teal)
        })
        card.addView(TextView(this).apply {
            text = description
            textSize = 14f
            setTextColor(Color.DKGRAY)
            setPadding(0, 7, 0, 0)
        })
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)
        parent.addView(card, params)
    }
}
