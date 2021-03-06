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
 * *****************************************************************************
 */
package fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.starter;

import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.interfaces.Writable;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.helpers.OPENGLUIHelper;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.components.Console3D;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.components.Loader;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.dto.Game3D;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.dto.framedto.Console3DState;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.dto.framedto.OPENGLFrameState;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.interfaces.ProgressObserver;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.DataUtils;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.OPENGLDisplayUtils;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Thomas.H Warner 2014
 */
public class Starter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // <editor-fold defaultstate="collapsed" desc="UI Manager">    
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }
        //</editor-fold>

        Starter.start3DUI();
        System.exit(0);
    }

    /**
     * Start OpenGL type 3d GUI.
     */
    private static void start3DUI() {

        Starter.startup();
        final boolean reloadPreviousGame = Game3D.getInstance().isReloadPreviousGame();
        Game3D.getInstance().setPreviousMoveQueue(
                reloadPreviousGame ? DataUtils.xmlDeserializeMoveQueue() : null);

        // Display loader. 
        final ProgressObserver loader = new Loader(OPENGLDisplayUtils.getCleintViewport(), null);
        final Writable console = new Console3D(!reloadPreviousGame);
        new OPENGLUIHelper().start(((Console3D) console), reloadPreviousGame, loader);
    }
    
    private static void startup() {
        // deserialization before anything else.
        Game3D.getInstance().deserialize();
        OPENGLFrameState.getInstance().deserialize();
        Console3DState.getInstance().deserialize();
    }

}
