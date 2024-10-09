package com.shaikhabdulgani.tmdb.base

import android.util.Log
import com.shaikhabdulgani.tmdb.core.data.util.Result
import com.shaikhabdulgani.tmdb.core.domain.util.Resource
import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

open class BaseRepository {

    companion object {
        private const val TAG = "BaseRepository"
    }

    protected val pageSize = 20

    protected suspend fun <T> executeWithFlow(
        emitState: EmitState = EmitState.EMIT_ALL,
        executeAPITask: suspend FlowCollector<Resource<T>>.() -> T
    ): Flow<Resource<T>> = flow {
        try {
            if (emitState != EmitState.EMIT_ONLY_SUCCESS || emitState != EmitState.EXCLUDE_LOADING) {
                emit(Resource.loading())
            }
            val res = executeAPITask.invoke(this)
            emit(Resource.success(res))
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Error In Api: ${e.message}")
            if (emitState != EmitState.EMIT_ONLY_SUCCESS) {
                emit(Resource.error(e.message))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error In Api: ${e.message}")
            if (emitState != EmitState.EMIT_ONLY_SUCCESS) {
                emit(Resource.error(e.message.toString()))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error In Repository: ${e.message}")
            if (emitState != EmitState.EMIT_ONLY_SUCCESS) {
                emit(Resource.error(e.message.toString()))
            }
        }
    }.catch {
        if (emitState != EmitState.EMIT_ONLY_SUCCESS) {
            emit(Resource.error(it.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    protected suspend fun <T> execute(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: ClientRequestException) {
            Log.e(TAG, "Error In Api: ${e.message}")
            Result.failure(e.message)
        } catch (e: IOException) {
            Log.e(TAG, "Error In Api: ${e.message}")
            Result.failure(e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Error In Repository: ${e.message}")
            e.printStackTrace()
            Result.failure(e.message)
        }
    }


    protected enum class EmitState {
        EMIT_ONLY_SUCCESS,
        EMIT_ALL,
        EXCLUDE_LOADING
    }
}