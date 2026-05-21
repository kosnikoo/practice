package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository private constructor(private val dao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> = dao.getAll()

    suspend fun saveCalculation(calculation: DepositCalculation) = dao.insert(calculation)

    suspend fun getCalculationById(id: Long): DepositCalculation? = dao.getById(id)

    companion object {
        @Volatile
        private var instance: DepositRepository? = null

        fun getInstance(dao: DepositDao): DepositRepository =
            instance ?: synchronized(this) {
                DepositRepository(dao).also { instance = it }
            }
    }
}