package com.android_a865.estimatescalculator.feature_network.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_a865.estimatescalculator.feature_network.data.entities.Device
import com.android_a865.estimatescalculator.feature_network.temp.Role
import kotlinx.coroutines.flow.Flow


@Dao
interface DevicesDao {

    @Query("SELECT * FROM Devices WHERE role = :role LIMIT 1" )
    suspend fun getServer(role: Int): Device?

    @Query("SELECT * FROM Devices WHERE role = :role")
    fun getNetworkClients(role: Int): Flow<List<Device>>

    suspend fun insertNewDevice(device: Device) {
        if (device.role == Role.Server.ordinal) {
            // only one server is allowed to exist
            deleteServerIfExist(Role.Server.ordinal)
        }
        insertDevice(device)
    }

    @Query("DELETE FROM Devices WHERE role = :role")
    suspend fun deleteServerIfExist(role: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device)

    @Delete
    suspend fun removeDevice(device: Device)
}