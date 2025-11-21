package com.example.digital_contest.Write.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

/**
 * 클립보드에 텍스트 복사
 */
fun copyToClipboard(context: Context, text: String) {
    val clipboardManager = getClipboardManager(context)
    copyTextToClipboard(clipboardManager, text)
    showToast(context)
}

/**
 * ClipboardManager 가져오기
 */
private fun getClipboardManager(context: Context): ClipboardManager {
    return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
}

/**
 * 클립보드에 텍스트 설정
 */
private fun copyTextToClipboard(clipboardManager: ClipboardManager, text: String) {
    val clipData = createClipData(text)
    clipboardManager.setPrimaryClip(clipData)
}

/**
 * ClipData 생성
 */
private fun createClipData(text: String): ClipData {
    return ClipData.newPlainText("Copy Text", text)
}

private fun showToast(context: Context) {
    Toast.makeText(context, "텍스트가 복사되었습니다.", Toast.LENGTH_SHORT).show()
}
