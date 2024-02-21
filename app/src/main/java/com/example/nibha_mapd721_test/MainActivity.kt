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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nibha_mapd721_test.datastore.UserStore
import com.example.nibha_mapd721_test.ui.theme.Nibha_MAPD721_TestTheme
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nibha_MAPD721_TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }

    Scaffold(
        topBar = {
            CustomAppBar()
        },
        content = {
            ProductList(userStore)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar() {
    TopAppBar(
        title = {
            Text("Nibha's App")
        },
        actions = {
            IconButton(
                onClick = {
                    // Cart button logic here
                }
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }
            IconButton(
                onClick = {
                    // Clear button logic here
                }
            ) {
                Icon(Icons.Default.Clear, contentDescription = null)
            }
        }
    )
}

@Composable
fun ProductList(userStore: UserStore) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .padding(top = 56.dp + 8.dp)
    ) {
        items(5) { index ->
            ProductItem(
                productName = getFruitName(index),
                price = "${(index + 1) * 2}$",
                onAddToCartClick = {
                    coroutineScope.launch {
                        userStore.saveDetails(getFruitName(index), getFruitName(index), "${(index + 1) * 2}$")
                        Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}
@Composable
fun ProductItem(productName: String, price: String, onAddToCartClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Adjusted arrangement
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(text = productName, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Price: $price", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.width(16.dp))

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