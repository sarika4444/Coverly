package coverly.rules

class BillingRiskRule {

  function calculateRisk(amountDue : double,
                         overdue : boolean) : int {

    var score = 0

    if (amountDue > 0) {
      score += 20
    }

    if (overdue) {
      score += 30
    }

    return Math.min(score, 100)
  }
}
