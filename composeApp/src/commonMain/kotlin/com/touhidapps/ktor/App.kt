package com.touhidapps.ktor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.touhidapps.ktor.di.koinViewModel
import com.touhidapps.ktor.viewModel.FlowerViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import ktorkotlinmultiplatformnetworking.composeapp.generated.resources.Res
import ktorkotlinmultiplatformnetworking.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinApplication
import com.touhidapps.ktor.di.appModule
import okio.FileSystem
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

@Composable
@Preview
fun App() {

    var isKoinInitialized by remember { mutableStateOf(false) } // To avoid execution of UI before Koin

    LaunchedEffect(Unit) {
        // To avoid already initialize exception
        val alreadyExists = KoinPlatformTools.defaultContext().getOrNull() != null
        if (!alreadyExists) {
            startKoin {
                modules(appModule)
            }
        }
        isKoinInitialized = true
    }

    if (isKoinInitialized) {
        MaterialTheme {

            val viewModelFlower: FlowerViewModel = koinViewModel() // For Koin DI + ViewModel
            val flowers by viewModelFlower.flowers.collectAsState()

            val errorMessage by viewModelFlower.errorMessage.collectAsState()
            val isLoading by viewModelFlower.isLoading.collectAsState()

            LaunchedEffect(Unit) {
                viewModelFlower.getFlowersData()
            }

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(20.dp))

                if (!isLoading) {
                    Button(
                        onClick = {
                            viewModelFlower.getFlowersData()
                        }
                    ) {
                        Text("Reload Data")
                    }
                }

                if (!errorMessage.isNullOrBlank()) {
                    Text(text = "Error: ${errorMessage}")
                }
                if (isLoading) {
                    LinearProgressIndicator(color = Color.Red)
                } else {
                    if (errorMessage.isEmpty()) {
                        if (flowers.myJsonObject.isEmpty()) {
                            Text(text = "No data found")
                        } else {
                            Text(text = "A list of ${flowers.myJsonObject.size} colorful items")
                            LazyColumn {
                                items(flowers.myJsonObject) { item ->

                                    Row(
                                        modifier = Modifier.fillMaxWidth().height(80.dp)
                                            .clickable {

                                                println("Clicked on: ${item.id}--${item.name}")

                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier = Modifier.width(10.dp))

                                        CoilImage(
                                            modifier = Modifier.size(70.dp).clip(
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                            imageModel = { "${item.imgData?.baseUrl ?: ""}${item.imgData?.fileName ?: ""}" }, // loading a network image or local resource using an URL.
                                            imageOptions = ImageOptions(
                                                contentScale = ContentScale.Crop,
                                                alignment = Alignment.Center
                                            )
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(text = item.name ?: "", style = TextStyle(
                                                fontSize = 18.sp,
                                                color = Color.Black,
                                                textAlign = TextAlign.Left
                                            ))
                                            Text(text = item.details ?: "", style = TextStyle(
                                                fontSize = 14.sp,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Left
                                            ))
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }

                                }
                            }
                        }
                    }
                }

            }

        }
    }

}


fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512MB
        .build()
}