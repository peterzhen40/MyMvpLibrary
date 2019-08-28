package cn.cbsd.mvplibrary.kit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View

/**
 * Author: zhenyanjun
 * Date  : 2017/5/8 11:14
 */

object ProgressUtil {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun showProgress(context: Context, formView: View, progress: View, show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = context.resources.getInteger(android.R.integer.config_shortAnimTime)

            formView.visibility = if (show) View.GONE else View.VISIBLE
            formView.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    formView.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            progress.visibility = if (show) View.VISIBLE else View.GONE
            progress.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progress.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.visibility = if (show) View.VISIBLE else View.GONE
            formView.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}
