//Name: Nibha Maharjan
//Student ID: 301282952
//Mid-Term Test Semester 2

package com.example.nibha_mapd721_test

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.nibha_mapd721_test.datastore.UserStore
import com.example.nibha_mapd721_test.ui.theme.Nibha_MAPD721_TestTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nibha_MAPD721_TestTheme {
                // Surface for the entire app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Main content of the app
                    MainContent()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    // Getting the current context
    val context = LocalContext.current
    // Creating an instance of UserStore
    val userStore = remember { UserStore(context) }
// Scaffold for the app structure
    Scaffold(
        topBar = {
            // Custom app bar with title and actions
            CustomAppBar(userStore)
        },
        // Product list displayed in a LazyColumn
        content = {
            ProductList(userStore)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(userStore: UserStore) {
    // State variable for tracking the shopping cart's visibility
    var isShoppingCartOpen by remember { mutableStateOf(false) }
    // Coroutine scope for handling asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // TopAppBar with title and actions
    TopAppBar(
        title = {
            Text("Nibha's App")
        },

        actions = {
            // Shopping cart icon button
            IconButton(
                onClick = {
                    isShoppingCartOpen = true
                }
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }
            // Clear button to remove items from the cart
            IconButton(
                onClick = {
                    // Clear button logic here
                    coroutineScope.launch {
                        userStore.clearCart()  // Call clearCart within a coroutine
                    }
                }
            ) {
                Text("Clear")
            }
        }
    )
// Display shopping cart dialog if it is open
    if (isShoppingCartOpen) {
        ShoppingCart(
            userStore = userStore,
            onDismiss = {
                isShoppingCartOpen = false
            }
        )
    }
}

@Composable
fun ProductList(userStore: UserStore) {
    // Getting the current context
    val context = LocalContext.current
    // Coroutine scope for handling asynchronous operations
    val coroutineScope = rememberCoroutineScope()
    // State for collecting and updating cart details
    val detailsState by rememberUpdatedState(userStore.getDetails.collectAsState(emptyList()))

    // LazyColumn for displaying the product list
    LazyColumn(
        modifier = Modifier
            .padding(top = 56.dp + 8.dp)
    ) {
        // Generating product items based on the fruit names
        items(5) { index ->
            ProductItem(
                productName = getFruitName(index),
                price = "${(index + 1) * 2}$",
                onAddToCartClick = {
                    coroutineScope.launch {
                        // Adding item to the cart
                        userStore.saveDetails(getFruitName(index), getFruitName(index), "${(index + 1) * 2}$")
                        // Displaying a toast message
                        Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}


@Composable
fun ProductItem(productName: String, price: String, onAddToCartClick: () -> Unit) {
    // Card for each product item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Row with product details and add to cart button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Adjusted arrangement
        ) {
            // Column for product name and price
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = productName, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Price: $price", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.width(16.dp))
            // Button to add product to cart
            Button(
                onClick = {
                    onAddToCartClick()
                },
                modifier = Modifier
                    .height(60.dp),
            ) {
                Text("Add to Cart")
            }
        }
    }
}
@Composable
fun ShoppingCart(userStore: UserStore, onDismiss: () -> Unit) {
    // State for collecting and updating cart details
    val detailsState = userStore.getDetails.collectAsState(null)
    val details = detailsState.value ?: emptyList()

    // Dialog for displaying shopping cart items
    Dialog(
        onDismissRequest = onDismiss,
        content = {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Shopping Cart Items", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (details.isNotEmpty()) {
                        for (item in details) {
                            val itemName = item.split(":")[1].trim()  // Extracting item name
                            if (item.contains("Name") || item.contains("Price")) {
                                Text(itemName, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    } else {
                        Text("Your shopping cart is empty.", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = onDismiss) {
                        Text("Close")
                    }
                }
            }
        }
    )
}

// Function to get the fruit name based on the index
fun getFruitName(index: Int): String {
    return when (index) {
        0 -> "Apple"
        1 -> "Banana"
        2 -> "Orange"
        3 -> "Grapes"
        4 -> "Watermelon"
        else -> "Unknown Fruit"
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    Nibha_MAPD721_TestTheme {
        ProductList(UserStore(LocalContext.current))
    }
}
