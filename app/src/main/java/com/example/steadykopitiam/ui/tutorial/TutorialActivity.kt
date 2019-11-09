package com.example.steadykopitiam.ui.tutorial

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.steadykopitiam.LoginActivity
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage


class TutorialActivity : AppIntro() {
    lateinit var sharedPreferencesTut : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val PACKAGE_NAME: String = getApplicationContext().getPackageName();
        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        val sliderPage1 = SliderPage()
        sliderPage1.title = "Steady your diet"
        sliderPage1.description = "Track your diet with Steady Kopitiam"
        var checklist = application.resources.getIdentifier("$PACKAGE_NAME:drawable/checklist",null,null)
        sliderPage1.imageDrawable = checklist
        sliderPage1.bgColor = Color.parseColor("#0963A3")
        addSlide(AppIntroFragment.newInstance(sliderPage1))

        val sliderPage5 = SliderPage()
        sliderPage5.title = "Picks nutritious meal for you"
        sliderPage5.description = "Steady Picks recommends food with nutrient you need"
        var recommended = application.resources.getIdentifier("$PACKAGE_NAME:drawable/recommended",null,null)
        sliderPage5.imageDrawable = recommended
        sliderPage5.bgColor = Color.parseColor("#4F3B3B")
        addSlide(AppIntroFragment.newInstance(sliderPage5))

        val sliderPage2 = SliderPage()
        sliderPage2.title = "Best value Steady Picks"
        sliderPage2.description = "Enjoy all our food recommendation at a discounted price!"
        var piggybank = application.resources.getIdentifier("$PACKAGE_NAME:drawable/piggybank",null,null)
        sliderPage2.imageDrawable = piggybank
        sliderPage2.bgColor = Color.parseColor("#640077")
        addSlide(AppIntroFragment.newInstance(sliderPage2))

        val sliderPage3 = SliderPage()
        sliderPage3.title = "Buy more, rebate more"
        sliderPage3.description = "Get 10% rebate as Steady Coins by ordering with us"
        var digitalwallet = application.resources.getIdentifier("$PACKAGE_NAME:drawable/digitalwallet",null,null)
        sliderPage3.imageDrawable = digitalwallet
        sliderPage3.bgColor = Color.parseColor("#d0672e")
        addSlide(AppIntroFragment.newInstance(sliderPage3))

        val sliderPage4 = SliderPage()
        sliderPage4.title = "Welcome to Steady Kopitiam"
        sliderPage4.description = "Start-off your healthy diet with us!"
        var dish = application.resources.getIdentifier("$PACKAGE_NAME:drawable/dish",null,null)
        sliderPage4.imageDrawable = dish
        sliderPage4.bgColor = Color.parseColor("#207a3c")
        addSlide(AppIntroFragment.newInstance(sliderPage4))

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
//        val sliderPage = SliderPage()
//        sliderPage.title = title
//        sliderPage.description = "Lalalala"
//        sliderPage.imageDrawable = 1
//        sliderPage.bgColor = Color.BLUE
//        addSlide(AppIntroFragment.newInstance(sliderPage))

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#009688"))
        setSeparatorColor(Color.parseColor("#39AF8E"))

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)

//        preferenceHelper!!.putIntro("no")
        sharedPreferencesTut = getSharedPreferences("tutPref", Context.MODE_PRIVATE)
        val editor = sharedPreferencesTut.edit()
        editor.putBoolean("tutPref", true)
        editor.apply()

        val intent = Intent(this@TutorialActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

//        preferenceHelper!!.putIntro("no")
        sharedPreferencesTut = getSharedPreferences("tutPref", Context.MODE_PRIVATE)
        val editor = sharedPreferencesTut.edit()
        editor.putBoolean("tutPref", true)
        editor.apply()

        val intent = Intent(this@TutorialActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}
