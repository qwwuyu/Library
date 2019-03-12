/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.github.chrisbanes.photoview;

/** 处理移动、滑动、缩放事件 */
interface OnGestureListener {
    /** 移动 */
    void onDrag(float dx, float dy);

    /** 滑动 */
    void onFling(float startX, float startY, float velocityX, float velocityY);

    /** 缩放 */
    void onScale(float scaleFactor, float focusX, float focusY);
}