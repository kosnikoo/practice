package ci.nsu.mobile.main.domain

object DepositCalculator {
    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        annualRate: Double,
        monthlyTopUp: Double?
    ): Pair<Double, Double> {
        if (periodMonths == 0) return initialAmount to 0.0
        val monthlyRate = annualRate / 12 / 100
        val topUp = monthlyTopUp ?: 0.0

        val finalAmount = if (monthlyRate == 0.0) {
            initialAmount + topUp * periodMonths
        } else {
            val factor = Math.pow(1 + monthlyRate, periodMonths.toDouble())
            initialAmount * factor + topUp * (factor - 1) / monthlyRate
        }

        val totalTopUp = topUp * periodMonths
        val interestEarned = finalAmount - initialAmount - totalTopUp
        return finalAmount to interestEarned
    }
}