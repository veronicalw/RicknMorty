package com.leony.app.ricknmorty.view

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.adapter.GetCharacterListAdapter
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import com.leony.app.ricknmorty.data.model.GetCharacterResults
import com.leony.app.ricknmorty.data.model.GetPagesInfo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @get:Rule
    var mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    private val characterItemOnTest = 1
    private val characterItemListOnTest = GetCharacterListResponse(
        GetPagesInfo("https://rickandmortyapi.com/api/character?page=2", prev=null),
        arrayListOf(
            GetCharacterResults(1, "Rick Sanchez", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
            GetCharacterResults(2, "Morty Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
            GetCharacterResults(3, "Summer Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
            GetCharacterResults(4, "Beth Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
            GetCharacterResults(5, "Jerry Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
        )
    )
    private val listItem = characterItemListOnTest.results[characterItemOnTest]

    /**
     * Is RecyclerView comes into the view
     */
    @Test
    fun isRecyclerViewDisplaying(){
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.mainDataRecyclerView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    /**
     * Select one item in RecyclerView then navigate to the DetailActivity
     */
    @Test
    fun testSelectCharacterItemFromListAndNavigateToDetailActivity(){
        /**
         * Giving delay if the intended character is not displayed on the UI (E.g: in position 8).
         * But in this case I would use the item on position 1
         */
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.mainDataRecyclerView)).perform(
            actionOnItemAtPosition<GetCharacterListAdapter.GetCharacterListViewHolder>(characterItemOnTest, click()))
        Espresso.onView(withId(R.id.charName)).check(matches(withText(listItem.name)))
    }

    /**
     * After DetailActivity is showing then press backButton
     */
    @Test
    fun testNavigateBackToMainActivity(){
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.mainDataRecyclerView)).perform(actionOnItemAtPosition<GetCharacterListAdapter.GetCharacterListViewHolder>(characterItemOnTest, click()))
        Espresso.onView(withId(R.id.charName)).check(matches(withText(listItem.name)))
        Espresso.onView(withId(R.id.backButton)).perform(click())
        Espresso.onView(withId(R.id.mainDataRecyclerView)).check(matches(isDisplayed()))
    }

}