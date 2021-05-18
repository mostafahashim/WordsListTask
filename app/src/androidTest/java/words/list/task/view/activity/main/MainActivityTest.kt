package words.list.task.view.activity.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import words.list.task.R
import words.list.task.TestUtil

@RunWith(AndroidJUnit4::class)
class MainActivityTest{
    @get:Rule
    var mActivityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    @Before
    fun setup() {
    }

    @Test
    fun testMainViewsSuccess() {
        mActivityTestRule.launchActivity(null)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testScroll() {
        TestUtil.wordModels = TestUtil.initData()
        mActivityTestRule.launchActivity(null)
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                    ViewActions.click()
                ))
    }

    @After
    fun shutDown() {
    }
}