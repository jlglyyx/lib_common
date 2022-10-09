package com.yang.lib_common.factory

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

class StringConverterFactory:Converter.Factory() {


    fun create(): StringConverterFactory? {
        return StringConverterFactory()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return StringResponseBodyConverter()
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return StringRequestBodyConverter()
    }


    inner class StringResponseBodyConverter : Converter<ResponseBody,String>{
        override fun convert(value: ResponseBody): String? {
            return value.string()
        }
    }
    inner class StringRequestBodyConverter : Converter<String,RequestBody>{
        override fun convert(value: String): RequestBody? {
            val buffer = Buffer()
            val outputStreamWriter = OutputStreamWriter(buffer.outputStream(), Charset.forName("UTF-8"))
            outputStreamWriter.write(value)
            outputStreamWriter.close()
            return RequestBody.create(MediaType.parse("application/json;charset=utf-8"),buffer.readByteString())
        }

    }
}