/*******************************************************************************
 * Copyright (c) 2014, Thomas.H Warner.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 *******************************************************************************/

package fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.uci;

import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.uci.externalengine.IOExternalEngine;

/**
 * UCI protocol for external chess engine.
 * @author Thomas.H Warner 2014
 */
public class UCIProtocolDriver {
    
    //<editor-fold defaultstate="collapsed" desc="private vars">
    /**
     * Singleton.
     */
    private static UCIProtocolDriver instance;
    
    /**
     * IO events driver for an external engine.
     */
    private IOExternalEngine ioExternalEngine;

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private constructor">
    private UCIProtocolDriver() { }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public methods">
    /**
     * Singleton accesor.
     * @return UCIProtocolDriver instance.
     */
    public static UCIProtocolDriver getInstance() {
        if (instance == null) {
            instance = new UCIProtocolDriver();
            instance.init();
            return instance;
        } else {
            return instance;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private methods">
    /**
     * Initialize the UCI external engine driver.
     */
    private void init() {
        ioExternalEngine = IOExternalEngine.getInstance();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public IOExternalEngine getIoExternalEngine() {
        return ioExternalEngine;
    }

    public void setIoExternalEngine(final IOExternalEngine ioExternalEngine) {
        this.ioExternalEngine = ioExternalEngine;
    }
    //</editor-fold>
    
}
