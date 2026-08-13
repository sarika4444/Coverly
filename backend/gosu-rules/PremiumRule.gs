package coverly.rules

class PremiumRule {

  function calculatePremium(insuredValue : double,
                             policyType : String) : double {

    var rate = 0.045

    if (policyType == "HOME") {
      rate = 0.03
    }

    if (policyType == "MOTOR") {
      rate = 0.045
    }

    return insuredValue * rate
  }
}
