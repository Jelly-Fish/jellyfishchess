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
package fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.helpers;

import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.constants.UI3DConst;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.gl3dobjects.ChessSquare;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.ColorUtils;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.Location3DUtils;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.SoundUtils;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.dto.Game3D;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.dto.Move;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.enums.ChessPositions;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.exceptions.FenValueException;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.opengl.utils.ChessUtils;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.chessui3d.time.StopWatch;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.constants.MessageTypeConst;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.exceptions.InvalidChessPositionException;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.exceptions.InvalidMoveException;
import fr.com.jellyfish.jfgjellyfishchess.jellyfishchess.jellyfish.exceptions.PawnPromotionException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author thw
 */
public class MouseEventHelper {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     *
     */
    private final OPENGLUIHelper uiHelper;

    /**
     * xyz coordinates onclick.
     */
    private int dx = 0, dy = 0, x = 0, y = 0;

    /**
     * Maximum elapsed time in ms between click events.
     */
    private static final double eventMaxInterval = 0.25;

    /**
     * Stop watch for prevent event redundancy.
     */
    private StopWatch stopwatch = new StopWatch(MouseEventHelper.eventMaxInterval);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param uiHelper
     * @param color
     */
    MouseEventHelper(final OPENGLUIHelper uiHelper, final String color) {
        this.uiHelper = uiHelper;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * @param squares Map<ChessPositions, ChessSquare>
     */
    void selectedSquareEvent(final Map<ChessPositions, ChessSquare> squares) {

        if (Mouse.isButtonDown(0)
                && !Game3D.isEngineMoving()
                && !Game3D.isEngineSearching()
                && this.stopwatch.hasReachedMaxElapsedMS()
                && !Game3D.isUiCheckmate()
                && !Game3D.isEngineCheckmate()) {

            /**
             * If wrong turn.
             */
            if (!this.uiHelper.driver.game.getColorToPLay().equals(Game3D.getEngineOponentColorStringValue())) {
                this.notifyWrongTurn();
                this.stopwatch = new StopWatch(MouseEventHelper.eventMaxInterval);
                return;
            }

            this.dx = Mouse.getDX();
            this.dy = Mouse.getDY();
            this.x = Mouse.getX();
            this.y = Mouse.getY();
            final Vector3f v = Location3DUtils.getMousePositionIn3dCoordinates(x, y);

            for (Map.Entry<ChessPositions, ChessSquare> s : squares.entrySet()) {

                if (s.getValue().collidesWith(v)) {

                    Game3D.setUiMoving(true);

                    if (s.getValue().isOccupied()) {

                        if (s.getValue().getModel() != null
                                && ColorUtils.floatArrayEqual(s.getValue().getModel().getColor(), Game3D.getEngineColor())
                                && uiHelper.getBoard().getSelectedSquare() != null
                                && uiHelper.getBoard().getSelectedSquare().getModel() != null
                                && ColorUtils.floatArrayEqual(uiHelper.getBoard().getSelectedSquare().getModel().getColor(),
                                        Game3D.getEngineOponentColor())) {

                            // Take move.
                            doMove(s.getKey(), uiHelper.getBoard().getSelectedSquare().CHESS_POSITION, s.getValue());
                            break;
                        } else {

                            if ((s.getValue().getModel() != null
                                    && ColorUtils.floatArrayEqual(s.getValue().getModel().getColor(), Game3D.getEngineColor()))
                                    && uiHelper.getBoard().getSelectedSquare() == null) {
                                break;
                            }
                            // Selecting chess square for move.
                            s.getValue().setColor(UI3DConst.UI_MOVE_SQUARE_COLOR);
                            uiHelper.getBoard().setSelectedSquare(s.getValue());
                            uiHelper.getSoundManager().playEffect(SoundUtils.StaticSoundVars.bip);
                        }
                    } else if (uiHelper.getBoard().getSelectedSquare() != null) {
                        // Move without take.
                        doMove(s.getKey(), uiHelper.getBoard().getSelectedSquare().CHESS_POSITION, s.getValue());
                        break;
                    }
                } else {
                    s.getValue().setColliding(false);
                }
            }

            // Set correct colors to selected, non-selected & in-check squares.
            if (uiHelper.getBoard().getSelectedSquare() != null) {
                this.uiHelper.getBoard().resetSquareColors();
            }

            this.stopwatch = new StopWatch(MouseEventHelper.eventMaxInterval);
            // Free engine movement to impact UI via engine's response to this move.
            Game3D.setUiMoving(false);
        }
    }

    /**
     *
     * @param key ChessPositions
     * @param posFrom ChessPositions
     * @param value ChessSquare
     */
    private void doMove(final ChessPositions key, final ChessPositions posFrom, final ChessSquare value) {

        /**
         * Systematically set to false to enable display list deletion in gl
         * main loop.
         */
        Game3D.setUndoingMoves(false);

        if (uiHelper.getBoard().getSelectedSquare() != null && !Game3D.isEngineMoving()) {

            // Pawn promotion.
            final boolean pawnPromotion
                    = ChessUtils.isPawnPromotionMove(uiHelper.getBoard().getSquareMap().get(posFrom),
                            value, Game3D.getEngineOponentColorStringValue());

            try {

                // Stop hint seach if hints are enabled.
                this.uiHelper.driver.stopHintSearch(Game3D.isEnableHints());
                Thread.sleep(200);

                if (uiHelper.driver.game.executeMove(
                        uiHelper.getBoard().getSelectedSquare().CHESS_POSITION.getStrPositionValueToLowerCase(),
                        key.getStrPositionValueToLowerCase(), true, pawnPromotion, Game3D.getPawnPromotion())) {

                    /**
                     * Append move to queue for undoing.
                     */
                    Move m;
                    if (value.getModel() != null) {
                        m = new Move(this.uiHelper.driver.game.getMoveCount(), posFrom, key, false,
                                uiHelper.getBoard().getSelectedSquare().getModel(),
                                value.getModel());
                    } else {
                        m = new Move(this.uiHelper.driver.game.getMoveCount(), posFrom, key, false,
                                uiHelper.getBoard().getSelectedSquare().getModel());
                    }

                    if (pawnPromotion) {
                        m.addPawnPromotionData(Game3D.getPawnPromotion(), Game3D.getEngineColorStringValue());
                    }

                    uiHelper.driver.moveQueue.appendToEnd(m);

                    value.setColor(UI3DConst.UI_MOVE_SQUARE_COLOR);

                    if (pawnPromotion) {
                        uiHelper.getBoard().updateSquare(m.getPosTo(), m.getPosFrom(),
                                Game3D.getEngineOponentColor(), m.getPawnPromotionObjPath(),
                                m.getPawnPromotionPieceType());
                    } else {
                        uiHelper.getBoard().updateSquare(key,
                                uiHelper.getBoard().getSelectedSquare().CHESS_POSITION,
                                Game3D.getEngineOponentColor());
                    }

                    // Finally :
                    uiHelper.getBoard().setSelectedSquare(value);
                    uiHelper.getSoundManager().playEffect(SoundUtils.StaticSoundVars.move);
                    // If move is validated check & checkmate situation is impossible.
                    Game3D.setUiCheck(false);
                    Game3D.setUiCheckmate(false);
                    if (pawnPromotion) {
                        Game3D.setEngineCheck(this.uiHelper.driver.game.inCheckSituation(
                                Game3D.getEngineColorStringValue()));
                    }
                    Game3D.setEngineSearching(true);
                } else {
                    throw new InvalidMoveException(String.format("%s %s-%s is not a valid chess move.\n",
                            uiHelper.getBoard().getSelectedSquare().getModel().getType().toString(),
                            uiHelper.getBoard().getSelectedSquare().CHESS_POSITION.getStrPositionValue(),
                            key.getStrPositionValueToLowerCase()));
                }
            } catch (final PawnPromotionException ex) {
                Logger.getLogger(MouseEventHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (final InvalidMoveException ex) {
                this.uiHelper.driver.getWriter().appendText(ex.getMessage(), MessageTypeConst.ERROR, true);
                Logger.getLogger(MouseEventHelper.class.getName()).log(Level.WARNING, null, ex);
            } catch (final InterruptedException ex) {
                Logger.getLogger(MouseEventHelper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (final FenValueException ex) {
                Logger.getLogger(MouseEventHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            this.notifyWrongTurn();
        }
    }

    /**
     * Notify Console for a 'wrong turn' error, oponent side must play first.
     */
    private void notifyWrongTurn() {
        this.uiHelper.driver.getWriter().appendText(
                String.format("It is %s's side to take a move...\n", Game3D.getEngineColorStringValue()),
                MessageTypeConst.ERROR, true);
    }
    //</editor-fold>

}
