package com.elmaddinasger.facedetector.dedection

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.elmaddinasger.facedetector.graphic.GraphicOverlay
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage

abstract class BaseCameraAnalyzer<T : List<*>> : ImageAnalysis.Analyzer {

    abstract val graphicOverlay : GraphicOverlay<*>

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        mediaImage?.let { image ->
            detectInImage(InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees))
                .addOnSuccessListener { results ->
                    onSuccess(results as List<com.google.mlkit.vision.face.Face>, graphicOverlay, image.cropRect)

                    imageProxy.close()
                }
                .addOnFailureListener {
                    onFailure(it)
                    imageProxy.close()
                }
        }
    }

    protected abstract fun detectInImage(image : InputImage) : Task<T>

    abstract fun stop()

    protected abstract fun onSuccess(
        results: List<com.google.mlkit.vision.face.Face>,
        graphicOverlay: GraphicOverlay<*>,
        rect: Rect
    )

    protected abstract fun onFailure(e : Exception)

}