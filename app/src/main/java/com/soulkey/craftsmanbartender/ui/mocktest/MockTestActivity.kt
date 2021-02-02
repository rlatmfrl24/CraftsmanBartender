package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import kotlinx.android.synthetic.main.activity_mock_test.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MockTestActivity : BaseActivity() {

    private val mockTestViewModel: MockTestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test)
    }

    override fun onBackPressed() {
        finish()
    }
}