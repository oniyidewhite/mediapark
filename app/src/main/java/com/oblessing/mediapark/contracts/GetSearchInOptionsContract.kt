package com.oblessing.mediapark.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.oblessing.mediapark.model.SearchCriteria
import com.oblessing.mediapark.screens.filterDate.FilterSearchInActivity

class GetSearchInOptionsContract : ActivityResultContract<SearchCriteria, SearchCriteria?>() {
    override fun createIntent(context: Context, input: SearchCriteria?): Intent {
        return Intent(context, FilterSearchInActivity::class.java).apply {
            putExtra(key, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SearchCriteria? {
        if (resultCode == Activity.RESULT_OK) {
            return intent?.getSerializableExtra(key) as? SearchCriteria
        }
        return null
    }

    companion object {
        const val key = "options-criteria-key"
    }
}