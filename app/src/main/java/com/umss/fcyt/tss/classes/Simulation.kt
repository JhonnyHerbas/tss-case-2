package com.umss.fcyt.tss.classes

import java.io.Serializable

class Simulation : Serializable {
    var simulationHours: Int
    var disconnectionPrice: Int
    var replacementPrice: Int
    var policyCost1: Double
    var policyCost2: Double
    var bestOption: String

    constructor(
        simulationHours: Int,
        disconnectionPrice: Int,
        replacementPrice: Int,
        policyCost1: Double,
        policyCost2: Double,
        bestOption: String
    ) {
        this.simulationHours = simulationHours
        this.disconnectionPrice = disconnectionPrice
        this.replacementPrice = replacementPrice
        this.policyCost1 = policyCost1
        this.policyCost2 = policyCost2
        this.bestOption = bestOption
    }
}
