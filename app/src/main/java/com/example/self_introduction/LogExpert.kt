package com.example.self_introduction

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

// ─── 1. Target classes whose logs you want to collect ───────────────────────

private val TARGET_CLASSES = listOf(
    "ClassNameA",
    "ClassNameB",
    "ClassNameC",
    "ClassNameD",
    "ClassNameE"
)

// ─── 2. Log collection ───────────────────────────────────────────────────────

/**
 * Reads logcat output and filters lines that belong to any of the target classes.
 * Each class gets its own .txt file, then all files are zipped together.
 *
 * Returns the zip File on success, null on failure.
 */
suspend fun collectAndZipLogs(context: Context): File? = withContext(Dispatchers.IO) {
    try {
        // Run logcat — grab last 5000 lines, all log levels
        val process = Runtime.getRuntime().exec(
            arrayOf("logcat", "-d", "-t", "5000", "-v", "threadtime")
        )
        val allLines = process.inputStream
            .bufferedReader()
            .readLines()

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val exportDir = File(context.cacheDir, "log_export_$timestamp").also { it.mkdirs() }

        // Write one .txt per class, containing only lines tagged with that class name
        TARGET_CLASSES.forEach { className ->
            val classLines = allLines.filter { line -> line.contains(className) }
            if (classLines.isNotEmpty()) {
                File(exportDir, "${className}_$timestamp.txt").writeText(
                    classLines.joinToString("\n")
                )
            }
        }

        // Also write a combined file for convenience
        val combinedLines = allLines.filter { line ->
            TARGET_CLASSES.any { line.contains(it) }
        }
        File(exportDir, "combined_$timestamp.txt").writeText(
            combinedLines.joinToString("\n")
        )

        // Zip everything up
        val zipFile = File(context.cacheDir, "logs_$timestamp.zip")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zos ->
            exportDir.listFiles()?.forEach { file ->
                zos.putNextEntry(ZipEntry(file.name))
                file.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }

        // Clean up temp txt files
        exportDir.deleteRecursively()

        zipFile

    } catch (e: Exception) {
        Log.e("LogExporter", "Failed to collect logs", e)
        null
    }
}

// ─── 3. Share / download the zip via FileProvider ───────────────────────────

/**
 * Opens the Android share sheet for the zip file.
 * The user can save it to Downloads, send via email, etc.
 *
 * Requires a FileProvider entry in AndroidManifest.xml — see note at bottom.
 */
fun shareZipFile(context: Context, zipFile: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",   // must match authority in manifest
        zipFile
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/zip"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Log Export — ${zipFile.name}")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Export Logs"))
}

// ─── 4. Jetpack Compose UI ──────────────────────────────────────────────────

sealed class ExportState {
    object Idle : ExportState()
    object Loading : ExportState()
    data class Success(val file: File) : ExportState()
    data class Error(val message: String) : ExportState()
}

@Composable
fun LogExportScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var exportState by remember { mutableStateOf<ExportState>(ExportState.Idle) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Log Exporter",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Collects logs from:\n${TARGET_CLASSES.joinToString(", ")}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (val state = exportState) {

            is ExportState.Idle -> {
                Button(
                    onClick = {
                        scope.launch {
                            exportState = ExportState.Loading
                            val zip = collectAndZipLogs(context)
                            exportState = if (zip != null) {
                                ExportState.Success(zip)
                            } else {
                                ExportState.Error("Failed to collect logs. Check READ_LOGS permission.")
                            }
                        }
                    }
                ) {
                    Text("Export Logs as ZIP")
                }
            }

            is ExportState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
                Text("Collecting logs…")
            }

            is ExportState.Success -> {
                Text(
                    text = "✓ ${state.file.name}",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { shareZipFile(context, state.file) }) {
                    Text("Download / Share ZIP")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { exportState = ExportState.Idle }) {
                    Text("Export Again")
                }
            }

            is ExportState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { exportState = ExportState.Idle }) {
                    Text("Retry")
                }
            }
        }
    }
}

// ─── 5. Required AndroidManifest.xml additions ──────────────────────────────
//
// Inside <manifest>:
//   <uses-permission android:name="android.permission.READ_LOGS" />
//
// Inside <application>:
//   <provider
//       android:name="androidx.core.content.FileProvider"
//       android:authorities="${applicationId}.fileprovider"
//       android:exported="false"
//       android:grantUriPermissions="true">
//       <meta-data
//           android:name="android.support.FILE_PROVIDER_PATHS"
//           android:resource="@xml/file_provider_paths" />
//   </provider>
//
// res/xml/file_provider_paths.xml:
//   <?xml version="1.0" encoding="utf-8"?>
//   <paths>
//       <cache-path name="log_export" path="." />
//   </paths>
//
// NOTE: READ_LOGS is a privileged permission on Android 8+.
// For debug builds it works fine. For production/release you either need
// the app to be a system app, or use your own in-memory log buffer instead
// of reading from logcat (see Timber + ring buffer approach as alternative).