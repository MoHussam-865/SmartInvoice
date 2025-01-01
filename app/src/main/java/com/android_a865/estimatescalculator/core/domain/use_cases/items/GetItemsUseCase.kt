package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toItems
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GetItemsUseCase(
    val repository: ItemsRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {

    operator fun invoke(path: String): Flow<List<Item>> {

        CoroutineScope(Dispatchers.IO).launch {
            val role = settings.getAppSettings().first().myRole
            if (role == Role.Client) {
                val items = clientApiRepository.getItems(path)
                items.forEach {
                    repository.insertItem(it)
                }
            }
        }

        return repository.getItems(path).map {
            it.toItems()
        }
    }

}