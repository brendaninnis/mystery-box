package ca.brendaninnis.kmpiap

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.StoreKit.SKPaymentQueue
import platform.StoreKit.SKPaymentTransaction
import platform.StoreKit.SKPaymentTransactionObserverProtocol
import platform.StoreKit.SKPaymentTransactionState
import platform.StoreKit.SKProduct
import platform.StoreKit.SKProductsRequest
import platform.StoreKit.SKProductsRequestDelegateProtocol
import platform.StoreKit.SKProductsResponse
import platform.StoreKit.SKRequest
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun IAPRepository.initialize() {
    initialize(IAPiOS())
}

internal class IAPiOS : PlatformIAP {
    init {
        SKPaymentQueue.defaultQueue().addTransactionObserver(object : NSObject(), SKPaymentTransactionObserverProtocol {
            override fun paymentQueue(
                queue: SKPaymentQueue,
                updatedTransactions: List<*>
            ) {
                for (transaction in updatedTransactions as List<SKPaymentTransaction>) {
                    when (transaction.transactionState) {
                        SKPaymentTransactionState.SKPaymentTransactionStatePurchased -> {
                            // Handle a successful purchase
                            queue.finishTransaction(transaction)
                        }
                        SKPaymentTransactionState.SKPaymentTransactionStateFailed -> {
                            // Handle a failed purchase
                            queue.finishTransaction(transaction)
                        }
                        else -> {
                            println("Transaction state: ${transaction.transactionState}")
                        }
                    }
                }
            }
        })
    }

    override suspend fun getIAPProducts(productIdentifiers: List<String>): Map<String, IAPProduct> =
        suspendCancellableCoroutine { continuation ->
            val request = SKProductsRequest(productIdentifiers.toSet()).apply {
                delegate = object : NSObject(), SKProductsRequestDelegateProtocol {
                    override fun productsRequest(
                        request: SKProductsRequest,
                        didReceiveResponse: SKProductsResponse
                    ) {
                        val products = didReceiveResponse.products as List<SKProduct>
                        val iapProducts = products.associate { skProduct ->
                            val formatter = NSNumberFormatter().apply {
                                numberStyle = NSNumberFormatterCurrencyStyle
                                locale = skProduct.priceLocale
                            }
                            skProduct.productIdentifier to IAPProduct(
                                id = skProduct.productIdentifier,
                                title = skProduct.localizedTitle,
                                description = skProduct.localizedDescription,
                                formattedPrice = formatter.stringFromNumber(skProduct.price) ?: ""
                            )
                        }
                        continuation.resume(iapProducts)
                    }

                    override fun request(request: SKRequest, didFailWithError: NSError) {
                        continuation.resumeWithException(Exception(didFailWithError.localizedDescription))
                    }
                }
            }

            continuation.invokeOnCancellation {
                request.cancel()
            }

            request.start()
        }
}
