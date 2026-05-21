package ci.nsu.mobile.main.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DepositSharedViewModel : ViewModel() {
    private val _initialAmount = MutableLiveData<Double?>()
    val initialAmount: LiveData<Double?> = _initialAmount

    private val _periodMonths = MutableLiveData<Int?>()
    val periodMonths: LiveData<Int?> = _periodMonths

    private val _monthlyTopUp = MutableLiveData<Double?>()
    val monthlyTopUp: LiveData<Double?> = _monthlyTopUp

    private val _selectedRate = MutableLiveData<Double?>()
    val selectedRate: LiveData<Double?> = _selectedRate

    fun setInitialAmount(amount: Double?) { _initialAmount.value = amount }
    fun setPeriodMonths(months: Int?) { _periodMonths.value = months }
    fun setMonthlyTopUp(topUp: Double?) { _monthlyTopUp.value = topUp }
    fun setSelectedRate(rate: Double?) { _selectedRate.value = rate }

    fun clear() {
        _initialAmount.value = null
        _periodMonths.value = null
        _monthlyTopUp.value = null
        _selectedRate.value = null
    }
}