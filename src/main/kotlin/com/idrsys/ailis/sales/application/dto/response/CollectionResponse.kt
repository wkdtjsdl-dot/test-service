package com.idrsys.ailis.sales.application.dto.response

import com.idrsys.ailis.sales.domain.model.BankDeposit
import com.idrsys.ailis.sales.domain.model.CardPayment
import com.idrsys.ailis.sales.domain.model.CollectionBill
import com.idrsys.ailis.sales.domain.model.CollectionLedger
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


data class CollectionBillListResponse(
   val colbillId: String?,
   val custCd: String,
   val colbillDt: LocalDate,
   val payMethodCd: String,
   val payAmt: BigDecimal,
   val cardPayId: String? = null,
   val bankDepositId: String? = null,
   val cardCompCd: String? = null,
   val cardCompNm: String? = null,
   val cardApprNo: String? = null,
   val cardNo: String? = null,
   val cardBillNo: String? = null,
   val instlMonth: String? = null,
   val accountYear: String? = null,
   val surecpSlstmtNo: String? = null,
   val salesSlstmtNo: String? = null,
   val advreceYn: Boolean = false,
   val closingCd: String? = null,
   val colledgerId: String? = null,
   val sendYn: Boolean = false,
   val sapCustCd: String? = null,
    val bzoffiCd: String? = null,
    val custNm: String? = null,
    val accountNo: String? = null,
    val remark: String? = null,
)
/**
 * Collection Bill Response DTO
 */
data class CollectionBillResponse(
    val colbillId: String,
    val custCd: String,
    val custNm: String? = null,
    val branchNm: String? = null,
    val colbillDt: LocalDate,
    val payMethodCd: String,
    val payAmt: BigDecimal,
    val sendYn: Boolean,
    val cardPayId: String? = null,
    val bankDepositId: String? = null,
    val colledgerId: String? = null,
    val registered: Boolean = true
) {
    companion object {
        fun from(bill: CollectionBill, colledgerId: String? = null): CollectionBillResponse {
            return CollectionBillResponse(
                colbillId = bill.colbillId!!,
                custCd = bill.custCd,
                colbillDt = bill.colbillDt,
                payMethodCd = bill.payMethodCd,
                payAmt = bill.payAmt,
                sendYn = bill.sendYn,
                cardPayId = bill.cardPayId,
                bankDepositId = bill.bankDepositId,
                colledgerId = colledgerId
            )
        }
    }
}

/**
 * Delete Collection Bill Response DTO
 */
data class DeleteCollectionBillResponse(
    val colbillId: String,
    val deleted: Boolean
)

/**
 * Send Collection Response DTO
 */
data class SendCollectionResponse(
    val sentCount: Int,
    val results: List<SendCollectionResult>
)

data class SendCollectionResult(
    val colbillId: String,
    val sent: Boolean,
    val message: String
)

/**
 * Split Collection Response DTO
 */
data class SplitCollectionResponse(
    val cardPayId: String? = null,
    val bankDepositId: String? = null,
    val totalPayAmt: BigDecimal,
    val splitCount: Int,
    val colbills: List<CollectionBillResponse>
)

/**
 * Collection Ledger Response DTO
 */
data class CollectionLedgerResponse(
    val custCd: String,
    val custNm: String? = null,
    val transactions: List<CollectionLedgerTransaction>,
    val totalDemandAmt: BigDecimal,
    val totalCollectionAmt: BigDecimal,
    val arBalance: BigDecimal
)

data class CollectionLedgerTransaction(
    val colledgerId: String,
    val colbillDt: LocalDate,
    val division: String,
    val colbillItemNm: String,
    val colbillAmt: BigDecimal,
    val balance: BigDecimal
) {
    companion object {
        fun from(ledger: CollectionLedger, balance: BigDecimal): CollectionLedgerTransaction {
            return CollectionLedgerTransaction(
                colledgerId = ledger.colledgerId!!,
                colbillDt = ledger.colbillDt,
                division = if (ledger.colbillDivCd == "0") "청구" else "수금",
                colbillItemNm = ledger.colbillItemNm,
                colbillAmt = ledger.colbillAmt,
                balance = balance
            )
        }
    }
}

/**
 * Card Payment Response DTO
 */
data class CardPaymentResponse(
    val cardPayId: String,
    val regYn: Boolean,
    val payDivCd: String,
    val shopId: String,
    val tradeNo: String,
    val cardBillNo: String? = null,
    val cardCompCd: String? = null,
    val cardCompNm: String? = null,
    val cardNo: String? = null,
    val instlMonth: String? = null,
    val payAmt: BigDecimal,
    val payDt: String,
    val payTime: String? = null,
    val cardApprNo: String? = null,
    val outamt: BigDecimal
) {
    companion object {
        fun from(cardPayment: CardPayment): CardPaymentResponse {
            return CardPaymentResponse(
                cardPayId = cardPayment.cardPayId,
                regYn = cardPayment.regYn,
                payDivCd = cardPayment.payDivCd,
                shopId = cardPayment.shopId,
                tradeNo = cardPayment.tradeNo,
                cardBillNo = cardPayment.cardBillNo,
                cardCompCd = cardPayment.cardCompCd,
                cardCompNm = cardPayment.cardCompNm,
                cardNo = cardPayment.cardNo,
                instlMonth = cardPayment.instlMonth,
                payAmt = cardPayment.payAmt,
                payDt = cardPayment.payDt,
                payTime = cardPayment.payTime,
                cardApprNo = cardPayment.cardApprNo,
                outamt = cardPayment.outamt
            )
        }
    }
}

/**
 * Bank Deposit Response DTO
 */
data class BankDepositResponse(
    val bankDepositId: String,
    val regYn: Boolean,
    val accountNo: String? = null,
    val accountYear: String? = null,
    val surecpSlstmtNo: String? = null,
    val depositDt: LocalDate,
    val depositAmt: BigDecimal,
    val outamt: BigDecimal? = null,
    val crcyCd: String? = null,
    val remark: String? = null
) {
    companion object {
        fun from(bankDeposit: BankDeposit): BankDepositResponse {
            return BankDepositResponse(
                bankDepositId = bankDeposit.bankDepositId,
                regYn = bankDeposit.regYn,
                accountNo = bankDeposit.accountNo,
                accountYear = bankDeposit.accountYear,
                surecpSlstmtNo = bankDeposit.surecpSlstmtNo,
                depositDt = bankDeposit.depositDt,
                depositAmt = bankDeposit.depositAmt,
                outamt = bankDeposit.outamt,
                crcyCd = bankDeposit.crcyCd,
                remark = bankDeposit.remark
            )
        }
    }
}
