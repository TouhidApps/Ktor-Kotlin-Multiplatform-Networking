package com.touhidapps.ktor.repository

import io.ktor.client.HttpClient
import com.touhidapps.ktor.model.FlowersModel
import com.touhidapps.ktor.network.AllApi
import io.ktor.http.parameters
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import org.koin.core.qualifier.named
import com.touhidapps.ktor.network.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import io.ktor.client.request.parameter
import io.ktor.client.statement.request
import io.ktor.http.contentLength
import io.ktor.http.encodedPath

class FlowerRepository(private val client: HttpClient) {

    suspend fun getFlowers(): ResultNet<FlowersModel, NetworkError> {

        var reqLog = ""

        val response = try {
            val response = client.get {
                url { builder ->
                    encodedPath = AllApi.FLOWERS
//                    parameters.append("page", page.toString())
                }
            }

            // url, req, resp, error code
            // TODO make reqLog formated and place
            reqLog = response.request.url.toString() + response.requestTime + response.responseTime + response.headers.toString() + response.request.method.value + response.request.contentLength() + response.request.content + response.body()

            ResultNet.Success(response.body<FlowersModel>())

        } catch(e: UnresolvedAddressException) {
            ResultNet.Error(NetworkError.NoInternet(
                message = "No Internet Connection",
                log = "${e?.cause?.toString()}"
            ))
        } catch(e: SerializationException) {
            ResultNet.Error(NetworkError.Serialization(
                message = "Serialization Error",
                log = "${e?.cause?.toString()}"
            ))
        } catch (e: Exception) {
            // TODO how error comes in android, ios and wasm
            // TODO cache when no internet
            val c = e?.cause?.toString() ?: ""
            val err = if (c.contains("ConnectException")) {
                "No Internet Connection"
            } else if (c.contains("JsonConvertException") || c.contains("JsonDecodingException")) {
                "JSON Serialization Error"
            } else {
                "${e?.cause?.toString() ?: e?.message ?: "-"}"
            }

            ResultNet.Error(NetworkError.Unknown(
                message = err,
                log = "${e?.cause}\n${e?.cause?.toString()}"
            ))
        }

        return response

    }

}


