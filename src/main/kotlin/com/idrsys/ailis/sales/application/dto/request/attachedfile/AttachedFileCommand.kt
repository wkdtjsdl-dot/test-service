package com.idrsys.ailis.sales.application.dto.request.attachedfile

/**
 * base-service에 첨부파일 생성 요청 시 사용하는 Command
 */
data class AttachedFileCreateCommand(
    /**
     * 업로드 타입
     */
    val uploadType: String,

    /**
     * 파일 저장 경로
     */
    val filePath: String,

    /**
     * 원본 파일명
     */
    val orgFileName: String,

    /**
     * 실제 저장된 파일명 (UUID 등으로 생성된 고유 파일명)
     */
    val realFileName: String,

    /**
     * 파일 확장자
     */
    val fileExtension: String? = null,

    /**
     * 파일 크기 (bytes)
     */
    val fileSize: Long,

    /**
     * MIME 타입
     */
    val mimeType: String? = null,

    /**
     * 사용 여부
     */
    val useYn: Boolean = true
)

/**
 * 첨부파일 그룹 생성 요청
 * - 기존 파일 재사용 + 새 파일 추가를 통한 파일 그룹 생성
 */
data class AttachedFileGroupCreateCommand(
    /**
     * 재사용할 기존 파일 ID 목록
     */
    val existingFileIds: List<String> = emptyList(),

    /**
     * 새로 업로드된 파일 정보 목록
     */
    val newFiles: List<AttachedFileCreateCommand> = emptyList()
) {
    init {
        require(existingFileIds.isNotEmpty() || newFiles.isNotEmpty()) {
            "기존 파일 또는 새 파일 중 최소 하나는 필요합니다."
        }
    }
}
