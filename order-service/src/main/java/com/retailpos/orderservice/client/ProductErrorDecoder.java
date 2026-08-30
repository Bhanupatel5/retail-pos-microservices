//package com.retailpos.orderservice.client;
//
//import java.io.IOException;
//
//import org.springframework.http.HttpStatus;
//
//import com.retailpos.orderservice.exception.ProductNotFoundException;
//
//import feign.Response;
//import feign.codec.ErrorDecoder;
//
//public class ProductErrorDecoder implements ErrorDecoder {
//
//    private final ErrorDecoder defaultErrorDecoder =
//            new ErrorDecoder.Default();
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//
//        if (response.status() == HttpStatus.NOT_FOUND.value()) {
//            return new ProductNotFoundException(
//                    "Product not found");
//        }
//
//        return defaultErrorDecoder.decode(
//                methodKey,
//                response);
//    }
//}