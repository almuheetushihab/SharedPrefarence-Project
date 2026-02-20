//package com.softzino.barnoipos.ui.screens.salehistory
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Menu
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.SnackbarResult
//import androidx.compose.material3.Text
//import androidx.compose.material3.pulltorefresh.PullToRefreshBox
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.paging.LoadState
//import androidx.paging.compose.LazyPagingItems
//import androidx.paging.compose.collectAsLazyPagingItems
//import androidx.paging.compose.itemKey
//import com.softzino.barnoipos.R
//import com.softzino.barnoipos.models.Event
//import com.softzino.barnoipos.models.Title
//import com.softzino.barnoipos.models.salehistory.SaleHistory
//import com.softzino.barnoipos.ui.components.Loading
//import com.softzino.barnoipos.ui.screens.emptyscreen.EmptyScreen
//import com.softzino.barnoipos.ui.screens.openclose.components.PaymentSummaryTitle
//import com.softzino.barnoipos.ui.screens.salehistory.components.SaleHistoryCountSummary
//import com.softzino.barnoipos.ui.screens.salehistory.components.SaleHistoryItem
//import com.softzino.barnoipos.ui.theme.BarnoiPosTheme
//import com.softzino.barnoipos.utils.DateTimeOperation
//import com.softzino.barnoipos.utils.extension.formatter
//import com.softzino.barnoipos.utils.previews.PreviewAppScreen
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.isActive
//
//@Composable
//fun SaleHistoryScreen(
//    viewModel: SaleHistoryViewModel,
//    navigateToSaleScreen: (SaleHistory) -> Unit,
//    openDrawer: () -> Unit
//) {
//    val state = viewModel.state.collectAsStateWithLifecycle()
//    val searchTFV = viewModel.searchText
//
//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(lifecycleOwner) {
//        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
//            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
//                viewModel.checkRegisterStatus()
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    LaunchedEffect(
//        Unit
//    ) {
//        viewModel.checkRegisterStatus()
//        viewModel.loadPosts(query = null)
//    }
//
//    LaunchedEffect(
//        searchTFV.value
//    ) {
//        viewModel.loadPosts(query = searchTFV.value)
//    }
//
//    val pagedPosts = state.value.saleHistories.collectAsLazyPagingItems()
//
//    SaleHistoryScreenSkeleton(
//        openDrawer = openDrawer,
//        navigateToSaleScreen = {
//            navigateToSaleScreen(it)
//        },
//        state = state.value,
//        showLoadingView = state.value.isLoading,
//        showMessage = state.value.message,
//        repos = pagedPosts,
//        searchTFV = searchTFV,
//        openSearch = viewModel.openSearch.value,
//        setOpenSearch = viewModel::setOpenSearch,
//        isRegisterClosed = state.value.isRegisterClosed,
//        retryDataLoad = {
//            viewModel.loadPosts(
//                query = searchTFV.value,
//                delayRequest = false
//            )
//        },
//        onRefresh = viewModel::pullToRefresh
//    )
//}
//
//@Suppress("ktlint:compose:modifier-missing-check")
//@Composable
//fun SaleHistoryScreenSkeleton(
//    openDrawer: () -> Unit,
//    navigateToSaleScreen: (SaleHistory) -> Unit,
//    state: SaleHistoryUiState,
//    showLoadingView: Boolean,
//    showMessage: Event<String>?,
//    repos: LazyPagingItems<SaleHistory>,
//    searchTFV: MutableState<String>,
//    openSearch: Boolean = false,
//    setOpenSearch: (Boolean) -> Unit = {},
//    isRegisterClosed: Boolean = false,
//    retryDataLoad: () -> Unit = {},
//    onRefresh: () -> Unit
//) {
//    val snackbarHostState = remember { SnackbarHostState() }
//    // val focusRequester = remember { FocusRequester() }
//
//    val lazyListState = rememberLazyListState()
//
//    LaunchedEffect(showMessage, retryDataLoad) {
//        showMessage?.let { message ->
//            message.getValueOnce()?.let { value ->
//                val result = snackbarHostState.showSnackbar(value)
//
//                if (result == SnackbarResult.ActionPerformed) {
//                    retryDataLoad()
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(setOpenSearch) {
//        delay(300)
//        if (openSearch && isActive) {
//            setOpenSearch(true)
//        }
//    }
//
//    Scaffold(
//        modifier = Modifier
//            .navigationBarsPadding()
//            .statusBarsPadding()
//            .imePadding(),
//        topBar = {
//            CenterAlignedTopAppBar(
//                navigationIcon = {
//                    IconButton(
//                        onClick = {
//                            openDrawer()
//                        },
//                        content = {
//                            Icon(
//                                imageVector = Icons.Outlined.Menu,
//                                contentDescription = ""
//                            )
//                        }
//                    )
//                },
//                title = {
//                    Text(
//                        text = stringResource(R.string.sales_history),
//                        style = MaterialTheme.typography.headlineLarge
//                    )
//                }
//            )
//        }
//    ) { innerPadding ->
//
//        PullToRefreshBox(
//            isRefreshing = state.isRefreshing,
//            onRefresh = {
//                onRefresh()
//            },
//            modifier = Modifier
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//
//            ) {
//                if (showLoadingView) {
//                    Loading(
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Card(
//                    modifier = Modifier
//                        .padding(
//                            horizontal = 16.dp
//                        )
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    elevation = CardDefaults.elevatedCardElevation(
//                        defaultElevation = 2.dp
//                    ),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surface
//                    ),
//                    border = BorderStroke(
//                        width = 0.3.dp,
//                        color = MaterialTheme.colorScheme.outlineVariant
//                    )
//
//                ) {
//                    Column {
//                        Spacer(modifier = Modifier.height(30.dp))
//
//                        SaleHistoryCountSummary(
//                            modifier = Modifier.padding(start = 16.dp),
//                            saleCount = repos.itemCount.toString(),
//                            onSearch = {
//                                searchTFV.value = it
//                            }
//                        )
//
//                        val textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W500)
//
//                        val titles = remember {
//                            mutableListOf(
//                                Title(
//                                    title = "",
//                                    weight = 0.2f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Receipt",
//                                    weight = 1f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Customer",
//                                    weight = 1f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Sold By",
//                                    weight = 0.8f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Sale Total",
//                                    weight = 0.8f,
//                                    textAlign = TextAlign.Start,
//                                    style = textStyle
//                                ),
//                                Title(
//                                    title = "Status",
//                                    weight = 0.5f,
//                                    textAlign = TextAlign.Center,
//                                    style = textStyle
//                                ),
//
//                                Title(
//                                    title = "Action",
//                                    weight = 0.5f,
//                                    textAlign = TextAlign.Center,
//                                    style = textStyle
//                                )
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        PaymentSummaryTitle(
//                            modifier = Modifier
//                                .border(
//                                    border = BorderStroke(
//                                        width = 0.5.dp,
//                                        color = MaterialTheme.colorScheme.outline.copy(0.5f)
//                                    )
//                                )
//                                .background(
//                                    color = MaterialTheme.colorScheme.outlineVariant
//                                        .copy(0.2f)
//                                )
//                                .padding(
//                                    vertical = 20.dp
//                                ),
//                            titles = titles
//                        )
//
//                        Box(
//                            Modifier
//                                .fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            repos.apply {
//                                Column(
//                                    Modifier.verticalScroll(rememberScrollState())
//                                ) {
//                                    if (loadState.refresh !is LoadState.Loading &&
//                                        loadState.prepend !is LoadState.Loading &&
//                                        loadState.append !is LoadState.Loading &&
//                                        loadState.prepend.endOfPaginationReached &&
//                                        itemCount == 0
//                                    ) {
//                                        EmptyScreen(
//                                            modifier = Modifier.fillMaxSize(),
//                                            emptyMgs = "No history found for \"${searchTFV.value}\""
//                                        )
//                                    }
//                                }
//                            }
//
//                            LazyColumn(
//                                Modifier
//                                    .fillMaxSize(),
//                                state = lazyListState,
//                                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
//                            ) {
//                                items(
//                                    count = repos.itemCount,
//                                    key = repos.itemKey { it.id }
//                                ) { index ->
//                                    val it = repos[index]
//
//                                    if (it == null) {
//                                        Text(
//                                            modifier = Modifier.fillMaxWidth(),
//                                            text = "Loading...",
//                                            textAlign = TextAlign.Center
//                                        )
//                                    } else {
//                                        SaleHistoryItem(
//                                            id = it.orderNumber,
//                                            date = DateTimeOperation.dataTime(
//                                                date = it.saleAt
//                                            ),
//                                            customerName = it.customer?.fullName ?: "",
//                                            customerPhone = it.customer?.phone ?: "",
//                                            sellerName = it.seller?.firstName ?: "",
//                                            sellerPhone = it.seller?.phone ?: "",
//                                            amount = stringResource(R.string.taka_sign) + it.total.formatter(currency = it.currencyCode).formatter(),
//                                            status = it.resolveStatus(),
//                                            isExchange = it.isExchanged,
//                                            isRefunded = it.isRefunded,
//                                            onReturnProduct = {
//                                                navigateToSaleScreen(it)
//                                            },
//                                            isRegisterClosed = isRegisterClosed,
//                                            isEnableToExchanged = it.isEnableToExchanged,
//                                            saleHistoryDetails = it
//                                        )
//                                    }
//                                }
//
//                                repos.apply {
//                                    when {
//                                        loadState.refresh is LoadState.Loading -> {
//                                            item {
//                                                Column {
//                                                    Text("Loading...")
//                                                }
//                                            }
//                                        }
//
//                                        loadState.append is LoadState.Loading -> {
//                                            item {
//                                                Column {
//                                                    Text("Loading...")
//                                                }
//                                            }
//                                        }
//
//                                        loadState.refresh is LoadState.Error -> {
//                                            item {
//                                                // Note: this should be full screen using fillParentMaxSize()
//                                                ErrorItem(
//                                                    message = "Error! " + (repos.loadState.refresh as LoadState.Error).error.message,
//                                                    onRetryClick = {
//                                                        retry()
//                                                    }
//                                                )
//                                            }
//                                        }
//
//                                        loadState.append is LoadState.Error -> {
//                                            item {
//                                                ErrorItem(
//                                                    message = "Error! " + (repos.loadState.append as LoadState.Error).error.message,
//                                                    onRetryClick = {
//                                                        retry()
//                                                    }
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        /*            SaleHistorySection(
//                                        saleHistories = state,
//                                        onReturnProduct = onReturnProduct
//                                    )*/
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun ErrorItem(
//    message: String,
//    onRetryClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(16.dp, 8.dp),
//        shape = RoundedCornerShape(8.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.errorContainer
//        )
//    ) {
//        Column(
//            Modifier.fillMaxWidth()
//        ) {
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
//                text = message,
//                fontSize = 16.sp,
//                color = MaterialTheme.colorScheme.onErrorContainer,
//                textAlign = TextAlign.Center
//            )
//
//            Button(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(8.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.onErrorContainer
//                ),
//                onClick = {
//                    onRetryClick()
//                }
//            ) {
//                Text("Retry")
//            }
//        }
//    }
//}
//
//@PreviewAppScreen
//@Composable
//private fun SaleHistoryScreenPreview() {
//    val searchText = remember { mutableStateOf("") }
//    val saleHistoryUiState = SaleHistoryUiState()
//    val pagedPosts = saleHistoryUiState.saleHistories.collectAsLazyPagingItems()
//
//    BarnoiPosTheme {
//        SaleHistoryScreenSkeleton(
//            openDrawer = {},
//            navigateToSaleScreen = {},
//            showLoadingView = false,
//            showMessage = null,
//            state = saleHistoryUiState,
//            repos = pagedPosts,
//            searchTFV = searchText,
//            openSearch = false,
//            setOpenSearch = {},
//            retryDataLoad = {},
//            onRefresh = {}
//        )
//    }
//}
