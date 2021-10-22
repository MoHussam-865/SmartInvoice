package com.android_a865.estimatescalculator.feature_client.data.dao

import androidx.room.*
import com.android_a865.estimatescalculator.feature_client.data.entities.ClientEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ClientsDao {

    @Transaction
    @Query("SELECT * FROM Clients ORDER BY id DESC")
    fun getClientsEntity(): Flow<List<ClientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(clientEntity: ClientEntity)

    @Delete
    suspend fun deleteClient(clientEntity: ClientEntity)
}