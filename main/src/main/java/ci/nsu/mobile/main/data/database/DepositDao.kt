package ci.nsu.mobile.main.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<DepositCalculation>>

    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculation?
}