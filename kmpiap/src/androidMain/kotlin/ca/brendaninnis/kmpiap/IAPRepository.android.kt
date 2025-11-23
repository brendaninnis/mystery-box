package ca.brendaninnis.kmpiap

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun IAPRepository.initialize(withContext: Context) {
    initialize(IAPAndroid(withContext))
}

internal class IAPAndroid(
    context: Context,
    private val listener: PurchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        // To be implemented in a later section.
    },
    private val billingClient: BillingClient = BillingClient
        .newBuilder(context)
        .setListener(listener)
        .enablePendingPurchases(
            PendingPurchasesParams
                .newBuilder()
                .enablePrepaidPlans()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()
) : PlatformIAP {

    override suspend fun getIAPProducts(productIdentifiers: List<String>): Map<String, IAPProduct> {
        val productList = productIdentifiers.map { productId ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        // leverage queryProductDetails Kotlin extension function
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }

        return productDetailsResult.productDetailsList?.associate {
            it.productId to
            IAPProduct(
                id = it.productId,
                title = it.title,
                description = it.description,
                formattedPrice = "$4.20"
            )
        } ?: emptyMap()
    }
}
