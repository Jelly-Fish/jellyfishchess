/**
 * *****************************************************************************
 * Copyright (c) 2015, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 ******************************************************************************
 */
package fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import org.lwjgl.opengl.Display;

/**
 *
 * @author thw
 */
public class OPENGLDisplayUtils {
    
    /**
     * @param x
     * @param y 
     */
    public static void hideDisplay(final int x, final int y) {
        Display.setLocation(-(x + 20), -(y + 20));
    }
    
    /**
     * @param x
     * @param y 
     */
    public static void showDisplay(final int x, final int y) {
        Display.setLocation(x, y);
    }
    
    /**
     * @return Rectangle of clients screen dimensions as a awt.Rectangle instance.
     */
    public static Rectangle getCleintViewport() {
        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final int width = gd.getDisplayMode().getWidth();
        final int height = gd.getDisplayMode().getHeight();
        return new Rectangle(width, height);
    }

    /**
     * @param x
     * @param y
     * @param defaultX
     * @param defaultY 
     */
    public static void checkDisplayLocation(final int x, final int y, final int defaultX,
            final int defaultY) {
        if (x < 0 && y < 0) {
            OPENGLDisplayUtils.showDisplay(defaultX, defaultY);
        }
    }
    
}
