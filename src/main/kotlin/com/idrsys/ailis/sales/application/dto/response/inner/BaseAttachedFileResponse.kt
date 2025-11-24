package com.idrsys.ailis.sales.application.dto.response.inner

import java.time.LocalDateTime

/**
 * base-service의 AttachedFile 응답 DTO
 * /api/inner/attachments/{id} 호출 시 사용
 */
data class BaseAttachedFileResponse(
    val attachedFileId: String?,
    val attachedFileGroupId: String?,
    val fileType: String,
    val filePath: String,
    val fileName: String,
    val orgFileName: String,
    val fileExtension: String?,
    val fileSize: Long,
    val mimeType: String?,
    val useYn: Boolean,
    val creator: String,
    val createDtime: LocalDateTime,
    val updater: String,
    val updateDtime: LocalDateTime
)
