package words.list.task.observer

import android.widget.ImageView

interface OnImagePreviewObserver {
    fun onOpenViewer(startPosition: Int, imageView: ImageView)
}