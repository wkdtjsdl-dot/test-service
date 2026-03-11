package com.idrsys.ailis.sales.application.dto.response

import com.fasterxml.jackson.annotation.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalResponse<T> @JsonCreator constructor(
    @JsonProperty("response") val response: HospitalResponseContent<T>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalResponseContent<T> @JsonCreator constructor(
    @JsonProperty("header") val header: HospitalDataHeader,
    @JsonProperty("body") val body: HospitalDataBody<T>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalDataHeader @JsonCreator constructor(
    @JsonProperty("resultCode") val resultCode: String,
    @JsonProperty("resultMsg") val resultMsg: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalDataBody<T> @JsonCreator constructor(
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("items") val items: HospitalDataItems<T>?,
    @JsonProperty("numOfRows") val numOfRows: Int,
    @JsonProperty("pageNo") val pageNo: Int,
    @JsonProperty("totalCount") val totalCount: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalDataItems<T> @JsonCreator constructor(
    @JsonFormat(with = [JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY])
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("item") val item: List<T> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalInfo @JsonCreator constructor(
    @JsonProperty("addr") val addr: String?,
    @JsonProperty("clCd") val clCd: String?,
    @JsonProperty("clCdNm") val clCdNm: String?,
    @JsonProperty("drTotCnt") val drTotCnt: Int?,
    @JsonProperty("estbDd") val estbDd: String?,
    @JsonProperty("hospUrl") val hospUrl: String?,
    @JsonProperty("postNo") val postNo: String?,
    @JsonProperty("sgguCd") val sgguCd: String?,
    @JsonProperty("sgguCdNm") val sgguCdNm: String?,
    @JsonProperty("sidoCd") val sidoCd: String?,
    @JsonProperty("sidoCdNm") val sidoCdNm: String?,
    @JsonProperty("telno") val telno: String?,
    @JsonProperty("emdongNm") val emdongNm: String?,
    @JsonProperty("XPos") val xPos: Double?,
    @JsonProperty("YPos") val yPos: Double?,
    @JsonProperty("yadmNm") val yadmNm: String,
    @JsonProperty("ykiho") val ykiho: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MediSbjtInfo @JsonCreator constructor(
    @JsonProperty("dgsbjtCd") val dgsbjtCd: String,      // 진료과목코드
    @JsonProperty("dgsbjtCdNm") val dgsbjtCdNm: String,  // 진료과목코드명
    @JsonProperty("dgsbjtPrSdrCnt") val dgsbjtPrSdrCnt: Int  // 진료과목별 전문의수
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeviceInfo @JsonCreator constructor(
    @JsonProperty("oftCd") val oftCd: String,      // 장비코드
    @JsonProperty("oftCdNm") val oftCdNm: String,  // 장비명
    @JsonProperty("oftCnt") val oftCnt: Int         // 보유대수
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalClosureInfo @JsonCreator constructor(
    @JsonProperty("addr") val addr: String?,
    @JsonProperty("clCdNm") val clCdNm: String?,
    @JsonProperty("cnclDd") val cnclDd: Int?,
    @JsonProperty("crtrYm") val crtrYm: Int?,
    @JsonProperty("estbCnclTp") val estbCnclTp: String?,
    @JsonProperty("estbDd") val estbDd: Int?,
    @JsonProperty("sidoCd") val sidoCd: Int?,
    @JsonProperty("sidoCdNm") val sidoCdNm: String?,
    @JsonProperty("telno") val telno: String?,
    @JsonProperty("yadmNm") val yadmNm: String,
    @JsonProperty("ykiho") val ykiho: String
)