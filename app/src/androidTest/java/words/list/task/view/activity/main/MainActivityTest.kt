package words.list.task.view.activity.main

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import words.list.task.R
import words.list.task.TestUtil

@LargeTest
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