package com.soulkey.craftsmanbartender.ui.mocktest

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.soulkey.craftsmanbartender.R
import com.soulkey.craftsmanbartender.lib.common.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MockTestActivity : BaseActivity() {

    private val mockTestViewModel : MockTestViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_test)
    }

    override fun onBackPressed() {
        if (mockTestViewModel.isTestFinished.value == false) {
            MaterialDialog(this)
                    .title(text = "Warning")
                    .message(text = "테스트가 완료되지 않았습니다. 정말 테스트를 종료할까요?")
                    .positiveButton {
                        finish()
                    }
                    .negativeButton()
                    .show()
        } else {
            finish()
        }
    }
}