//package com.shihab.practicesharedprefarence.ui.screen.settingscreen
//
//package com.softzino.barnoipos.ui.screens.salehistory.components
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import com.softzino.barnoipos.R
//import com.softzino.barnoipos.models.Title
//import com.softzino.barnoipos.models.salehistory.Item
//import com.softzino.barnoipos.models.salehistory.dummyItems
//import com.softzino.barnoipos.ui.screens.openclose.components.PaymentSummaryTitle
//import com.softzino.barnoipos.ui.theme.BarnoiPosTheme
//import com.softzino.barnoipos.ui.theme.LocalAppColor
//import com.softzino.barnoipos.utils.PreviewAppScreen
//import com.softzino.barnoipos.utils.extension.formatter
//
//@Composable
//fun RefundProductDialog(
//    onClose: () -> Unit,
//    refundedProducts: List<Item>,
//    modifier: Modifier = Modifier
//) {
//    val orderTitleBackgroundColor = LocalAppColor.current.gray.copy(0.05f)
//
//    val totalReturnAmount = refundedProducts.sumOf { item ->
//        val price = item.unitPrice
//            .replace(item.currencyCode, "")
//            .replace(",", "")
//            .trim()
//            .toDoubleOrNull() ?: 0.0
//
//        val qty = item.orderQuantity ?: item.quantity.toDouble()
//
//        price * qty
//    }
//
//    Dialog(
//        onDismissRequest = onClose
//    ) {
//        Card(
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = MaterialTheme.colorScheme.background
//            ),
//            modifier = modifier
//                .fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier.padding(bottom = 16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(end = 8.dp, top = 8.dp),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = onClose) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "",
//                            modifier = Modifier.size(20.dp),
//                            tint = MaterialTheme.colorScheme.outline
//                        )
//                    }
//                }
//
//                Text(
//                    text = stringResource(R.string.refund_products),
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = stringResource(R.string.refund_products_description),
//                    fontSize = 14.sp,
//                    color = MaterialTheme.colorScheme.outline,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                HorizontalDivider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 14.dp),
//                    thickness = .5.dp,
//                    color = MaterialTheme.colorScheme.outline
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 14.dp)
//                        .verticalScroll(rememberScrollState()),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surface
//                    ),
//                    shape = RoundedCornerShape(0.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .border(
//                                border = BorderStroke(
//                                    width = .5.dp,
//                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//                                )
//                            )
//                    ) {
//                        val textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500)
//
//                        val titles = remember {
//                            mutableListOf(
//                                Title(
//                                    title = "Description",
//                                    weight = 2f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Quantity",
//                                    weight = 0.7f,
//                                    textAlign = TextAlign.Center,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Discount",
//                                    weight = 0.7f,
//                                    textAlign = TextAlign.Center,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Unit Total",
//                                    weight = 0.8f,
//                                    textAlign = TextAlign.End,
//                                    style = textStyle
//                                )
//                            )
//                        }
//                        PaymentSummaryTitle(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .border(
//                                    BorderStroke(
//                                        width = .5.dp,
//                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//                                    )
//                                )
//                                .background(orderTitleBackgroundColor)
//                                .padding(
//                                    horizontal = 8.dp,
//                                    vertical = 12.dp
//                                ),
//                            titles = titles
//                        )
//
//                        refundedProducts.forEachIndexed { index, item ->
//                            val unitName = item.metaData.unit?.orderUnit?.abbreviation
//
//                            val finalQty = item.orderQuantity ?: item.quantity.toDouble()
//                            val qtyString = if (finalQty % 1.0 == 0.0) finalQty.toInt().toString() else finalQty.toString()
//                            val quantityText = if (!unitName.isNullOrEmpty()) "$qtyString ($unitName)" else "$qtyString pcs"
//
//                            val unitPriceVal = item.unitPrice
//                                .replace(item.currencyCode, "")
//                                .replace(",", "")
//                                .trim()
//                                .toDoubleOrNull() ?: 0.0
//
//                            val discountVal = item.discountTotal
//                                .replace(item.currencyCode, "")
//                                .replace(",", "")
//                                .trim()
//                                .toDoubleOrNull() ?: 0.0
//
//                            val calculatedTotal = unitPriceVal * finalQty
//
//                            RefundItemSection(
//                                description = item.description,
//                                quantity = quantityText,
//
//                                discount = stringResource(R.string.taka_sign) + discountVal.formatter(),
//                                unitTotal = stringResource(R.string.taka_sign) + calculatedTotal.formatter(),
//
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .border(
//                                        BorderStroke(
//                                            width = .5.dp,
//                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//                                        )
//                                    )
//                                    .background(if (index % 2 == 0) MaterialTheme.colorScheme.surface else orderTitleBackgroundColor)
//                                    .padding(vertical = 12.dp)
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                HorizontalDivider(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 14.dp),
//                    thickness = .5.dp,
//                    color = MaterialTheme.colorScheme.outline
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Text(
//                        text = stringResource(R.string.return_total) + " " + stringResource(R.string.taka_sign) + totalReturnAmount.formatter(),
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(
//                            top = 10.dp,
//                            end = 12.dp,
//                            bottom = 10.dp
//                        ),
//                        textAlign = TextAlign.End
//                    )
//                }
//            }
//        }
//    }
//}
//
//@PreviewAppScreen
//@Composable
//private fun RefundedProductsDialogPreview() {
//    BarnoiPosTheme {
//        RefundProductDialog(
//            onClose = {},
//            refundedProducts = dummyItems
//        )
//    }
//}
