package com.example.sharedclipboard.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Stable
data class Product(
    val id: Int,
    val name: String,
    var isSelected: MutableState<Boolean>
) {
    companion object {
        val Saver: Saver<Product, *> = mapSaver(
            save = {
                mapOf(
                    "id" to it.id,
                    "name" to it.name,
                    "isSelected" to it.isSelected.value
                )
            },
            restore = {
                Product(
                    it["id"] as Int,
                    it["name"] as String,
                    mutableStateOf(it["isSelected"] as Boolean)
                )
            })
    }
}

@Immutable
data class Products(val products: List<Product>) {
    companion object {
        val Saver: Saver<Products, *> = listSaver(
            save = {
                it.products
            },
            restore = {
                Products(it)
            }
        )
    }
}

@Composable
fun rememberListState(initial: Products): Products {
    return rememberSaveable(
        initial,
        saver = Products.Saver
        ) {
        initial
    }
}

@Composable
fun ProductListScreen(
    products: Products,
    onProductChange: (Int) -> Unit,
    selectedCount: Int,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()



    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 5
        }
    }
//    var showScrollToTop by remember { mutableStateOf(false) }

//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

    Box(modifier) {
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Text(text = "Выбрано: $selectedCount")
            }
            item {}

            items(
                items = products.products,
                key = { product -> product.id }
            ) { product ->
                ProductItem(
                    product = product,
                    onSelectionChanged = onProductChange
                )
            }
        }
//    }


//    LaunchedEffect(listState.firstVisibleItemIndex) {
//        showScrollToTop = listState.firstVisibleItemIndex > 5
//    }

        val coroutineScope = rememberCoroutineScope()

        if (showScrollToTop) {

            Button(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
//                LaunchedEffect(Unit) {
//                    listState.animateScrollToItem(0)
//                }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Наверх")
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onSelectionChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(product.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = product.isSelected.value,
            onCheckedChange = {
                onSelectionChanged(product.id)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = product.name)
    }
}

@Composable
@Preview
fun ProductListScreenPreview() {
    val products = remember {
        Products(List(100) { index ->
            Product(
                id = index,
                name = "Product $index",
                isSelected = mutableStateOf(true)
            )
        })
    }
    var selectedCount by remember {
        mutableStateOf(products.products.count { it.isSelected.value })
    }
    ProductListScreen(
        products,
        { id ->
            val product = products.products.find { it.id == id } ?: return@ProductListScreen
            product.isSelected.value = !product.isSelected.value
            if (product.isSelected.value){
                selectedCount = selectedCount + 1
            } else {
                selectedCount = selectedCount - 1
            }
        },
        selectedCount
    )

}
