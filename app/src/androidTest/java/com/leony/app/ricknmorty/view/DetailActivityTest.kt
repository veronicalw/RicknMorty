package com.leony.app.ricknmorty.view

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.data.model.GetCharacterDetailOriginLocation
import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailActivityTest {
    @get:Rule
    var detailActivityRule = ActivityScenarioRule(DetailActivity::class.java)
    val data = GetCharacterDetailResponse(
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "Unknown",
        "Male",
        GetCharacterDetailOriginLocation("Earth replacement dimension"),
        GetCharacterDetailOriginLocation("Earth"),
        "https://cdn.pixabay.com/photo/2021/06/17/22/55/rick-and-morty-6344804_1280.jpg",
        episodes = ArrayList()
    )

    /**
     * Is Attribute comes to the view
     */
    @Test
    fun isAttributeDisplaying(){
        Espresso.onView(withId(R.id.charName)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.charGender)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.charStatus)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.detailSpeciesValue)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.detailLocationValue)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        Espresso.onView(withId(R.id.detailOriginValue)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * Is Attribute comes to the view
     */
    @Test
    fun displayDataToView(){
        Espresso.onView(withId(R.id.charName)).perform(data.name?.let { setTextInTextView(it) })
        Espresso.onView(withId(R.id.charGender)).perform(data.gender?.let { setTextInTextView(it) })
        Espresso.onView(withId(R.id.charStatus)).perform(data.status?.let { setTextInTextView(it) })
        Espresso.onView(withId(R.id.detailSpeciesValue)).perform(data.species?.let { setTextInTextView(it) })
        Espresso.onView(withId(R.id.detailOriginValue)).perform(data.origin?.name.let { setTextInTextView(it.toString()) })
        Espresso.onView(withId(R.id.detailLocationValue)).perform(ScrollToAction(),data.location?.name.let { setTextInTextView(it.toString()) })

        Espresso.onView(withId(R.id.charName)).check(matches(withText(data.name)))
        Espresso.onView(withId(R.id.charGender)).check(matches(withText(data.gender)))
        Espresso.onView(withId(R.id.charStatus)).check(matches(withText(data.status)))
        Espresso.onView(withId(R.id.detailSpeciesValue)).check(matches(withText(data.species)))
        Espresso.onView(withId(R.id.detailOriginValue)).check(matches(withText(data.origin?.name
            ?: "")))
        Espresso.onView(withId(R.id.detailLocationValue)).check(matches(withText(data.location?.name
            ?: "")))
    }

    private fun setTextInTextView(value: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.allOf(isDisplayed(), isAssignableFrom(TextView::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextView).text = value
            }

            override fun getDescription(): String {
                return value
            }
        }
    }
}