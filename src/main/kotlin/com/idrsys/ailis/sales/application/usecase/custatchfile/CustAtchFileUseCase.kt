package com.idrsys.ailis.sales.application.usecase.custatchfile

import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileCommand
import com.idrsys.ailis.sales.application.dto.request.custatchfile.CustAtchFileSearchParam
import com.idrsys.ailis.sales.application.dto.response.CustAtchFileResponse
import kotlinx.coroutines.flow.Flow

interface CustAtchFileUseCase {
    fun getFiles(searchParam: CustAtchFileSearchParam): Flow<CustAtchFileResponse>
    suspend fun saveFile(command: CustAtchFileCommand, creator: String): CustAtchFileResponse
    suspend fun deleteFile(custAtchFileId: String)
}
