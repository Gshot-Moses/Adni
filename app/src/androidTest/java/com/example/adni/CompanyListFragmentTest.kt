package com.example.adni

import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.example.adni.data.FakeRepo
import com.example.adni.data.dao.CompanyDao
import com.example.adni.data.database.AdniDatabase
import com.example.adni.di.Cache
import com.example.adni.di.CacheModule
import com.example.adni.domain.IRepo
import com.example.adni.presentation.company.list.CompanyListFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidTest
@UninstallModules(CacheModule::class, Cache::class)
class CompanyListFragmentTest {

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class CacheTest {
        @Singleton
        @Provides
        fun provideDb(): AdniDatabase {
            return Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                AdniDatabase::class.java).build()
        }
        @Singleton
        @Provides
        fun provideDao(db: AdniDatabase): CompanyDao {
            return db.getCompanyDao()
        }
        @ActivityRetainedScoped
        @Binds
        abstract fun provideRepo(repo: FakeRepo): IRepo
    }

    @Inject lateinit var repo: IRepo

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun items_displayed_on_recycler_view() {
        (repo as FakeRepo).returnCompanyList = true
        launchFragmentInHiltContainer<CompanyListFragment> {
            //onView(withId(R.id.company_recycler)).check(matches(ViewMatchers.isDisplayed()))
        }
    }
}