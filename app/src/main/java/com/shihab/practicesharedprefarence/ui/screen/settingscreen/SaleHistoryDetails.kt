//package com.softzino.barnoipos.ui.screens.salehistory.components
//
//import android.widget.Toast
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Info
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.RichTooltip
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TooltipBox
//import androidx.compose.material3.TooltipDefaults
//import androidx.compose.material3.rememberTooltipState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.softzino.barnoipos.R
//import com.softzino.barnoipos.models.Title
//import com.softzino.barnoipos.models.salehistory.Item
//import com.softzino.barnoipos.models.salehistory.SaleHistory
//import com.softzino.barnoipos.models.salehistory.SaleMetaData
//import com.softzino.barnoipos.ui.components.ButtonWithIcon
//import com.softzino.barnoipos.ui.components.KeyValue
//import com.softzino.barnoipos.ui.components.SectionTitle
//import com.softzino.barnoipos.ui.screens.openclose.components.PaymentSummaryTitle
//import com.softzino.barnoipos.ui.theme.BarnoiPosTheme
//import com.softzino.barnoipos.ui.theme.LocalAppColor
//import com.softzino.barnoipos.utils.PreviewAppScreen
//import com.softzino.barnoipos.utils.extension.formatter
//import com.softzino.softzuilib.tag.SLTag
//import java.util.Locale
//import kotlinx.coroutines.launch
//
//@Composable
//fun SaleHistoryDetails(
//    saleHistoryDetails: SaleHistory,
//    onReturnProduct: () -> Unit,
//    isRegisterClosed: Boolean,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//    var showRefundDialog by remember { mutableStateOf(false) }
//    val discountPercent = saleHistoryDetails.meta_data?.customer?.membershipDiscountPercentage
//    val breakdownList = saleHistoryDetails.meta_data?.discountBreakdown ?: emptyList()
//    val membershipAmount = breakdownList.find { it.key == "membership_discount" }?.amount ?: 0.0
//    val customAmount = breakdownList.find { it.key == "custom_discount" }?.amount ?: 0.0
//    val totalDiscountValue = saleHistoryDetails.discountTotal
//        .replace(saleHistoryDetails.currencyCode, "")
//        .replace(",", "")
//        .trim()
//        .toDoubleOrNull() ?: 0.0
//
//    val generalDiscountAmount = totalDiscountValue - membershipAmount - customAmount
//
//    Column(
//        modifier = modifier
//            .padding(
//                start = 2.dp,
//                top = 2.dp,
//                end = 2.dp,
//                bottom = 2.dp
//            )
//            .border(
//                width = 1.dp,
//                color = Color.Blue
//            )
//
//    ) {
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            modifier = Modifier.padding(horizontal = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            SectionTitle(
//                title = stringResource(R.string.sale_details)
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            val exchangedFromOrder = saleHistoryDetails.exchange?.orderReturn?.orderNumber
//            // FIXME: comment out for store credit
//            Row(
//                modifier = Modifier,
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                if (isRegisterClosed) {
//                    val tooltipState = rememberTooltipState(isPersistent = true)
//                    val scope = rememberCoroutineScope()
//
//                    TooltipBox(
//                        modifier = Modifier.padding(),
//                        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
//                        tooltip = {
//                            RichTooltip(
//                                title = { Text("") },
//                                action = {}
//                            ) {
//                                Text(stringResource(R.string.sale_history_tools_tips))
//                            }
//                        },
//                        state = tooltipState
//                    ) {
//                        IconButton(onClick = {
//                            scope.launch { tooltipState.show() }
//                        }) {
//                            Icon(
//                                imageVector = Icons.Outlined.Info,
//                                contentDescription = "Localized Description",
//                                tint = LocalAppColor.current.orange,
//                                modifier = Modifier.rotate(180f)
//                            )
//                        }
//                    }
//                }
//
//                CustomIconButton(
//                    size = 42.dp,
//                    iconRes = R.drawable.printer,
//                    onClick = {
//                        Toast.makeText(context, "Printer Clicked", Toast.LENGTH_SHORT).show()
//                    },
//                    modifier = Modifier
//                        .padding(end = 8.dp)
//                )
//
//                if (!exchangedFromOrder.isNullOrEmpty()) {
//                    Surface(
//                        shape = RoundedCornerShape(4.dp),
//                        color = Color.Transparent,
//                        modifier = Modifier.padding(end = 10.dp)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
//                        ) {
//                            Text(
//                                text = "Order Exchanged From : $exchangedFromOrder",
//                                color = LocalAppColor.current.blue,
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//
//                if (!saleHistoryDetails.isRefunded && exchangedFromOrder.isNullOrEmpty()) {
//                    ButtonWithIcon(
//                        onButtonClick = onReturnProduct,
//                        contentPadding = PaddingValues(horizontal = 8.dp),
//                        enabled = saleHistoryDetails.isEnableToExchanged
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.redo),
//                            contentDescription = "",
//                            modifier = Modifier.size(18.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(text = stringResource(R.string.return_products))
//                    }
//                }
//
//                if (saleHistoryDetails.isRefunded) {
//                    Button(
//                        onClick = { showRefundDialog = true },
//                        modifier = Modifier,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = LocalAppColor.current.blue,
//                            contentColor = MaterialTheme.colorScheme.surface
//                        ),
//                        shape = RoundedCornerShape(8.dp),
//                        contentPadding = PaddingValues(horizontal = 16.dp),
//                        enabled = true
//                    ) {
//                        Text(text = stringResource(R.string.view_refunded_product))
//                    }
//                }
//            }
//            if (showRefundDialog) {
//                RefundProductDialog(
//                    onClose = { showRefundDialog = false },
//                    refundedProducts = saleHistoryDetails.refundItemsList
//                    // refundedProducts = com.softzino.barnoipos.models.salehistory.dummyItems
//                )
//            }
//        }
//
//        val textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500)
//
//        val titles = remember {
//            mutableListOf(
//                Title(
//                    title = "Product Name",
//                    weight = 1f,
//                    textAlign = TextAlign.Start,
//                    style = textStyle
//                ),
//                Title(
//                    title = "QTY",
//                    weight = 0.4f,
//                    textAlign = TextAlign.End,
//                    style = textStyle
//                ),
//                Title(
//                    title = "Unit Price",
//                    weight = 0.8f,
//                    textAlign = TextAlign.End,
//                    style = textStyle
//                ),
//                Title(
//                    title = "Discount",
//                    weight = 0.6f,
//                    textAlign = TextAlign.End,
//                    style = textStyle
//                ),
//                Title(
//                    title = "Unit Total",
//                    weight = 0.8f,
//                    textAlign = TextAlign.End,
//                    style = textStyle
//                )
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        PaymentSummaryTitle(
//            modifier = Modifier
//                .padding(
//                    vertical = 10.dp,
//                    horizontal = 16.dp
//                ),
//            titles = titles
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    vertical = 10.dp,
//                    horizontal = 16.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        saleHistoryDetails.items.forEach {
//            DetailsProducts(
//                modifier = Modifier.padding(
//                    horizontal = 16.dp,
//                    vertical = 8.dp
//                ),
//                product = it
//            )
//
//            HorizontalDivider(
//                modifier = Modifier
//                    .padding(
//                        vertical = 10.dp,
//                        horizontal = 16.dp
//                    ),
//                color = MaterialTheme.colorScheme.outlineVariant,
//                thickness = 0.5.dp
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//        ) {
//            Spacer(
//                modifier = Modifier.weight(1f)
//            )
//
//            Spacer(
//                modifier = Modifier.weight(0.4f)
//            )
//
//            Spacer(
//                modifier = Modifier.weight(0.8f)
//            )
//
//            Text(
//                modifier = Modifier.weight(0.6f),
//                text = stringResource(R.string.sub_total),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W500,
//                textAlign = TextAlign.End
//            )
//
//            Text(
//                modifier = Modifier.weight(0.8f),
//                text = saleHistoryDetails.subtotal.replace(saleHistoryDetails.currencyCode, stringResource(R.string.taka_sign)),
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W500,
//                textAlign = TextAlign.End
//            )
//        }
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    vertical = 10.dp,
//                    horizontal = 16.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//        /*
//                KeyValue(
//                    modifier = Modifier.padding(
//                        start = 50.dp,
//                        end = 16.dp
//                    ),
//                    text = stringResource(R.string.coupon_code),
//                    value = stringResource(R.string.taka_sign) + saleHistoryDetails.couponCode,
//                    valueFontSize = 16.sp,
//                    valueFontWeight = FontWeight.W500
//                )*/
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 4.dp,
//                    start = 50.dp,
//                    end = 16.dp,
//                    bottom = 4.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = stringResource(R.string.discount),
//            value = "- " + stringResource(id = R.string.taka_sign) + " " + String.format(
//                Locale.US,
//                "%.2f",
//                generalDiscountAmount
//            ),
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 4.dp,
//                    start = 50.dp,
//                    end = 16.dp,
//                    bottom = 4.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = if (discountPercent != null && discountPercent > 0) {
//                "${stringResource(id = R.string.member_discount)} ($discountPercent%)"
//            } else {
//                stringResource(id = R.string.member_discount)
//            },
//            value = "-" + stringResource(id = R.string.taka_sign) + " " + String.format(
//                Locale.US,
//                "%.2f",
//                saleHistoryDetails.meta_data?.discountBreakdown
//                    ?.find { it.key == "membership_discount" }?.amount ?: 0.0
//            ),
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 4.dp,
//                    start = 50.dp,
//                    end = 16.dp,
//                    bottom = 4.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        /* KeyValue(
//             modifier = Modifier.padding(
//                 start = 50.dp,
//                 end = 16.dp
//             ),
//             text = stringResource(R.string.store_credit),
//             value = stringResource(R.string.taka_sign) + saleHistoryDetails.storeCredit,
//             valueFontSize = 16.sp,
//             valueFontWeight = FontWeight.W500
//         )
//         */
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = stringResource(R.string.custom_discount),
//            value = "-" + (
//                    saleHistoryDetails.currencyCode + String.format(
//                        Locale.US,
//                        "%.2f",
//                        saleHistoryDetails.meta_data?.discountBreakdown
//                            ?.find { it.key == "custom_discount" }?.amount ?: 0.0
//                    )
//                    ).replace(saleHistoryDetails.currencyCode, stringResource(R.string.taka_sign) + " "),
//
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 4.dp,
//                    start = 50.dp,
//                    end = 16.dp,
//                    bottom = 4.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = "VAT",
//            value = "+" + saleHistoryDetails.taxTotal
//                .replace(saleHistoryDetails.currencyCode, stringResource(R.string.taka_sign)),
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 2.dp,
//                    start = 50.dp,
//                    end = 16.dp,
//                    bottom = 2.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = "",
//            value = saleHistoryDetails.total
//                .replace(saleHistoryDetails.currencyCode, stringResource(R.string.taka_sign)),
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        saleHistoryDetails.payment?.metaData?.forEach {
//            KeyValue(
//                modifier = Modifier.padding(
//                    start = 50.dp,
//                    end = 16.dp
//                ),
//                text = it.paymentInformation,
//                value = stringResource(R.string.taka_sign) + it.amount.toDouble().formatter(),
//                valueFontSize = 16.sp,
//                valueFontWeight = FontWeight.W500
//            )
//        }
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 10.dp,
//                    start = 16.dp,
//                    end = 16.dp,
//                    bottom = 10.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(
//                    horizontal = 16.dp
//                ),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Sale Total",
//                style = MaterialTheme.typography.titleSmall,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W500
//
//            )
//
//            SLTag(
//                modifier = Modifier
//                    .padding(
//                        start = 10.dp
//                    )
//                    .background(
//                        color = MaterialTheme.colorScheme.outlineVariant.copy(0.1f),
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .border(
//                        border = BorderStroke(
//                            1.dp,
//                            color = MaterialTheme.colorScheme.outlineVariant.copy(0.1f)
//                        ),
//                        shape = RoundedCornerShape(8.dp)
//                    ),
//                text = saleHistoryDetails.totalProductQuantity().toString() + " Products",
//                textColor = MaterialTheme.colorScheme.outline,
//                border = null,
//                backgroundColor = null
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = saleHistoryDetails.total
//                    .replace(saleHistoryDetails.currencyCode, stringResource(R.string.taka_sign)),
//                style = MaterialTheme.typography.bodyMedium,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.W500
//            )
//        }
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 10.dp,
//                    start = 16.dp,
//                    end = 16.dp,
//                    bottom = 10.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = stringResource(R.string.prev_loyalty),
//            value = saleHistoryDetails.meta_data?.loyaltyPoint?.previous.toString(),
//
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = stringResource(R.string.this_invoice),
//            value = saleHistoryDetails.meta_data?.loyaltyPoint?.earn.toString(),
//
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        HorizontalDivider(
//            modifier = Modifier
//                .padding(
//                    top = 10.dp,
//                    start = 16.dp,
//                    end = 16.dp,
//                    bottom = 10.dp
//                ),
//            color = MaterialTheme.colorScheme.outlineVariant,
//            thickness = 0.5.dp
//        )
//
//        KeyValue(
//            modifier = Modifier.padding(
//                start = 50.dp,
//                end = 16.dp
//            ),
//            text = stringResource(R.string.total_balance_points),
//            value = saleHistoryDetails.meta_data?.loyaltyPoint?.total.toString(),
//
//            valueFontSize = 16.sp,
//            valueFontWeight = FontWeight.W500
//        )
//
//        Spacer(
//            modifier = Modifier.height(10.dp)
//        )
//
//        Column(
//            modifier = Modifier.padding(
//                horizontal = 10.dp
//            )
//        ) {
//            Text(
//                text = stringResource(R.string.note),
//                style = MaterialTheme.typography.bodyMedium
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Text(
//                text = stringResource(R.string.note_description),
//                color = MaterialTheme.colorScheme.outline,
//                fontSize = 15.sp
//            )
//        }
//    }
//}
//
//@Composable
//fun DetailsProducts(
//    product: Item,
//    modifier: Modifier = Modifier
//) {
//    val unitName = product.metaData.unit?.orderUnit?.abbreviation
//    val finalQty = product.orderQuantity ?: product.quantity.toDouble()
//    val qtyString = if (finalQty % 1.0 == 0.0) {
//        finalQty.toInt().toString()
//    } else {
//        finalQty.toString()
//    }
//
//    val quantityText = if (!unitName.isNullOrEmpty()) {
//        "$qtyString ($unitName)"
//    } else {
//        "${product.quantity}x"
//    }
//
//    val finalUnitPrice = product.orderUnitPrice ?: product.unitPrice
//    val metaData = product.metaData
//    val isRewarded = metaData.isRewarded == true
//    val hasDiscount = metaData.discount != null
//
//    val itemStatusText = when {
//        isRewarded && hasDiscount -> " (Free Item)"
//
//        isRewarded && !hasDiscount -> " (Gift Item)"
//
//        else -> ""
//    }
//
//    val productName = buildAnnotatedString {
//        append(product.description)
//
//        if (itemStatusText.isNotEmpty()) {
//            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 12.sp)) {
//                append(itemStatusText)
//            }
//        }
//    }
//
//    Row(modifier = modifier) {
//        Text(
//            modifier = Modifier.weight(1f),
//            text = productName,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.Start
//        )
//
//        Text(
//            modifier = Modifier.weight(0.4f),
//            text = quantityText,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.End
//        )
//
//        Text(
//            modifier = Modifier.weight(0.8f),
//            text = finalUnitPrice.replace(product.currencyCode, stringResource(R.string.taka_sign)),
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.End
//        )
//
//        Text(
//            modifier = Modifier.weight(0.6f),
//            text = product.discountTotal.replace(product.currencyCode, stringResource(R.string.taka_sign)),
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.End
//        )
//
//        Text(
//            modifier = Modifier.weight(0.8f),
//            text = product.subtotal.replace(product.currencyCode, stringResource(R.string.taka_sign)),
//            fontSize = 16.sp,
//            fontWeight = FontWeight.W500,
//            textAlign = TextAlign.End
//        )
//    }
//}
//
//@PreviewAppScreen
//@Composable
//private fun ButtonWithIconPreview() {
//    BarnoiPosTheme {
//        SaleHistoryDetails(
//            saleHistoryDetails = SaleHistory(
//                amountChange = "0.00",
//                amountDue = "0.00",
//                amountPaid = "0.00",
//                amountRefund = "0.00",
//                currencyCode = "৳",
//                customer = null,
//                discountTotal = "0.00",
//                exchange = null,
//                id = 0,
//                items = emptyList(),
//                refundItems = emptyList(),
//                orderNumber = "123456789",
//                payment = null,
//                paymentStatus = "",
//                saleAt = "",
//                seller = null,
//                fullPaid = false,
//                status = "",
//                subtotal = "0.00",
//                taxTotal = "0.00",
//                total = "0.00",
//                returnData = null,
//                totalExcludingTax = "0.00",
//                meta_data = SaleMetaData(
//                    discountBreakdown = emptyList(),
//                    additionalAmountBreakdown = emptyList(),
//                    deviceInfo = null,
//                    customer = null,
//                    loyaltyPoint = null
//                )
//            ),
//            onReturnProduct = {},
//            isRegisterClosed = false
//        )
//    }
//}
