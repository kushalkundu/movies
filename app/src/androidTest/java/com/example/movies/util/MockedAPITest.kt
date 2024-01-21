

package com.example.movies.util

import org.junit.Before
import org.junit.Rule

open class MockedAPITest {
    @get:Rule
    var mockWebServerRule = MockWebServerRule()

//    @get:Rule
//    var okHttpIdlingResourceRule = OkHttpIdlingResourceRule()

    @Before
    open fun setBaseUrl() {
        com.example.movies.network.URL.BASE_URL = mockWebServerRule.server.url("/").toString()
    }

//    @After
//    open fun updateBaseUrl() {
//        CoreSDK.getInstance().baseUrl = AndroidTestData.ENDPOINT
//    }

}