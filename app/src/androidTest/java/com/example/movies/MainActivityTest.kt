package com.example.movies

import androidx.test.InstrumentationRegistry
import com.example.movies.util.MockedAPITest
import com.example.movies.util.AssetReaderUtil
import okhttp3.mockwebserver.MockResponse

class MainActivityTest: MockedAPITest() {

    private fun loadMockResponse(responseCode: Int, responseString: String) {
        mockWebServerRule.server.enqueue(
            MockResponse().setResponseCode(responseCode)
                .setBody(
                    AssetReaderUtil.asset(
                        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext,
                        responseString
                    )
                )
        )
    }
}