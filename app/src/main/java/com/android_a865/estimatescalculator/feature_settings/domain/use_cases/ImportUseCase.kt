package com.android_a865.estimatescalculator.feature_settings.domain.use_cases

import android.util.Log
import com.android_a865.estimatescalculator.feature_settings.domain.models.DatabaseHolder
import com.android_a865.estimatescalculator.feature_settings.domain.repository.ImportExportRepository
import com.android_a865.estimatescalculator.utils.toObject
import java.lang.Exception

class ImportUseCase(
    private val repository: ImportExportRepository
) {

    suspend operator fun invoke(data0: String) {


        try {
            val data = data0.toObject<DatabaseHolder>()

            // invoices
            data.invoices.forEach {
                repository.insertInvoice(it)
            }
            // clients
            data.clients.forEach {
                repository.insertClient(it)
            }
            // items
            data.items.forEach {
                repository.insertItemEntity(it)
            }

        } catch (e: Exception) {
            Log.d("Importing Error", e.message.toString())
        }

    }

}