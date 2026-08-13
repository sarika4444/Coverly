package coverly.rules

class PolicyEligibilityRule {

  function evaluate(policyStatus : String,
                    customerRiskLevel : String) : boolean {

    if (policyStatus == "CANCELLED") {
      return false
    }

    if (customerRiskLevel == "CRITICAL") {
      return false
    }

    return true
  }
}
