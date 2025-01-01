package com.android_a865.estimatescalculator.core.domain.use_cases.items

import com.android_a865.estimatescalculator.core.data.local.mapper.toEntity
import com.android_a865.estimatescalculator.core.domain.repository.ItemsRepository
import com.android_a865.estimatescalculator.core.data.local.entity.Item
import com.android_a865.estimatescalculator.core.domain.repository.ClientsApiRepository
import com.android_a865.estimatescalculator.core.enu.Role
import com.android_a865.estimatescalculator.feature_settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class AddItemUseCase(
    val repository: ItemsRepository,
    private val settings: SettingsRepository,
    private val clientApiRepository: ClientsApiRepository
) {
    suspend operator fun invoke(item: Item): Int {

        val role = settings.getAppSettings().first().myRole

        if (role == Role.Client) {
            clientApiRepository.sendData(item.toEntity())
            return 0
        } else {
            val itemId = repository.insertItem(item.toEntity())
            return itemId.toInt()
        }
    }
}