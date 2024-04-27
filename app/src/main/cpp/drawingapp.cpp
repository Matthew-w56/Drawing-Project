// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("drawingapp");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("drawingapp")
//      }
//    }

#include <jni.h>
#include <time.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string>

#define  LOG_TAG    "C++ Processing"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

// Info, Pixels, x, y
#define GET_PIXEL_PTR(i, p, x, y) (uint32_t*)((char*)p + y * i->stride + x * 4)
// Broken, probably
#define GET_ROW(p, s, y) (  (uint32_t*)(( (char*)p + (s * y) ))  )

static uint32_t pack_rgba(int red, int green, int blue, int alpha) {
    return (
            ((alpha << 24) & 0xFF000000) |
            ((red << 16) & 0x00FF0000) |
            ((green << 8) & 0x0000FF00) |
            (blue & 0x000000FF) |
            0xFF000000);
}

static int rgb_clamp(int value) {
    if(value > 255) {
        return 255;
    }
    if(value < 0) {
        return 0;
    }
    return value;
}

static void apply_image_transform(AndroidBitmapInfo* info, void* pixels, uint32_t* newVals, int newValsSize) {
    int w = info->width - 4;
    for (int i = 0; i < newValsSize; i++) {
        int x = (i % w) + 2;
        int y = (i / w) + 2;
        *GET_PIXEL_PTR(info, pixels, x, y) = newVals[i];
    }
}


static int32_t get_average_val(AndroidBitmapInfo* info, void* pixels, int x, int y) {
    int sum_red, sum_green, sum_blue, sum_alpha, red, green, blue;
    sum_red = 0;
    sum_green = 0;
    sum_blue = 0;
    sum_alpha = 0;
    for (int yy = y-2; yy < y+2; yy++) {
        for (int xx = x-2; xx < x+2; xx++) {

            uint32_t p = *GET_PIXEL_PTR(info, pixels, xx, yy);
            if (p >> 24 < 100) {
                sum_alpha += 255;
                sum_red += 255;
                sum_green += 255;
                sum_blue += 255;
            } else {
                sum_alpha += (int) (p >> 24);
                sum_red += (int) ((p & 0x00FF0000) >> 16);
                sum_green += (int)((p & 0x0000FF00) >> 8);
                sum_blue += (int) (p & 0x00000FF );
            }


        } // End x loop
    } // End y loop

    // Set the pixel color as average of
    sum_red = sum_red / 8.8;
    sum_green = sum_green / 8.8;
    sum_blue = sum_blue / 8.8;
    sum_alpha = sum_alpha / 9;
    if (sum_red > 255) sum_red = 255;
    if (sum_green > 255) sum_green = 255;
    if (sum_blue > 255) sum_blue = 255;
    return pack_rgba(sum_red, sum_green, sum_blue, sum_alpha);
}

/**
 * Method to invert the values of the pixels to be used in do in doActualInvert
 * @param info
 * @param pixels
 * @param x
 * @param y
 * @return
 */
static int32_t invert_pixel_val(AndroidBitmapInfo* info, void* pixels, int x, int y) {
    int alpha, red, green, blue;
    red = 0;
    green = 0;
    blue = 0;
    alpha = 0;

    uint32_t p = *GET_PIXEL_PTR(info, pixels, x, y);
    alpha = (int) (p >> 24);
    red = (int) ((p & 0x00FF0000) >> 16);
    green = (int)((p & 0x0000FF00) >> 8);
    blue = (int) (p & 0x00000FF );

    // Returns the pixel values subtracted from 255 to get the inversion of each pixel's value.
    return pack_rgba(255 - red, 255 - green, 255 - blue, alpha);
}


static void doActualBlur(AndroidBitmapInfo* info, void* pixels) {
    int w = info->width - 4;
    int h = info->height - 4;
    int newValsSize = w * h;
    uint32_t newVals[newValsSize];
    uint32_t* line;
    int v_i = 0;
    for (int y = 2; y < info->height - 2; y++) {
        line = GET_ROW(pixels, info->stride, y);

        for (int x = 2; x < info->width - 2; x++) {
            newVals[v_i] = get_average_val(info, pixels, x, y);

            v_i ++;
        } // End x loop
    } // End y loop

    apply_image_transform(info, pixels, newVals, newValsSize);

} // End blurImage def

/**
 * Method to apply inverted pixel values to bitmap
 * @param info - A bitmap to alter
 * @param pixels
 */
static void doActualInvert(AndroidBitmapInfo* info, void* pixels){
    int w = info->width - 4;
    int h = info->height - 4;
    int newValsSize = w * h;
    uint32_t newVals[newValsSize];
    uint32_t* line;
    int v_i = 0;
    for (int y = 2; y < info->height - 2; y++) {
        line = GET_ROW(pixels, info->stride, y);

        for (int x = 2; x < info->width - 2; x++) {
            newVals[v_i] = invert_pixel_val(info, pixels, x, y);

            v_i ++;
        } // End x loop
    } // End y loop

    apply_image_transform(info, pixels, newVals, newValsSize);
}


extern "C" JNIEXPORT void JNICALL Java_com_example_drawingapp_MyViewModel_blurImage(JNIEnv * env, jobject  obj, jobject bitmap) {

    AndroidBitmapInfo info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    // Method call!
    doActualBlur(&info, pixels);
    // End method call

    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C" {
JNIEXPORT void JNICALL Java_com_example_drawingapp_MyViewModel_invertImage(JNIEnv * env, jobject  obj, jobject bitmap)
{

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
    }

    doActualInvert(&info,pixels);

    AndroidBitmap_unlockPixels(env, bitmap);
}
}



