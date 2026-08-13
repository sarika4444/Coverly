package coverly.rules

class ClaimRiskRule {

  function calculateRisk(claimAmount : double,
                         insuredValue : double,
                         previousClaims : int) : int {

    var score = 0
    var ratio = claimAmount / insuredValue

    if (ratio > 0.70) {
      score += 30
    } else if (ratio > 0.50) {
      score += 15
    }

    if (claimAmount > 500000) {
      score += 20
    }

    if (previousClaims >= 3) {
      score += 20
    }

    return Math.min(score, 100)
  }
}
