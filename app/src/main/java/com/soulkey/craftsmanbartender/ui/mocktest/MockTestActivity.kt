package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.lib.common.BaseActivity

class MockTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test)
    }

    override fun onBackPressed() {
        finish()
    }
}