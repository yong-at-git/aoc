import kotlinx.serialization.Serializable
import utils.IoHelper
import java.util.*
import java.util.function.Predicate

class D04 {

    fun getSolution1(): Int {
        return getValidPassports { isValidPassportIgnoringCid(it) }
    }

    fun getSolution2(): Int {
        return getValidPassports { isValidPassportIgnoringCid(it) && isMeetingExtraRequirement(it) }
    }

    private fun getValidPassports(filter: Predicate<Passport>): Int {
        return getParsedPassports().count { filter.test(it) }
    }

    private fun getParsedPassports(): List<Passport> {
        return getInputs()?.split("\n\n").orEmpty().map { parseRawPassport(it) }
    }

    private fun parseRawPassport(rawPassport: String): Passport {
        val pairs = rawPassport.split("\n")
            .flatMap { it.split(" ") }
            .map {
                val pair = it.split(":")
                pair[0] to pair[1]
            }
            .toMap()

        return Passport(
            pairs["ecl"],
            pairs["pid"],
            pairs["eyr"]?.toInt(),
            pairs["hcl"],
            pairs["byr"]?.toInt(),
            pairs["iyr"]?.toInt(),
            pairs["cid"],
            pairs["hgt"]
        )
    }

    private fun getInputs(): String? {
        return IoHelper().getRawContent("d04.in")
    }

    private fun isValidPassportIgnoringCid(passport: Passport): Boolean {
        return Objects.nonNull(passport.byr)
                && Objects.nonNull(passport.ecl)
                && Objects.nonNull(passport.eyr)
                && Objects.nonNull(passport.hcl)
                && Objects.nonNull(passport.hgt)
                && Objects.nonNull(passport.iyr)
                && Objects.nonNull(passport.pid)
    }

    private fun isMeetingExtraRequirement(passport: Passport): Boolean {
        val isHeightInCm = ("^(1[5-9][0-9]cm)$".toRegex().matches(passport.hgt!!)
                && passport.hgt.split("cm")[0].toInt() in 150..193)
        val isHeightInIn = ("^([5-7][0-9]in)$".toRegex().matches(passport.hgt)
                && passport.hgt.split("in")[0].toInt() in 59..76)
        val isHeightValid = isHeightInCm || isHeightInIn

        return isHeightValid
                && passport.byr!! in 1902..2002
                && passport.iyr!! in 2010..2020
                && passport.eyr!! in 2020..2030
                && "^(#[0-9a-f]{6})$".toRegex().matches(passport.hcl!!)
                && "^(amb|blu|brn|gry|grn|hzl|oth)$".toRegex().containsMatchIn(passport.ecl!!)
                && "^([0-9]{9})$".toRegex().matches(passport.pid!!)
    }
}

@Serializable
data class Passport(
    val ecl: String?,
    val pid: String?,
    val eyr: Int?,
    val hcl: String?,
    val byr: Int?,
    val iyr: Int?,
    val cid: String?,
    val hgt: String?
)
